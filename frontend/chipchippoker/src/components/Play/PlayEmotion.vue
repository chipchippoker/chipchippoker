<template>
    <div class="text-white">
        {{gameStore?.showEmotionNickname}}님의 감정표현
        <div v-for="key in Object.keys(emotion)">
         <div>{{ key }}: {{ emotion[key] }}</div>
        </div>
    </div>
</template>

<script setup>
import { useGameStore } from '@/stores/game';
const gameStore = useGameStore()
const emotion = gameStore?.playerEmotion[gameStore?.showEmotionNickname]
</script>

<style scoped>

</style>