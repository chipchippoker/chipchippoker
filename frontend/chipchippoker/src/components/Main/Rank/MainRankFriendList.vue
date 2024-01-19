<template>
    <div class="rank-scroll bg-lightblue tab-radius-2">
        <MainRankItemItem
         v-for="item in friendStore.friendRankList" 
         :class="{'myrank sticky-bottom sticky-top':item?.nickname==userStore.myNickName}"
         :key="item?.nickname"
        :item="item"/>
    </div>
</template>

<script setup>
import MainRankItemItem from './MainRankItem.vue';
import { useFriendStore } from '@/stores/friend';
const friendStore = useFriendStore()
import { useUserStore } from '@/stores/user';
const userStore = useUserStore()
</script>

<style scoped>
.tab-radius-2 {
    border-top-left-radius: 10px;
    border-top-right-radius: 0;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
}
</style>