<template>
    <div class="container m-0 p-0">
        <div class="bg-lightblue row m-0 tab-radius py-2 ps-0 pe-2" style="height: 350px; width: 100%;">
            <div class="col-6 h-25 pe-1" v-for="item in roomStore.allRoomList" :key="item.id">
                <MainRoomItem
                :item="item"/>
            </div>
            <div class="col-6 h-25 pe-1" v-for="item in roomStore.emptyList" :key="item.id">
                
            </div>
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
.tab-radius {
    border-top-left-radius: 0px;
    border-top-right-radius: 10px;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
}
</style>