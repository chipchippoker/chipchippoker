<template>
  <div class="position-absolute d-flex flex-column align-items-center w-100 h-100">
    <!-- round, 관전 -->
    <div class="m-3 d-flex justify-content-center position-relative" style="width: 90%;">
      <div class="btn-outline-weightyellow py-2 px-4"><p class="fw-bold fs-3 m-0">round 1</p></div>
      <div class="d-flex align-items-center position-absolute top-50 start-100 translate-middle me-3">
        <font-awesome-icon :icon="['fas', 'eye']" size="lg" style="color: #ffffff;" />
        <div class="text-white ms-3" style="width: 40px;">3 명</div>
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
  import PlayControllerVue from "../components/Play/PlayController.vue";
  import PlayPlayerVue from "../components/Play/PlayPlayer.vue";
  import PlayTalkVue from "../components/Play/PlayTalk.vue";
  import { ref, computed, onMounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router';

  const router = useRouter()
 
  // Join form
  const roomId = ref("SessionCrome")
  const myNickname = ref("Participant" + Math.floor(Math.random() * 100))
  
  const route = useRoute()
  

  
</script>

<style scoped>

</style>