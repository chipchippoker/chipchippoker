<template>
  <div class="position-absolute d-flex flex-column align-items-center w-100 h-100">
    <!-- round, 관전 -->
    <div class="m-3 d-flex justify-content-center position-relative" style="width: 90%;">
      <!-- 라운드 시작 전이면 대기중 아니면 라운드 수 -->
      <div v-if="gameStore.currentRound === 0" class="btn-outline-weightyellow py-2 px-4">
        <p class="fw-bold fs-3 m-0">대기중</p>
      </div>
      <div v-else class="btn-outline-weightyellow py-2 px-4">
        <p class="fw-bold fs-3 m-0">Round {{ gameStore.currentRound }}</p>
      </div>

      <div class="position-absolute top-50 end-0 translate-middle-y">
        <div class="d-flex align-items-center">
          <h3 class="text-white me-5 mb-0">{{ roomTitle }}</h3>
          <!-- Watchers List -->
          <div v-if="showWatchersList" class="text-white">
            <ul>
              <li v-for="watcher in gameStore.watchersNickname" :key="watcher">{{ watcher }}</li>
            </ul>
          </div>
          <font-awesome-icon type="button" @click="toggleWatchersList" :icon="['fas', 'eye']" size="lg"
            style="color: #ffffff;" />
          <div class="text-white ms-3" style="width: 40px;">{{ gameStore.watchersNickname?.length }}명</div>
        </div>
      </div>
    </div>

    <!-- players -->
    <div class="w-100 position-relative" style="height: 70%;">
      <!-- 결과표 -->
      <!-- 라운드 결과 -->
      <div v-if="gameStore.roundState == false && gameStore.memberEndGameInfos.length == 0 && gameStore.winnerNickname != ''">
      </div>

      <!-- 게임 결과 -->
      <div v-else-if="gameStore.memberEndGameInfos.length && gameStore.winnerNickname != ''" class="position-absolute top-50 start-50 translate-middle bg-modal
       rounded-5 px-5 pt-5" style="z-index: 999; width: 40%;">
        <div class="d-flex flex-column justify-content-center align-items-center text-center">
          <h2 class="fw-bold">게임 결과</h2>
          <div class="" v-for="playerResult in gameStore.memberEndGameInfos" :key="playerResult.nickname">
            <h3 class="m-3 mb-0">{{ playerResult.nickname }}님 <span
                :class="[{ 'text-primary': playerResult.isResult == '승' }, { 'text-danger': playerResult.isResult == '패' }]">{{
                  playerResult.isResult }}</span></h3>
            <p v-if="gameStore.kindGame==='경쟁전'"
            :class="[{ 'text-primary': playerResult.isResult == '승' }, { 'text-danger': playerResult.isResult == '패' }]"
            >pointChange: {{ playerResult.pointChange }}</p>
          </div>
          <!-- 매치 종료 후 메인페이지로 이동 -->
            <button type="button" class="btn-2 btn-2-yellow my-3 justify-content-center align-items-center" style="width: 70px; height: 30px; font-size: 15px;" @click="gotoMain()">나가기</button>
        </div>
      </div>

      <!-- 플레이어 화면 / 카드 / 감정 / 정보 -->
      <PlayPlayerVue />
    </div>

    <!-- 채팅, 카드, 컨트롤러 -->
    <div class="d-flex align-items-center mb-3" style="width: 100%; height: 20%;">
      <!-- 카드 세트-->
      <div class="w-25">
        <div class="d-flex flex-wrap p-3 ms-5">
          <img v-for="idx in (10 - gameStore.currentRound)" :key="idx" class="object-fit-contain list-overlap"
            style="width: 80px;" src="/src/assets/cards/set0/card0.png" alt="back">
        </div>
      </div>

      <!-- 컨트롤러 or 채팅 -->
      <div class="h-100 w-50">
        <PlayControllerVue v-if="roomStore.isWatcher === false"/>
        <PlayTalkVue v-else />
        <!-- <PlayTalkVue /> -->
      </div>

      <!-- 이모지 or 로로 -->
      <div class="h-100 w-25 d-flex justify-content-center align-items-center">
        <!-- <PlayEmotionVue/> -->
        <!-- 나가기 -->
        <div class="">
          <button
            class="btn-2 btn-2-red"
            data-bs-toggle="modal" data-bs-target="#roomOutModal">나가기</button>
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
                <div class="d-flex justify-content-evenly mt-4">
                  <button class="btn-2 btn-3-blue " data-bs-dismiss="modal"
                    >게임하기</button>
                  <button class="btn-2 btn-3-red" data-bs-dismiss="modal"
                    @click="leaveRoom()">나가기</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import PlayControllerVue from "../components/Play/PlayController.vue";
import PlayPlayerVue from "../components/Play/PlayPlayer.vue";
import PlayTalkVue from "../components/Play/PlayTalk.vue";
import PlayEmotionVue from "../components/Play/PlayEmotion.vue";

import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useUserStore } from "@/stores/user";
import { useRoomStore } from "@/stores/room";
import { useGameStore } from "@/stores/game";
import { onBeforeRouteLeave } from "vue-router";
import router from "@/router";

const userStore = useUserStore()
const roomStore = useRoomStore()
const gameStore = useGameStore()

// Join form
const roomId = ref('')
const myNickname = ref('')
const roomTitle = ref('')
const totalParticipantCnt = ref('')

roomId.value = roomStore.roomId
myNickname.value = userStore.myNickname
roomTitle.value = roomStore.title
totalParticipantCnt.value = roomStore.totalParticipantCnt

const watchersNickname = computed(() => gameStore.watchersNickname)

const showWatchersList = ref(false)

const toggleWatchersList = () => {
  showWatchersList.value = !showWatchersList.value
}

// 배팅 오류
const notMatchRound = ref(false)
const notYourTurn = ref(false)
const cannotBat = ref(false)

notMatchRound.value = computed(() => gameStore.notMatchRound)
notYourTurn.value = computed(() => gameStore.notYourTurn)
cannotBat.value = computed(() => gameStore.cannotBat)

const closeModal = function () {
  // 각각의 모달 닫기 로직을 작성
  gameStore.notMatchRound = false;
  gameStore.notYourTurn = false;
  gameStore.cannotBat = false;
};

// 경쟁모드 종료 후 메인페이지로 이동
const gotoMain = function () {
  if (roomStore.isWatcher === true) {
    roomStore.leaveWatcher()
  } else {
    roomStore.leaveRoom()
  }
}

// 친선모두 종료 후 대기페이지로 이동
const goToWait = function () {
  router.push({
    name: 'wait',
    params: { roomId: roomStore.roomId },
  })
}

const cardSetName = {
  1:'클로버',
  2:'하트',
  3:'다이아몬드',
  4:'스페이드'
}

// 이전에 페이지가 새로고침된 횟수를 가져옵니다.
let refreshCount = localStorage.getItem('refreshCount');

// 새로고침 횟수를 증가시킵니다.
refreshCount = parseInt(refreshCount) + 1 || 1;

// 현재 새로고침 횟수를 로컬 스토리지에 저장합니다.
localStorage.setItem('refreshCount', refreshCount);

// 새로고침 횟수를 출력합니다.

// 새로고침 횟수가 2회가 넘어가면 새로고침을 했다는 것이므로 out
if (refreshCount >= 2) {
    if (roomStore.isWatcher === true) {
      roomStore.leaveWatcher()
    } else {
      roomStore.leaveRoom()
    }
}

// 방 나가기
const leaveRoom = function () {
  // 관전자면
  if (roomStore.isWatcher === true) {
    roomStore.isWatcher = false
    roomStore.leaveWatcher()
  } else {
    roomStore.leaveRoom()
  }
}

onMounted(() => {
  window.addEventListener("beforeunload", (event) => {
    event.preventDefault()
    event.returnValue = '';
  })

  window.addEventListener("unload", (event) => {
    if (roomStore.isWatcher === true) {
      roomStore.leaveWatcher()
    } else {
      roomStore.leaveRoom()
    }
  }) 

  window.addEventListener("popstate", (event) => {
    if (roomStore.isWatcher === true) {
      roomStore.leaveWatcher()
    } else {
      roomStore.leaveRoom()
    }
  })
})

onUnmounted(() => {
  window.removeEventListener("beforeunload", (event) => {
    event.preventDefault()
    event.returnValue = '';
  })

  window.removeEventListener("unload", (event) => {
    if (roomStore.isWatcher === true) {
      roomStore.leaveWatcher()
    } else {
      roomStore.leaveRoom()
    }
  }) 

  window.removeEventListener("popstate", (event) => {
    if (roomStore.isWatcher === true) {
      roomStore.leaveWatcher()
    } else {
      roomStore.leaveRoom()
    }
  })

})

</script>

<style scoped>

</style>