<template>
  <div>
    <div class="position-absolute top-50 start-50 translate-middle battingstyle p-0" style="width: 35%; height: 75%;">
      <div class="position-relative w-100 h-100 d-flex">
        <!-- 왼쪽 플레이어들 -->
        <div class="w-50 h-100">
          <div v-if="gameStore.memberInfos && gameStore.memberInfos.length > 0"
            class="h-50 d-flex justify-content-evenly align-items-center flex-column">
            <div class="text-white">보유코인
              <div class="d-flex flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[0]?.haveCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(1)" alt="">
              </div>
            </div>
            <div class="text-white fw-bold">배팅 코인:
              <!-- 배팅 코인 이미지 -->
              <div class="d-flex flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[0]?.bettingCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(1)" alt="">
              </div>
            </div>
          </div>
          <div v-if="gameStore.memberInfos && gameStore.memberInfos.length > 2"
            class="h-50 d-flex justify-content-evenly align-items-center flex-column">
            <div class="text-white">보유코인
              <div class="d-flex flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[2]?.haveCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(3)" alt="">
              </div>

            </div>
            <div class="text-white fw-bold">배팅 코인:
              <!-- 배팅 코인 이미지 -->
              <div class="d-flex flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[2]?.bettingCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(3)" alt="">
              </div>
            </div>
          </div>
        </div>
        <!-- 오른쪽 플레이어들 -->
        <div class="w-50 h-100">
          <div v-if="gameStore.memberInfos && gameStore.memberInfos.length > 1"
            class="h-50 d-flex justify-content-evenly align-items-center flex-column">
            <div class="text-white">보유코인
              <div class="d-flex flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[1]?.haveCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(2)" alt="">
              </div>
            </div>
            <div class="text-white fw-bold">배팅 코인:
              <!-- 배팅 코인 이미지 -->
              <div class="d-flex flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[1]?.bettingCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(2)" alt="">
              </div>
            </div>

          </div>
          <div v-if="gameStore.memberInfos && gameStore.memberInfos.length > 3"
            class="h-50 d-flex justify-content-evenly align-items-center flex-column">
            <div class="text-white">보유코인
              <div class="d-flex flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[3]?.haveCoin" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(4)" alt="">
              </div>
            </div>
            <div class="text-white fw-bold">배팅 코인: 
              <!-- 배팅 코인 이미지 -->
              <div class="d-flex flex-wrap" style="width: 100px;">
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