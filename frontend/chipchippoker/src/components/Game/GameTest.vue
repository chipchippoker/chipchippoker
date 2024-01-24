<template>
  <div>
    <h1>게임 테스트</h1>
    <input type="text" v-model="gameRoomTitle" placeholder="방 제목">
    <button @click="onCreatRoom">방 생성</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useGameStore } from '@/stores/game'
import { useUserStore } from '@/stores/user';

const gameStore = useGameStore()
const gameRoomTitle = ref('')

const onCreatRoom = () => {
  // 게임방 생성
  // gameStore.connectHandler(gameRoomTitle.value)
  // gameStore.gameRoomTitle = gameRoomTitle.value
  gameStore.createRoom(gameRoomTitle.value, 3)
  gameStore.subscribeHandler(gameRoomTitle.value)

}
</script>

<style scoped>

</style>