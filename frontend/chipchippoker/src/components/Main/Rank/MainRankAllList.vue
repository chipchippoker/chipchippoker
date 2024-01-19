<template>
    <div class="rank-scroll tab-radius bg-lightblue">
        <MainRankItemItem 
        v-for="item in friendStore.allRankList" 
        :class="{'myrank':item?.nickname==userStore.myNickName}"
        :key="item?.nickname"
        :item="item"/>
        <MainRankItemItem 
        class="sticky-bottom myrank mb-3"
        :item="friendStore?.myRank"
        />
        
    </div>
</template>

<script setup>
import MainRankItemItem from './MainRankItem.vue';
import { useFriendStore } from '@/stores/friend';
const friendStore = useFriendStore()
import { useUserStore } from '@/stores/user';
const userStore = useUserStore()
</script>

<style  scoped>
.tab-radius {
    border-top-left-radius: 0;
    border-top-right-radius: 10px;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
}
</style>