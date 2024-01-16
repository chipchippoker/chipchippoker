<template>
  <div class="d-flex flex-column align-items-center w-100">
    <!-- round, 관전 -->
    <div class="m-3 d-flex justify-content-center position-relative" style="width: 90%;">
      <div class="btn-outline-weightyellow py-2 px-4"><p class="fw-bold fs-3 m-0">round 1</p></div>
      <div class="d-flex align-items-center position-absolute top-50 start-100 translate-middle me-3">
        <font-awesome-icon :icon="['fas', 'eye']" size="lg" style="color: #ffffff;" />
        <div class="text-white ms-3" style="width: 40px;">3 명</div>
      </div>
    </div>
    <!-- players -->
    <div class="position-relative w-100" style="height: 70%;">
      <!-- 플레이어 화면 / 카드 / 감정 / 정보 -->
      <PlayPlayerVue />
      <!-- 배팅보드 -->
      <PlayBattingVue />
      <!-- 남은 시간, 총 배팅 코인 -->
      <div class="position-absolute top-0 start-50 translate-middle mt-4 d-flex flex-column align-items-center">
        <div class="text-white fs-3 fw-bold">12 초</div>
        <div class="text-white fw-bold">총 배팅: 21</div>
      </div>
    </div>
    <!-- 채팅, 카드, 컨트롤러 -->
    <div class="d-flex align-items-center mb-3" style="width: 100%; height: 30%;">
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
        <!-- <img class="middle-logo mb-5 me-5" src="/src/assets/Logo.png" alt=""> -->
      </div>
      <!-- 나가기 -->
      <div class="position-absolute bottom-0 end-0">
        <div class="bg-danger me-3 text-white fw-bold rounded-pill d-flex justify-content-center align-items-center border border-2 border-white" style="width: 60px; height: 30px">나가기</div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import PlayBattingVue from "../components/Play/PlayBatting.vue";
  import PlayControllerVue from "../components/Play/PlayController.vue";
  import PlayPlayerVue from "../components/Play/PlayPlayer.vue";
  import PlayTalkVue from "../components/Play/PlayTalk.vue";

</script>

<style scoped>
  @import '@/assets/color.css';
  @import "@/assets/size.css";
</style>