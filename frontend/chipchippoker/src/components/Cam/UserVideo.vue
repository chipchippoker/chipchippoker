<template>
  <div class="position-relative text-center" v-if="streamManager" @mouseover="showControls = true" @mouseleave="showControls = false">
    <ov-video 
    class=""
    :class="{ 'is-turn': clientData === gameStore.yourTurn, 'is-die' : isDie, 'is-ready': isReady  }"
    :stream-manager="streamManager"
    @sendEmotion="sendEmotion"
    />
    <div class="position-absolute" :class="{'ready-effect': isReady}" style="color: transparent;">Ready</div>
    <!-- {{ clientData }}이게 내 현재 이름임 이거 지우면 실행 안됨, 안보이게 투명하게 해야하나? -->
    <!-- <img class="xx-small-icon"  :src='friendStore.getTierIconUrl(userInfo?.tier)'> -->
    <span class="mx-2 position-absolute" id="text" style="color: transparent;">{{ clientData }}</span>
    <!-- <span style="color: white;">{{ userInfo?.rank }}등</span> -->
    

    <!-- 감정 이모지 -->
    <div class="position-absolute top-0 start-0 p-3 text-white">
      <i v-if="maxEmotion == 'angry'" class="fa-solid fa-face-angry fa-2xl" style="color: #ff0000;"></i>
      <i v-if="maxEmotion == 'disgusted'" class="fa-solid fa-face-dizzy fa-2xl" style="color: #009e37;"></i>
      <i v-if="maxEmotion == 'fearful'" class="fa-solid fa-face-flushed fa-2xl" style="color: #74C0FC;"></i>
      <i v-if="maxEmotion == 'happy'" class="fa-solid fa-face-laugh-squint fa-2xl" style="color: #FFD43B;"></i>
      <i v-if="maxEmotion == 'neutral'" class="fa-solid fa-face-meh fa-2xl" style="color: #FFD43B;"></i>
      <i v-if="maxEmotion == 'sad'" class="fa-solid fa-face-sad-tear fa-2xl" style="color: #FFD43B;"></i>
      <i v-if="maxEmotion == 'surprised'" class="fa-solid fa-face-surprise fa-2xl" style="color: #74C0FC;"></i>
    </div>

    <div v-if="showControls" class="position-absolute top-0 end-0 d-flex flex-column">
      <div class="m-1">
        <button class="btn-2-blue-done" v-if="!camerOff" id="camera-activate" @click="handleCameraBtn()">Video Off</button>
        <button class="btn-2-blue" v-else id="camera-activate" @click="handleCameraBtn()">Video On</button>
      </div>
      <div class="m-1">
        <button class="btn-2-yellow-done" v-if="!muted" id="mute-activate" @click="handleMuteBtn()">Sound Off</button>
        <button class="btn-2-yellow" v-else id="mute-activate" @click="handleMuteBtn()">Sound On</button>
      </div>
      <!-- <button class="m-1 btn-2-green" id="show-emotion" @click="showExpression()">감정 인식 표</button> -->
      <!-- 방장일 때만 강퇴가 보이기 / 방장 자신은 안보이기 -->
      <button class="m-1 btn-2-red" v-if="isManager===true && clientData!==roomManagerNickname" @click="forceDisconnect()">강퇴</button>
    </div>

  </div>
</template>

<script>
  export default {
    name: 'UserVideo',
  }
</script>

<script setup>
  import { ref, computed, } from 'vue';
  import OvVideo from '@/components/Cam/OvVideo.vue';
  import { useUserStore } from '@/stores/user';
  import { useGameStore } from '@/stores/game';
  import { useFriendStore } from '@/stores/friend';
  import { useRoute } from 'vue-router';

  const userStore = useUserStore()
  const gameStore = useGameStore()
  const friendStore = useFriendStore()
  const userInfo = ref({})
  const route = useRoute()

  ///////////////////카메라 및 오디오 설정을 위한 부분임
  const muted = ref(false)       // 기본은 음소거 비활성화
  const camerOff = ref(false)    // 기본 카메라 활성화

  const props = defineProps({
    streamManager: Object,
    isManager: Boolean,
    roomManagerNickname:String,
  })

 
  const showControls = ref(false);


  // clientData는 computed로 진행됨
  const clientData = computed(() => {
    const { clientData } = getConnectionData()
    friendStore.allRankList.forEach(info => {
      if (info.nickname === clientData) {
        userInfo.value = info
      }
    })
    emit('clientData', clientData);
    return clientData;
  });
  // console.log(clientData)
  

   // emit으로 올라온 감정 추출
  const maxEmotion = ref(null)
  const emotion = ref(null)
  const sendEmotion = function (...args){
    maxEmotion.value = args[0]
    emotion.value = args[1]
    // 스토어에 닉네임에 따라 감정 저장
    if (clientData.value){
      gameStore.playerEmotion[clientData.value] = emotion.value
    }
  }
  // 감정표현 표 보여줄 사람 닉네임 저장
  const showExpression = function(){
    gameStore.showEmotionNickname = clientData.value
  }

  // 게임 준비 체크
  const isReady = computed(() => {
    const memberInfo = gameStore.memberInfos.find(info => info.nickname === clientData.value);
    if (route.name === 'wait') {
      return memberInfo ? memberInfo.isReady : false;
    } else {
      return false
    }
  })

  // 죽은 상태 체크
  const isDie = computed(() => {
    const memberInfo = gameStore.nextGameMemberInfos.find(info => info.nickname === clientData.value);
    if (memberInfo?.isState === 'DIE' || memberInfo?.haveCoin + memberInfo?.bettingCoin === 0) {
      return true
    } else {
      return false
    }
  })

  const emit = defineEmits(['clientData', 'forceDisconnect'])
  
  function getConnectionData() {
    const { connection } = props.streamManager.stream
    return JSON.parse(connection.data);
  }

  // 강제 퇴장 버튼 작동
  function forceDisconnect () {
    emit('forceDisconnect', clientData.value)
  }

  // 음소거, 캠 활성화 버튼 작동
  function handleCameraBtn() {
    if (!props.streamManager) return;
    // 카메라 상태 토글
    camerOff.value = !camerOff.value;
    // remote가 true면 상대방, false면 자신
    const remote = props.streamManager.remote
    // 카메라 작동 상태를 적용
    if (remote === true) {
      props.streamManager.subscribeToVideo(!camerOff.value);
    } else {
      props.streamManager.publishVideo(!camerOff.value);
    }
    // const cameraActivate = document.getElementById('camera-activate')
    // if(camerOff.value){   //카메라 비활성화상태
    //   cameraActivate.innerText = 'Video On'
    // }else{                //카메라 활성화상태
    //   cameraActivate.innerText = 'Video Off'
    // }
  }

  function handleMuteBtn() {
    if (!props.streamManager) return;

    // 음소거 상태 토글
    muted.value = !muted.value;
    // const muteActivate = document.getElementById('mute-activate')
    // if(muted.value){   //음소거 활성화상태
    //   muteActivate.innerText = 'Sound On'
    // }else{                //음소거 비활성화상태
    //   muteActivate.innerText = 'Sound Off'
    // }
    // remote가 true면 상대방, false면 자신
    const remote = props.streamManager.remote
    // 음소거 설정을 적용
    if (remote === true) {
      props.streamManager.subscribeToAudio(!muted.value);
    } else {
      props.streamManager.publishAudio(!muted.value);
    }
  }
  
</script>

<style lang="scss" scope>
.ready-border {
  border: 3px solid #ffcc00; /* 두껍고 밝은 색상 테두리 */
}

/* 내 턴 */
.is-turn {
  border: 5px solid green; /* 준비가 완료되었을 때의 테두리 색상 */
  border-radius: 30px;
  animation: pulse 1s infinite alternate; /* 테두리에 깜빡거리는 애니메이션 효과 */
}

@keyframes pulse {
  from {
    border-color: green;
    box-shadow: 0 0 10px green;
  }
  to {
    border-color: #00ff00;
    box-shadow: 0 0 20px #00ff00;
  }
}

.is-die {
  filter: grayscale(100%)
}

.is-ready {
  border: 5px solid green; /* 준비가 완료되었을 때의 테두리 색상 */
  border-radius: 30px;
  animation: pulse 1s infinite alternate; /* 테두리에 깜빡거리는 애니메이션 효과 */
}

.cam {
  position: relative;
}

.ready-effect {
  position: absolute;
  bottom: 30px;
  font-size: 50px;
}
</style>
  
