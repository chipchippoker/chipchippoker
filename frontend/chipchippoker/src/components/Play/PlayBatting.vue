<template>
  <!-- 배팅 보드 가운데 위치시키기 -->
  <div class="container position-absolute top-50 start-50 translate-middle battingstyle p-0 z-0" style="width: calc(100% - 620px); height: 90%;">
    <div
      class="row position-relative w-100 h-100 d-flex justify-content-center align-items-center">

      <!-- 위쪽 -->
      <div class="row col-12 justify-content-between">

        <!-- 1번 플레이어 -->
        <div class="row col-6">
          <!-- 카드 -->
          <div class="col-5">
            <img id="card" :src="getCardUrl(gameStore?.gameMemberInfos[0]?.cardInfo?.cardSet, gameStore?.gameMemberInfos[0]?.cardInfo?.cardNumber)" alt="앞장">
          </div>
          <!-- 코인 -->
          <div class="col-6 d-flex flex-column justify-content-between align-items-center">
            <!-- 보유 코인 -->
            <div class="text-white text-center mb-3"> 
              <p class="mb-1">보유코인 : {{ gameStore?.gameMemberInfos[0]?.haveCoin }}</p>
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[0]?.haveCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(1)" alt="">
              </div>
            </div>
            <!-- 배팅 코인 -->
            <div class="text-white text-center fw-bold">
              <p class="mb-1">배팅 코인: {{ gameStore?.gameMemberInfos[0]?.bettingCoin }}</p>
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[0]?.bettingCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(1)" alt="">
              </div>
            </div>
          </div>
          <!-- 공백 -->
          <div class="col-1">

          </div>
        </div>

        <!-- 2번 플레이어 -->
        <div class="row col-6" v-if="gameStore.memberInfos && gameStore.memberInfos.length > 1">
          <!-- 공백 -->
          <div class="col-2">

          </div>
          <!-- 코인 -->
          <div class="col-5 d-flex flex-column justify-content-between align-items-center">
            <!-- 코인 -->
            <div class="text-white text-center">
              <p class="mb-1">보유코인: {{ gameStore?.gameMemberInfos[1]?.haveCoin }}</p>
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[1]?.haveCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(2)" alt="">
              </div>
            </div>
            <!-- 배팅 코인 이미지 -->
            <div class="text-white text-center fw-bold">
              <p class="mb-1">배팅 코인: {{ gameStore?.gameMemberInfos[1]?.bettingCoin }}</p>
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[1]?.bettingCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(2)" alt="">
              </div>
            </div>
          </div>
          <!-- 카드 -->
          <div class="col-5">
            <img id="card" :src="getCardUrl(gameStore?.gameMemberInfos[1]?.cardInfo?.cardSet, gameStore?.gameMemberInfos[1]?.cardInfo?.cardNumber)" alt="앞장">
          </div>
        </div>
      </div>
      
      <!-- 아래쪽 -->
      <div class="row col-12 justify-content-between" v-if="gameStore.memberInfos && gameStore.memberInfos.length > 2">

        <!-- 3번 플레이어 -->
        <div class="row col-5" v-if="gameStore.memberInfos && gameStore.memberInfos.length > 2">
          <!-- 카드 -->
          <div class="col-5">
            <img id="card" :src="getCardUrl(gameStore?.gameMemberInfos[2]?.cardInfo?.cardSet, gameStore?.gameMemberInfos[2]?.cardInfo?.cardNumber)" alt="앞장">

          </div>
          <!-- 코인 -->
          <div class="col-6 d-flex flex-column justify-content-between align-items-center">
            <!-- 배팅 코인-->
            <div class="text-white text-center fw-bold">
              <p class="mb-1">배팅 코인: {{ gameStore?.gameMemberInfos[2]?.bettingCoin }}</p>
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[2]?.bettingCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(3)" alt="">
              </div>
            </div>
            <!-- 보유코인 -->
            <div class="text-white text-center">
              <p class="mb-1">보유코인: {{ gameStore?.gameMemberInfos[2]?.haveCoin }}</p>
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[2]?.haveCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(3)" alt="">
              </div>
            </div>
          </div>
          <!-- 공백 -->
          <div class="col-1">

          </div>
        </div>

        <!-- 4번 플레이어 -->
        <!-- 코인 -->
        <div class="row col-6" v-if="gameStore.memberInfos && gameStore.memberInfos.length > 3">
          <!-- 공백 -->
          <div class="col-2">

          </div>
          <!-- 코인 -->
          <div class="col-5 d-flex flex-column justify-content-between align-items-center">
            <!-- 배팅 코인 -->
            <div class="text-white text-center fw-bold">
              <p class="mb-1"></p>배팅 코인: {{ gameStore?.gameMemberInfos[3]?.bettingCoin }}
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[3]?.bettingCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(4)" alt="">
              </div>
            </div>
            <!-- 보유 코인 -->
            <div class="text-white text-center">
              <p class="mb-1">보유코인: {{ gameStore?.gameMemberInfos[3]?.haveCoin }}</p>
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[3]?.haveCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(4)" alt="">
              </div>
            </div>
          </div>

          <!-- 카드 -->
          <div class="col-5">
            <img id="card" :src="getCardUrl(gameStore?.gameMemberInfos[3]?.cardInfo?.cardSet, gameStore?.gameMemberInfos[3]?.cardInfo?.cardNumber)" alt="앞장">

          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { useGameStore } from '@/stores/game';

const gameStore = useGameStore()
const getCoinUrl = function (num) {
  return new URL(`/src/assets/coins/coin${num}.png`, import.meta.url).href;
};

// 카드 가져오기
const getCardUrl = function (setnum, cardnum) {
  return new URL(`/src/assets/cards/set${setnum}/card${cardnum}.png`, import.meta.url).href;
};
</script>

<style scoped>
.container {
    display: flex;
    justify-content: center; /* 수평 가운데 정렬 */
    align-items: center; /* 수직 가운데 정렬 */
}

#card {
  width: 100px;
}
</style>