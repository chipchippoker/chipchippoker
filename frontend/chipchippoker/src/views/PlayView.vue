<template>
  <div class="position-absolute d-flex flex-column align-items-center w-100 h-100">
    <!-- round, 관전 -->
    <div class="m-3 d-flex justify-content-center position-relative" style="width: 90%;">
      <!-- 라운드 시작 전이면 대기중 아니면 라운드 수 -->
      <div v-if="gameStore.currentRound === 0" class="btn-outline-weightyellow py-2 px-4"><p class="fw-bold fs-3 m-0">대기중</p></div>
      <div v-else class="btn-outline-weightyellow py-2 px-4"><p class="fw-bold fs-3 m-0">Round {{ gameStore.currentRound }}</p></div>

      <div class="position-absolute top-50 end-0 translate-middle-y">
        <div class="d-flex align-items-center">
          <h3 class="text-white me-5 mb-0">{{ roomTitle }}</h3>
          <!-- Watchers List -->
          <div v-if="showWatchersList" class="text-white">
            <ul>
              <li v-for="watcher in watchersNickname" :key="watcher">{{ watcher }}</li>
            </ul>
          </div>
          <font-awesome-icon type="button" @click="toggleWatchersList" :icon="['fas', 'eye']" size="lg" style="color: #ffffff;" />
          <div class="text-white ms-3" style="width: 40px;">{{ watchersCount }}명</div>
        </div>
      </div>
    </div>
    <!-- players -->
    <div class="w-100" style="height: 70%;">
      <!-- 플레이어 화면 / 카드 / 감정 / 정보 -->
      <PlayPlayerVue :roomId="roomId" :myNickname="myNickname" />
    </div>
    <!-- 채팅, 카드, 컨트롤러 -->
    <div class="d-flex align-items-center mb-3" style="width: 100%; height: 20%;">
      <!-- 카드 -->
      <div class="text-white w-25">
        card
      </div>
      <!-- 컨트롤러 or 채팅 -->
      <div class="h-100 w-50">
        <PlayControllerVue />
        <!-- <PlayTalkVue /> -->
      </div>
      <!-- 이모지 or 로로 -->
      <div class="h-100 w-25 d-flex justify-content-center align-items-center">
        <div class="d-flex justify-content-around w-100">
          <font-awesome-icon :icon="['fas', 'face-angry']" class="fa-5x" style="color: #9f0909;"/>
          <font-awesome-icon :icon="['fas', 'face-sad-cry']" class="fa-5x" style="color: #ece513;"/>
          <font-awesome-icon :icon="['fas', 'face-grin-tongue-wink']" class="fa-5x" style="color: #ffffff;"/>
        </div>
        <!-- <img class="middle-logo mb-5 me-5" src="/src/assets/icons/Logo.png" alt=""> -->
      </div>
    </div>
  </div>
</template>

<script setup>
  import { useUserStore } from "@/stores/user";
  import PlayControllerVue from "../components/Play/PlayController.vue";
  import PlayPlayerVue from "../components/Play/PlayPlayer.vue";
  import PlayTalkVue from "../components/Play/PlayTalk.vue";
  import { ref, computed, onMounted, onUnmounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router';
  import { useRoomStore } from "@/stores/room";
  import { useGameStore } from "@/stores/game";

  const router = useRouter()
  const userStore = useUserStore()
  const roomStore = useRoomStore()
  const route = useRoute()
  const gameStore = useGameStore()
  // Join form
  const roomId = ref('')
  const myNickname = ref('')
  const roomTitle = ref('')
  const totalParticipantCnt = ref('')
  
  roomId.value = route.params.roomId
  myNickname.value = userStore.myNickname
  roomTitle.value = roomStore.title
  totalParticipantCnt.value = roomStore.totalParticipantCnt
  console.log(roomTitle)

  const watchersNickname = computed(() => roomStore.watchersNickname)
  const watchersCount = computed(() => watchersNickname.value.length)
  
  const showWatchersList = ref(false)

  const toggleWatchersList = () => {
    showWatchersList.value = !showWatchersList.value
  }

  onMounted(() => {
    // 프로필 아이콘 안보이기
    userStore.viewProfileIcon = false
  })

  onUnmounted(() => {
    // 프로필 아이콘 보이기
    userStore.viewProfileIcon = true
  })
  
</script>

<style scoped>

</style>