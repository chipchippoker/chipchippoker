<template>
    <div class="watcher-list bg-lightblue h-100 p-2">
        <p>관전자</p>
        <ul class="not-overflow-text">
            <li v-for="watcher in watchersNickname" :key="watcher">{{ watcher }}</li>
        </ul>
    </div>
</template>

<script setup>
import { useRoomStore } from '@/stores/room';
import { computed } from 'vue';
import { useGameStore } from '../../stores/game';


const roomStore = useRoomStore()
const gameStore = useGameStore()
const watchersNickname = computed(() => gameStore.watchersNickname)
console.log(watchersNickname.value);

</script>

<style scoped>
.watcher-list {
    border-radius: 10px;
}

p {
    color: white;
}

.not-overflow-text {
  overflow: hidden; /* 텍스트 오버플로우를 숨김 */
  text-overflow: ellipsis; /* 텍스트 오버플로우시 말줄임(...) 표시 */
  white-space: nowrap; /* 텍스트가 넘치면 줄바꿈을 하지 않음 */
}
</style>