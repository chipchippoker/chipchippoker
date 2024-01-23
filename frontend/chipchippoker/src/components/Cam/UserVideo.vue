<template>
  <div class="position-relative" v-if="streamManager" @mouseover="showControls = true" @mouseleave="showControls = false">
    <ov-video :stream-manager="streamManager"/>
    <!-- {{ clientData }}이게 내 현재 이름임 이거 지우면 실행 안됨, 안보이게 투명하게 해야하나? -->
    <div><p class="opacity-0">{{ clientData }}</p></div>
    <div v-if="showControls" class="position-absolute top-0 end-0 d-flex flex-column">
      <button id="camera-activate" @click="handleCameraBtn()">Video Off</button>
      <button id="mute-activate" @click="handleMuteBtn()">Sound Off</button>
      <button v-if="view='waitView'" @click="forceDisconnect()">강퇴</button>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'UserVideo',
  }
</script>

<script setup>
  import { ref, onMounted, computed, } from 'vue';
  import OvVideo from '@/components/Cam/OvVideo.vue';
  ///////////////////카메라 및 오디오 설정을 위한 부분임
  const muted = ref(false)       // 기본은 음소거 비활성화
  const camerOff = ref(false)    // 기본 카메라 활성화

  const props = defineProps({
    streamManager: Object,
    view: String,
  })

  const showControls = ref(false);


  // clientData는 computed로 진행됨
  const clientData = computed(() => {
    const { clientData } = getConnectionData();
    emit('clientData', clientData);
    return clientData;
  });
  // console.log(clientData)
  
  const emit = defineEmits(['clientData', 'forceDisconnect'])
  
  function getConnectionData() {
    const { connection } = props.streamManager.stream;
    return JSON.parse(connection.data);
  }

  // 강제 퇴장 버튼 작동
  function forceDisconnect () {
    emit('forceDisconnect', 'forceDisconnect')
  }

  // 음소거, 캠 활성화 버튼 작동
  function handleCameraBtn() {
    if (!props.streamManager) return;
    // 카메라 상태 토글
    camerOff.value = !camerOff.value;
    const cameraActivate = document.getElementById('camera-activate')
    if(camerOff.value){   //카메라 비활성화상태
      cameraActivate.innerText = 'Video On'
    }else{                //카메라 활성화상태
      cameraActivate.innerText = 'Video Off'
    }
    // remote가 true면 상대방, false면 자신
    const remote = props.streamManager.remote
    // 카메라 작동 상태를 적용
    if (remote === true) {
      props.streamManager.subscribeToVideo(!camerOff.value);
    } else {
      props.streamManager.publishVideo(!camerOff.value);
    }
  }

  function handleMuteBtn() {
    if (!props.streamManager) return;

    // 음소거 상태 토글
    muted.value = !muted.value;
    const muteActivate = document.getElementById('mute-activate')
    if(muted.value){   //음소거 활성화상태
      muteActivate.innerText = 'Sound On'
    }else{                //음소거 비활성화상태
      muteActivate.innerText = 'Sound Off'
    }
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
  
