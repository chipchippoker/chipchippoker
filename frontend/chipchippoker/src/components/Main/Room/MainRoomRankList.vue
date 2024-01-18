<template>
    <div class="bg-lightblue row m-0 tab-radius py-2">
        <div class="col-6" v-for="item in roomStore.allRoomList" :key="item.id">
            <MainRoomItem
            :item="item"/>
        </div>
        
    </div>
</template>

<script setup>
import { useRoomStore } from '@/stores/room';
import MainRoomItem from './MainRoomItem.vue';
const roomStore = useRoomStore()

</script>

<style scoped>
.tab-radius {
    border-top-left-radius: 0;
    border-top-right-radius: 10px;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
}
</style>