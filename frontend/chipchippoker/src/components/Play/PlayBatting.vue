<template>
  <div >
    <div class="position-absolute top-50 start-50 translate-middle battingstyle p-0 z-0" style="width: calc(100% - 620px); height: 90%;">
      <div class="position-relative w-100 h-100 d-flex">
        <!-- 왼쪽 플레이어들 -->
        <div class="w-50 h-100 d-flex flex-row-reverse pe-5">
          <div v-if="gameStore.memberInfos && gameStore.memberInfos.length > 0"
            class="h-50 d-flex justify-content-evenly align-items-center flex-column">
            <div class="text-white text-center">보유코인 : {{ gameStore?.gameMemberInfos[0]?.haveCoin }}
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[0]?.haveCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(1)" alt="">
              </div>
            </div>
            <div class="text-white text-center fw-bold">배팅 코인: {{ gameStore?.gameMemberInfos[0]?.bettingCoin }}
              <!-- 배팅 코인 이미지 -->
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[0]?.bettingCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(1)" alt="">
              </div>
            </div>
          </div>
          <div v-if="gameStore.memberInfos && gameStore.memberInfos.length > 2"
            class="h-50 d-flex justify-content-evenly align-items-center flex-column">
            <div class="text-white text-center">보유코인: {{ gameStore?.gameMemberInfos[2]?.haveCoin }}
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[2]?.haveCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(3)" alt="">
              </div>

            </div>
            <div class="text-white text-center fw-bold">배팅 코인: {{ gameStore?.gameMemberInfos[2]?.bettingCoin }}
              <!-- 배팅 코인 이미지 -->
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[2]?.bettingCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(3)" alt="">
              </div>
            </div>
          </div>
        </div>
        <!-- 오른쪽 플레이어들 -->
        <div class="w-50 h-100 d-flex ps-5">
          <div v-if="gameStore.memberInfos && gameStore.memberInfos.length > 1"
            class="h-50 d-flex justify-content-evenly align-items-center flex-column">
            <div class="text-white text-center">보유코인: {{ gameStore?.gameMemberInfos[1]?.haveCoin }}
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[1]?.haveCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(2)" alt="">
              </div>
            </div>
            <div class="text-white text-center fw-bold">배팅 코인: {{ gameStore?.gameMemberInfos[1]?.bettingCoin }}
              <!-- 배팅 코인 이미지 -->
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[1]?.bettingCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(2)" alt="">
              </div>
            </div>
          </div>
          <div v-if="gameStore.memberInfos && gameStore.memberInfos.length > 3"
            class="h-50 d-flex justify-content-evenly align-items-center flex-column">
            <div class="text-white text-center">보유코인: {{ gameStore?.gameMemberInfos[3]?.haveCoin }}
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[3]?.haveCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(4)" alt="">
              </div>
            </div>
            <div class="text-white text-center fw-bold">배팅 코인: {{ gameStore?.gameMemberInfos[3]?.bettingCoin }}
              <!-- 배팅 코인 이미지 -->
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[3]?.bettingCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(4)" alt="">
              </div>
            </div>
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
</script>

<style scoped></style>