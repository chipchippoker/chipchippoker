<template>
    <div class="watcher-list bg-lightblue h-100 p-2">
        <p>관전</p>
        <ul>
            <li v-for="watcher in watchersNickname" :key="watcher">{{ watcher }}</li>
        </ul>
    </div>
</template>

<script setup>
import { useRoomStore } from '@/stores/room';
import { computed } from 'vue';


const roomStore = useRoomStore()
const watchersNickname = computed(() => roomStore.watchersNickname)

</script>

<style scoped>
.watcher-list {
    border-radius: 10px;
}

p {
    color: white;
}
</style>