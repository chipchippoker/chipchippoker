<template>
  <div>
    <div class="position-absolute top-50 start-50 translate-middle battingstyle p-0" style="width: 35%; height: 75%;">
      <div class="position-relative w-100 h-100 d-flex">
        <!-- 왼쪽 플레이어들 -->
        <div class="w-50 h-100">
          <div v-if="gameStore.memberInfos.length >0" class="h-50 d-flex justify-content-evenly align-items-center flex-column">
            <div class="text-white">코인들</div>
            <div class="text-white fw-bold">배팅 코인: {{gameStore?.gameMemberInfos[0]?.bettingCoin}}</div>
          </div>
          <div v-if="gameStore.memberInfos.length >1" class="h-50 d-flex justify-content-evenly align-items-center flex-column">
            <div class="text-white">코인들</div>
            <div class="text-white fw-bold">배팅 코인: {{gameStore?.gameMemberInfos[2]?.bettingCoin}}</div>
          </div>
        </div>
        <!-- 오른쪽 플레이어들 -->
        <div class="w-50 h-100">
          <div v-if="gameStore.memberInfos.length >2" class="h-50 d-flex justify-content-evenly align-items-center flex-column">
            <div class="text-white">코인들</div>
            <div class="text-white fw-bold">배팅 코인: {{gameStore?.gameMemberInfos[1]?.bettingCoin}}</div>
          </div>
          <div v-if="gameStore.memberInfos.length >3" class="h-50 d-flex justify-content-evenly align-items-center flex-column">
            <div class="text-white">코인들</div>
            <div class="text-white fw-bold">배팅 코인: {{gameStore?.gameMemberInfos[3]?.bettingCoin}}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useGameStore } from '@/stores/game';
import { ref, onMounted, computed, } from 'vue';

const gameStore = useGameStore()

</script>

<style scoped></style>