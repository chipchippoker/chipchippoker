<template>
    <div class="border border-5 border-warning bg-lightyellow rounded-4 p-2">
        <div>{{gameStore?.showEmotionNickname}}님의 감정인식 결과</div>
        <div v-for="key in Object.keys(gameStore?.playerEmotion[gameStore?.showEmotionNickname])">
         <div>{{ korean[key] }}: {{ gameStore?.playerEmotion[gameStore?.showEmotionNickname][key]}}</div>
        </div>
    </div>
</template>

<script setup>
import { useGameStore } from '@/stores/game';
const gameStore = useGameStore()
const korean = {
    'angry':'분노',
    'disgusted':'역겨움',
    'fearful':'두려움',
    'happy':'행복',
    'neutral':'평온',
    'sad':'슬픔',
    'surprised':'놀람'
}
</script>

<style scoped>

</style>