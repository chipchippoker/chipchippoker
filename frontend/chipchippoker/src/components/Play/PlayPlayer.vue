<template>
  <div class="w-100 h-100">
    <!-- 플레이어 감정, 동영상, 카드 -->
    <div class="w-100 h-100 position-relative">
      <div>
        <!-- 플레이어 정보 -->
        <div :class="classNameList[index]" :id="'player' + (index + 1)" v-for="(player, index) in gameMemberInfos"
          :key="index">
          <div class="text-white align-self-center ">{{ player.nickname }}
          </div>
          <!-- 1, 3번 플레이어 -->
          <div v-if="[0, 2].includes(index)" class="d-flex h-100 m-2 mt-0">
            <!-- 본인 -->
            <div class="m-2">
              <UserVideoVue :stream-manager="findVideo(playersComputed, player.nickname)" />
            </div>

            <!-- 뒤집 카드 -->
            <div class="flip-card" id="flip-card">
              <div class="flip-card-inner">
                <div class="flip-card-front">
                  <img :src="getCardUrl(0, 0)" alt="뒷면카드">
                </div>
                <div class="flip-card-back">
                  <img :src="getCardUrl(player.cardInfo?.cardSet, player.cardInfo?.cardNumber)" alt="앞장">
                </div>
              </div>
            </div>
          </div>
          <!-- 2, 4번 플레이어 -->
          <div v-else class="d-flex flex-row-reverse h-100 m-2 mt-0">
            <div class="m-2">
              <UserVideoVue :stream-manager="findVideo(playersComputed, player.nickname)" />
            </div>
            <!-- 뒤집 카드 -->
            <div class="flip-card" id="flip-card">
              <div class="flip-card-inner">
                <div class="flip-card-front">
                  <img :src="getCardUrl(0, 0)" alt="뒷면카드">
                </div>
                <div class="flip-card-back">
                  <img :src="getCardUrl(player.cardInfo?.cardSet, player.cardInfo?.cardNumber)" alt="앞장">
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 배팅보드 -->
      <PlayBattingVue />

      <!-- 남은 시간, 총 배팅 코인 -->
      <div class="position-absolute top-0 start-50 translate-middle mt-4 d-flex flex-column align-items-center">
        <div class="text-white fs-3 fw-bold"> 초</div>
        <div class="text-white fw-bold">턴: {{ gameStore.yourTurn }}</div>
      </div>
    </div>
    <!-- 나가기 -->
    <div class="position-absolute bottom-0 end-0">
      <button
        class="bg-danger me-3 mb-3 text-white fw-bold rounded-pill d-flex justify-content-center align-items-center border border-2 border-white"
        data-bs-toggle="modal" data-bs-target="#roomOutModal" style="width: 70px; height: 30px">나가기</button>
    </div>

    <div data-bs-backdrop="static" class="modal fade" id="roomOutModal" tabindex="-1" aria-labelledby="IconModalLabel"
      aria-hidden="true">
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
              <button class="custom-btn btn-2" data-bs-dismiss="modal"
                style="width: 50px;"><span>게임하기</span><span>아니요</span></button>
              <button class="custom-btn btn-3" data-bs-dismiss="modal"
                @click="leaveRoom()"><span>나가?</span><span>나가기</span></button>
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
import { useGameStore } from "@/stores/game";
import { useRoomStore } from "@/stores/room";
import { useOpenviduStore } from "@/stores/openvidu";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore()
const gameStore = useGameStore()
const roomStore = useRoomStore()
const openviduStore = useOpenviduStore()
const router = useRouter()
const roomId = ref(null)
const myNickname = ref(null)

const props = defineProps({
  roomId: String,
  myNickname: String
});
// 클래스 부여하기
const classNameList = ["position-absolute top-0 start-0 d-flex flex-column h-50", "position-absolute top-0 end-0 d-flex flex-column h-50", "position-absolute bottom-0 start-0 d-flex flex-column h-50", "position-absolute bottom-0 end-0 d-flex flex-column h-50"]

console.log('게임방 멤버', gameStore.memberInfos);

roomId.value = props.roomId
myNickname.value = props.myNickname

// ----------------------------------------------------------------------------------------------------------
const findVideo = function (players, targetNickname) {
  for (let i = 0; i < players.length; i++) {
    const player = players[i]
    if (player.nickname === targetNickname) {
      return player.player
    }
  }
  return publisherComputed.value
}

//-----------------------------------------------------------------------------------------------------------

// 카드 가져오기
const getCardUrl = function (setnum, cardnum) {
  return new URL(`/src/assets/cards/set${setnum}/card${cardnum}.png`, import.meta.url).href;
};

// 카드 뒤집기
const flip = () => {
  const flipCards = document.querySelectorAll('.flip-card')
  console.log('카드 뒤집');
  flipCards.forEach(flipCard => {
    flipCard.classList.toggle('flipped')
  });
};

const playersComputed = computed(() => openviduStore.players)
const publisherComputed = computed(() => openviduStore.publisher)

const roundState = computed(() => gameStore.roundState);

// 애니메이션과 시간차 관련 로직 --------------------------------------------------------------------------------
const gameMemberInfos = ref(null)

const getGameData = function () {
  gameMemberInfos.value = gameStore.gameMemberInfos
}

// 라운드 상태 감지
watch(roundState, (newValue, oldValue) => {
  if (newValue === true && oldValue === false) {
    // 게임 시작시
    if (gameStore.currentRound === 1) {
      Promise.all([
        // 바로 게임 데이터 받아오기
        getGameData(),
      ])
        .then(() => {
          //  카드 앞으로 뒤집기 (실행효과가 안보이는 이유 모르겠음)
          flip()
        })
    } else {
      // 라운드 시작시
      Promise.all([
        // 데이터 받아오기
        getGameData()
      ])
      // 데이터 받아온 뒤에 카드 앞장 뒤집기
        .then(() => {
          flip()
        })
    }
  }
  // 라운드 종료시
  else if (newValue === false && oldValue === true) {
    // 카드 뒤로 뒤집기
    flip()
  }
})

// 방 나가기
const leaveRoom = function () {
  // 관전자면
  if (roomStore.isWatcher === true) {
    roomStore.leaveWatcher()
    roomStore.isWatcher = false
  } else {
    roomStore.leaveRoom()
  }
}

onMounted(() => {

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
  box-shadow: inset 2px 2px 2px 0px rgba(255, 255, 255, .5),
    7px 7px 20px 0px rgba(0, 0, 0, .1),
    4px 4px 5px 0px rgba(0, 0, 0, .1);
  outline: none;
}

@-webkit-keyframes shiny-btn1 {
  0% {
    -webkit-transform: scale(0) rotate(45deg);
    opacity: 0;
  }

  80% {
    -webkit-transform: scale(0) rotate(45deg);
    opacity: 0.5;
  }

  81% {
    -webkit-transform: scale(4) rotate(45deg);
    opacity: 1;
  }

  100% {
    -webkit-transform: scale(50) rotate(45deg);
    opacity: 0;
  }
}

/* 12 */
.btn-1 {
  position: relative;
  right: 20px;
  bottom: 20px;
  border: none;
  box-shadow: none;
  width: 60px;
  height: 40px;
  line-height: 42px;
  -webkit-perspective: 230px;
  perspective: 230px;
}

.btn-2 {
  position: relative;
  right: 20px;
  bottom: 20px;
  border: none;
  box-shadow: none;
  width: 80px;
  height: 40px;
  line-height: 42px;
  -webkit-perspective: 230px;
  perspective: 230px;
}

.btn-3 {
  position: relative;
  right: 20px;
  bottom: 20px;
  border: none;
  box-shadow: none;
  width: 60px;
  height: 40px;
  line-height: 42px;
  -webkit-perspective: 230px;
  perspective: 230px;
}

.btn-1 span {
  background: rgb(0, 172, 238);
  background: linear-gradient(0deg, rgba(0, 172, 238, 1) 0%, rgba(2, 126, 251, 1) 100%);
  display: block;
  position: absolute;
  width: 60px;
  height: 40px;
  box-shadow: inset 2px 2px 2px 0px rgba(255, 255, 255, .5),
    7px 7px 20px 0px rgba(0, 0, 0, .1),
    4px 4px 5px 0px rgba(0, 0, 0, .1);
  border-radius: 5px;
  margin: 0;
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
  box-shadow: inset 2px 2px 2px 0px rgba(255, 255, 255, .5),
    7px 7px 20px 0px rgba(0, 0, 0, .1),
    4px 4px 5px 0px rgba(0, 0, 0, .1);
  border-radius: 5px;
  margin: 0;
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
  box-shadow: inset 2px 2px 2px 0px rgba(255, 255, 255, .5),
    7px 7px 20px 0px rgba(0, 0, 0, .1),
    4px 4px 5px 0px rgba(0, 0, 0, .1);
  border-radius: 5px;
  margin: 0;
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
  box-shadow: inset 2px 2px 2px 0px rgba(255, 255, 255, .5),
    7px 7px 20px 0px rgba(0, 0, 0, .1),
    4px 4px 5px 0px rgba(0, 0, 0, .1);
  -webkit-transform: rotateX(0deg);
  -moz-transform: rotateX(0deg);
  transform: rotateX(0deg);
}

.custom-btn:hover span:nth-child(2) {
  box-shadow: inset 2px 2px 2px 0px rgba(255, 255, 255, .5),
    7px 7px 20px 0px rgba(0, 0, 0, .1),
    4px 4px 5px 0px rgba(0, 0, 0, .1);
  color: transparent;
  -webkit-transform: rotateX(-90deg);
  -moz-transform: rotateX(-90deg);
  transform: rotateX(-90deg);
}
</style>
