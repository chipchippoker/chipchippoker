<template>
    <div class="watcher-list bg-lightblue h-100 p-2">
        <p>관전</p>
        <ul></ul>
    </div>
</template>

<script setup>
import { useRoomStore } from '@/stores/room';

const roomStore = useRoomStore()
roomStore.watchersNickname

</script>

<style scoped>
.watcher-list {
    border-radius: 10px;
}

p {
    color: white;
}
</style>