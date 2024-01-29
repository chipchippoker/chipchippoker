<template>
    <div class="bg-lightblue row m-0 tab-radius-2 py-2" style="height: 350px; width: 100%;">
        <div class="col-6 h-25" v-for="item in roomStore.allRoomList" :key="item.id">
            <MainRoomItem
            :item="item"/>
        </div>
        <div class="col-6 h-25" v-for="item in roomStore.emptyList" :key="item.id">
            
        </div>
        
    </div>
</template>

<script setup>
import { useRoomStore } from '@/stores/room';
import MainRoomItem from './MainRoomItem.vue';
import { ref } from 'vue';
const roomStore = useRoomStore()


</script>

<style scoped>
.tab-radius-2 {
    border-top-left-radius: 10px;
    border-top-right-radius: 0;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
}
</style>