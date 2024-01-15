<template>
    <div class="border" style="width: 80%;">   
        <h3 class="text-white">Main</h3>
        <div class="d-flex flex-row">
            
            <MainRank class="mainstyle m-1"/>
            <MainRoom class="flex-grow-1 mainstyle m-1"/>
        </div>
    </div>
    <!-- <MainFriendList/> -->
</template>

<script setup>
import MainFriendList from '@/components/Main/MainFriendList.vue';
import MainRank from '@/components/Main/MainRank.vue';
import MainRoom from '@/components/Main/MainRoom.vue';
</script>

<style scoped>
@import "@/assets/color.css";
@import "@/assets/size.css";
</style>