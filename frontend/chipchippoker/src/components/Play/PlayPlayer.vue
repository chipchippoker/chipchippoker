<template>
  <div class="w-100 h-100">
    <!-- 플레이어 감정, 동영상, 카드 -->
    <div class="w-100 h-100 position-relative">
      <!-- 관전 중이 아니고 게임하는 플레이어일 때 -->
      <div v-if="roomStore.isWatcher===false">
        <div class="position-absolute top-0 start-0 d-flex flex-column h-50" id="player1" style="width: 500px;">
          <div class="text-white align-self-center ">{{ myNickname }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 0">보유코인: {{gameStore?.gameMemberInfos[0]?.havingCoin}}</div>

          </div>
          <div class="d-flex m-2 mt-0 h-100">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              :stream-manager="publisherComputed"
              view="playView"
              />
            </div>
            <!-- 내 카드는 뒷면을 기본적으로 보여주기 -->
            <img class="object-fit-contain" style="width: 150px;" :src=getBackCardUrl() alt="?">
            
          </div>
        </div>
        <div v-if="playersComputed.length > 0" class="position-absolute top-0 end-0 d-flex flex-column h-50" id="player2" style="width: 500px;">
          <div class="text-white align-self-center ">{{ player1 }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 1">보유코인: {{gameStore?.gameMemberInfos[1]?.havingCoin}}</div>
          
          </div>
          <div class="d-flex flex-row-reverse h-100 m-2 mt-0">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              v-if="playersComputed.length > 0"
              :stream-manager="playersComputed[0]"
              view="playView"
              @client-data="fPlayer1"
              />
            </div>
            <img class="object-fit-contain" style="width: 150px;" :src=getCardUrl(1,gameStore?.gameMemberInfos[1].cardNumber) alt="?">
            
          </div>
        </div>
        <div v-if="playersComputed.length > 1" class="position-absolute bottom-0 start-0 d-flex flex-column h-50" id="player3" style="width: 500px;">
          <div class="text-white align-self-center ">{{ player2 }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 2">보유코인: {{gameStore?.gameMemberInfos[2]?.havingCoin}}</div>
          
          </div>
          <div class="d-flex m-2 mt-0 h-100">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              v-if="playersComputed.length > 1"
              :stream-manager="playersComputed[1]"
              view="playView"
              @client-data="fPlayer2"
              />
            </div>
            <img class="object-fit-contain" style="width: 150px;" :src=getCardUrl(1,gameStore?.gameMemberInfos[2].cardNumber) alt="?">
          </div>
        </div>
        <div v-if="playersComputed.length > 2" class="position-absolute bottom-0 end-0 d-flex flex-column h-50" id="player4" style="width: 500px;">
          <div class="text-white align-self-center">{{ player3 }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 3">보유코인: {{gameStore?.gameMemberInfos[3]?.havingCoin}}</div>
          
          </div>
          <div class="d-flex flex-row-reverse m-2 h-100 mt-0">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              v-if="playersComputed.length > 2"
              :stream-manager="playersComputed[2]"
              view="playView"
              @client-data="fPlayer3"
              />
            </div>

            <img class="object-fit-contain" style="width: 150px;" :src=getCardUrl(1,gameStore?.gameMemberInfos[3].cardNumber) alt="?">
            
          </div>
        </div>
      </div>

      <!-- 관전 중일 때 -->
      <div v-else>
        <div v-if="playersComputed.length > 0" class="position-absolute top-0 start-0 d-flex flex-column h-50" id="player1" style="width: 500px;">
          <div class="text-white align-self-center ">{{ gameStore?.gameMemberInfos[0]?.nickname }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 0">보유코인: {{gameStore?.gameMemberInfos[0]?.havingCoin}}</div>
          </div>
          <div class="d-flex m-2 mt-0 h-100">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              :stream-manager="playersComputed[0]"
              view="playView"
              />
            </div>
            <img class="object-fit-contain" style="width: 150px;" :src=getBackCardUrl(1,gameStore?.gameMemberInfos[0].cardNumber) alt="?">         
          </div>
        </div>
        <div v-if="playersComputed.length > 1" class="position-absolute top-0 end-0 d-flex flex-column h-50" id="player2" style="width: 500px;">
          <div class="text-white align-self-center " >{{ gameStore?.gameMemberInfos[1]?.nickname }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 1">보유코인: {{gameStore?.gameMemberInfos[1]?.havingCoin}}</div>
          </div>
          <div class="d-flex flex-row-reverse h-100 m-2 mt-0">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              :stream-manager="playersComputed[1]"
              view="playView"
              @client-data="fPlayer1"
              />
            </div>
            <img class="object-fit-contain" style="width: 150px;" :src=getCardUrl(1,gameStore?.gameMemberInfos[1].cardNumber) alt="?">
          </div>
        </div>
        <div v-if="playersComputed.length > 2" class="position-absolute bottom-0 start-0 d-flex flex-column h-50" id="player3" style="width: 500px;">
          <div class="text-white align-self-center ">{{ gameStore?.gameMemberInfos[2]?.nickname }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 2">보유코인: {{gameStore?.gameMemberInfos[2]?.havingCoin}}</div>
          </div>
          <div class="d-flex m-2 mt-0 h-100">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              :stream-manager="playersComputed[2]"
              view="playView"
              @client-data="fPlayer2"
              />
            </div>
            <img class="object-fit-contain" style="width: 150px;" :src=getCardUrl(1,gameStore?.gameMemberInfos[2].cardNumber) alt="?">
          </div>
        </div>
        <div v-if="playersComputed.length > 3" class="position-absolute bottom-0 end-0 d-flex flex-column h-50" id="player4" style="width: 500px;">
          <div class="text-white align-self-center">{{ gameStore?.gameMemberInfos[3]?.nickname }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 3">보유코인: {{gameStore?.gameMemberInfos[3]?.havingCoin}}</div>
          
          </div>
          <div class="d-flex flex-row-reverse m-2 h-100 mt-0">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              :stream-manager="playersComputed[3]"
              view="playView"
              @client-data="fPlayer3"
              />
            </div>
            <img class="object-fit-contain" style="width: 150px;" :src=getCardUrl(1,gameStore?.gameMemberInfos[3].cardNumber) alt="?">
          </div>
        </div>
      </div>
      <!-- 배팅보드 -->
      <PlayBattingVue />
      <!-- 남은 시간, 총 배팅 코인 -->
      <div class="position-absolute top-0 start-50 translate-middle mt-4 d-flex flex-column align-items-center">
        <div class="text-white fs-3 fw-bold">{{ time }} 초</div>
        <div class="text-white fw-bold">총 배팅: {{ totalBettingCoin }}</div>
      </div>
    </div>
    <!-- 나가기 -->
    <div class="position-absolute bottom-0 end-0">
      <button class="bg-danger me-3 mb-3 text-white fw-bold rounded-pill d-flex justify-content-center align-items-center border border-2 border-white" 
      data-bs-toggle="modal" data-bs-target="#roomOutModal" style="width: 70px; height: 30px" >나가기</button>
    </div>

    <div class="modal fade" id="roomOutModal" tabindex="-1" aria-labelledby="IconModalLabel" aria-hidden="true">
      <!-- 나가기 모달 -->
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content" style="background-color: #ffde76;">
          <div class="modal-header border-0">
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div class="text-center fw-bold fs-3 bg-modal-yellow">
              정말 나가시겠습니까?
            </div>
            <div class="mt-5 d-flex justify-content-around">
                <button class="custom-btn btn-2" data-bs-dismiss="modal" style="width: 50px;"><span>게임하기</span><span>아니요</span></button>
                <button class="custom-btn btn-3" data-bs-dismiss="modal" @click="leaveSession()"><span>나가?</span><span>나가기</span></button>
            </div>
          </div>
      
        </div>
      </div>  
    </div>
  </div>
</template>

<script setup>
  import PlayBattingVue from "@/components/Play/PlayBatting.vue";
  import UserVideoVue from "../Cam/UserVideo.vue";
  import { ref, defineProps, computed, onMounted, watch } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import axios from 'axios'
  import { OpenVidu } from "openvidu-browser";
  import { useGameStore } from "@/stores/game";
  import { useRoomStore } from "@/stores/room";
  import { useOpenviduStore } from "@/stores/openvidu";

  const gameStore = useGameStore()
  const roomStore = useRoomStore()
  const openviduStore = useOpenviduStore()
  // 앞장 카드 가져오기
  const getCardUrl = function (setnum, cardnum) {
      return new URL(`/src/assets/cards/set${setnum}/card${cardnum}.png`, import.meta.url).href;
  };
  // 뒷장 카드 가져오기
  const getBackCardUrl = function () {
      return new URL(`/src/assets/cards/back.png`, import.meta.url).href;
  }

  const router = useRouter()

  const roomId = ref(null)
  const myNickname = ref(null)
  const props = defineProps({
    roomId: String,
    myNickname: String
  });

  roomId.value = props.roomId
  myNickname.value = props.myNickname

  
  ////다시그려내기 위해 computed 작성
  const publisherComputed = computed(() => openviduStore.publisher);
  const subscribersComputed = computed(() => openviduStore.subscribers);
  
  // 자신을 제외한 다른 player들
  const player1 = ref(null)
  const player2 = ref(null)
  const player3 = ref(null)

  // 플레이어 배열 초기화
  gameStore.player = [myNickname.value]


  // 시간 15초에서 줄어들기
  const yourTurn = ref(null)
  const time = ref(15)

  const handleYourTurnChange = () => {
    yourTurn.value = gameStore.yourTurn; // yourTurn 업데이트

    if (yourTurn.value !== null) {
      // yourTurn 값이 존재하면 타이머 시작
      startCountdownTimer();
    } else {
      // yourTurn 값이 null이면 타이머 중지
      stopCountdownTimer();
    }
  };

  // 카운트다운 타이머 변수
  let countdownTimer;

  // 타이머 시작 함수
  const startCountdownTimer = function()  {
    time.value = 15; // 초기 남은 시간 15초

    // 1초마다 실행되는 타이머 함수
    countdownTimer = setInterval(() => {
      time.value--;

      if (time.value >= 0) {
        // 남은 시간이 0보다 크면 업데이트
        console.log('남은 시간:', time.value);
      } else {
        // 남은 시간이 0이면 "die"로 응답 보내고 타이머 중지
        console.log('남은 시간: 0, die로 응답 보내기');
        gameStore.bet(roomStore.title,"DIE", gameStore.bettingCoin)
        stopCountdownTimer();
        // 여기에서 "die"로 응답 보내는 로직 추가
      }
    }, 1000);
  };

  // 타이머 중지 함수
  const stopCountdownTimer = () => {
    clearInterval(countdownTimer);
  };

  // yourTurn 값이 변경될 때 호출되는 함수 등록
  watch(() =>gameStore.yourTurn, handleYourTurnChange)


  // 합
  const totalBettingCoin = computed(() => {
    return gameStore.gameMemberInfos.reduce((total, member) => total + member.bettingCoin, 0);
  });
  
  // 플레이어
  const fPlayer1 = function(clientData) {
    player1.value = clientData
    // 게임 store 에서 player 저장
    if (player1.value != null){
      gameStore.player.push(player1.value)
    }
  }
  const fPlayer2 = function(clientData) {
    player2.value = clientData
    if (player2.value != null){
      gameStore.player.push(player2.value)
    }
  }
  const fPlayer3 = function(clientData) {
    player3.value = clientData
    if (player3.value != null){
      gameStore.player.push(player3.value)
    }
  }
  
  /// 관전을 위한 변수들 크헝헝
  const playersComputed = computed(() => openviduStore.players);
  const watchersComputed = computed(() => openviduStore.watchers);

  onMounted (() => {
    navigator.mediaDevices.getUserMedia({ video: true, audio: true })
      .then((stream) => {
        // 카메라 및 마이크에 대한 성공적인 액세스 처리
      })
      .catch((error) => {
        console.error('카메라 및 마이크 액세스 오류:', error);
      });

      openviduStore.joinSession()
  })
</script>

<style scoped>
.custom-btn {
  width: 60px;
  height: 40px;
  color: #fff;
  border-radius: 5px;
  padding: 10px 25px;
  font-family: 'Lato', sans-serif;
  font-weight: 500;
  background: transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  display: inline-block;
   box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  outline: none;
}

@-webkit-keyframes shiny-btn1 {
    0% { -webkit-transform: scale(0) rotate(45deg); opacity: 0; }
    80% { -webkit-transform: scale(0) rotate(45deg); opacity: 0.5; }
    81% { -webkit-transform: scale(4) rotate(45deg); opacity: 1; }
    100% { -webkit-transform: scale(50) rotate(45deg); opacity: 0; }
}

/* 12 */
.btn-1{
  position: relative;
  right: 20px;
  bottom: 20px;
  border:none;
  box-shadow: none;
  width: 60px;
  height: 40px;
  line-height: 42px;
  -webkit-perspective: 230px;
  perspective: 230px;
}
.btn-2{
  position: relative;
  right: 20px;
  bottom: 20px;
  border:none;
  box-shadow: none;
  width: 80px;
  height: 40px;
  line-height: 42px;
  -webkit-perspective: 230px;
  perspective: 230px;
}
.btn-3{
  position: relative;
  right: 20px;
  bottom: 20px;
  border:none;
  box-shadow: none;
  width: 60px;
  height: 40px;
  line-height: 42px;
  -webkit-perspective: 230px;
  perspective: 230px;
}
.btn-1 span {
  background: rgb(0,172,238);
background: linear-gradient(0deg, rgba(0,172,238,1) 0%, rgba(2,126,251,1) 100%);
  display: block;
  position: absolute;
  width: 60px;
  height: 40px;
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  border-radius: 5px;
  margin:0;
  text-align: center;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  -webkit-transition: all .3s;
  transition: all .3s;
}
.btn-2 span {
  background: rgb(45, 217, 82);
background: linear-gradient(0deg, rgb(45, 217, 82) 0%, rgb(33, 168, 62) 100%);
  display: block;
  position: absolute;
  width: 80px;
  height: 40px;
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  border-radius: 5px;
  margin:0;
  text-align: center;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  -webkit-transition: all .3s;
  transition: all .3s;
}
.btn-3 span {
  background: rgb(218, 62, 62);
background: linear-gradient(0deg, rgb(218, 62, 62) 0%, rgb(215, 30, 30) 100%);
  display: block;
  position: absolute;
  width: 60px;
  height: 40px;
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  border-radius: 5px;
  margin:0;
  text-align: center;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  -webkit-transition: all .3s;
  transition: all .3s;
}
.custom-btn span:nth-child(1) {
  box-shadow:
   -7px -7px 20px 0px #fff9,
   -4px -4px 5px 0px #fff9,
   7px 7px 20px 0px #0002,
   4px 4px 5px 0px #0001;
  -webkit-transform: rotateX(90deg);
  -moz-transform: rotateX(90deg);
  transform: rotateX(90deg);
  -webkit-transform-origin: 50% 50% -20px;
  -moz-transform-origin: 50% 50% -20px;
  transform-origin: 50% 50% -20px;
}
.custom-btn span:nth-child(2) {
  -webkit-transform: rotateX(0deg);
  -moz-transform: rotateX(0deg);
  transform: rotateX(0deg);
  -webkit-transform-origin: 50% 50% -20px;
  -moz-transform-origin: 50% 50% -20px;
  transform-origin: 50% 50% -20px;
}
.custom-btn:hover span:nth-child(1) {
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  -webkit-transform: rotateX(0deg);
  -moz-transform: rotateX(0deg);
  transform: rotateX(0deg);
}
.custom-btn:hover span:nth-child(2) {
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
 color: transparent;
  -webkit-transform: rotateX(-90deg);
  -moz-transform: rotateX(-90deg);
  transform: rotateX(-90deg);
}
</style>
