<template>
    <div class="bg-lightblue row m-0 tab-radius-2">
        
        <div class="col-6" v-for="item in items" :key="item.id">
            <MainRoomItem/>
        </div>
        
    </div>
</template>

<script setup>
import MainRoomItem from './MainRoomItem.vue';
const items = [1,2,3,4,5,6,7,8,9,10];

</script>

<style scoped>
.tab-radius-2 {
    border-top-left-radius: 10px;
    border-top-right-radius: 0;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
}
</style>