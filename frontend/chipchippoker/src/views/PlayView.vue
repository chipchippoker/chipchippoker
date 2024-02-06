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
              <li v-for="watcher in watchersNickname" :key="watcher">{{ watcher }}</li>
            </ul>
          </div>
          <font-awesome-icon type="button" @click="toggleWatchersList" :icon="['fas', 'eye']" size="lg"
            style="color: #ffffff;" />
          <div class="text-white ms-3" style="width: 40px;">{{ watchersCount }}명</div>
        </div>
      </div>
    </div>
    <!-- players -->
    <div class="w-100" style="height: 70%;">
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
        <PlayControllerVue v-if="roomStore.isWatcher === false" />
        <PlayTalkVue v-else />
        <!-- <PlayTalkVue /> -->
      </div>
      <!-- 이모지 or 로로 -->
      <div class="h-100 w-25 d-flex justify-content-center align-items-center">
        <div class="d-flex justify-content-around w-100 animate__jackInTheBox ">
          <font-awesome-icon :icon="['fas', 'face-angry']" class="fa-5x" style="color: #9f0909;" />
          <font-awesome-icon :icon="['fas', 'face-sad-cry']" class="fa-5x" style="color: #ece513;" />
          <font-awesome-icon :icon="['fas', 'face-grin-tongue-wink']" class="fa-5x" style="color: #ffffff;" />
        </div>
        <!-- <img class="middle-logo mb-5 me-5" src="/src/assets/icons/Logo.png" alt=""> -->
      </div>
    </div>
    <!-- 배팅 오류 모달 -->
    <!-- <div class="modal fade" id="IsPlayingModal" tabindex="-1" aria-labelledby="IsPlayingModal" aria-hidden="true"> 
      <div class="modal-dialog modal-dialog-centered" @close="closeModal">
          <div class="modal-content pb-3" style="background-color: #fff0c0;">
          <div class="modal-header border-0">
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body d-flex flex-column justify-content-center align-items-center">
            <h3 v-if="notMatchRound">현재 진행 중인 라운드가 아닙니다.</h3>
            <h3 v-else-if="notYourTurn">본인의 배팅 차례가 아닙니다.</h3>
            <h3 v-else-if="cannotBat">배팅 불가능한 코인 개수입니다.</h3>
        </div>
        </div>
      </div>
    </div> -->
  </div>
</template>

<script setup>
import { useUserStore } from "@/stores/user";
import PlayControllerVue from "../components/Play/PlayController.vue";
import PlayPlayerVue from "../components/Play/PlayPlayer.vue";
import PlayTalkVue from "../components/Play/PlayTalk.vue";
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRoomStore } from "@/stores/room";
import { useGameStore } from "@/stores/game";

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
console.log(roomTitle.value)

const watchersNickname = computed(() => roomStore.watchersNickname)
const watchersCount = computed(() => watchersNickname.value.length)

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

onMounted(() => {
  // 프로필 아이콘 안보이기
  userStore.viewProfileIcon = false
  // 새로고침할때 구독 재연결  
})

// window.addEventListener('beforeunload', (event) => {
//   // 새로고침 감지 코드

// })

onUnmounted(() => {
  // 프로필 아이콘 보이기
  userStore.viewProfileIcon = true
})

</script>

<style scoped></style>