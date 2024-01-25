<template>
    <div class="rank-scroll tab-radius bg-lightblue">
        <MainRankItemItem 
        v-for="item in friendStore?.allRankList" 
        :class="{'sticky-top sticky-bottom myrank':item?.nickname==userStore.myNickName}"
        :key="item?.nickname"
        :item="item"/>
        <!-- 내가 rank안에 없을때 추가로 붙음 -->
        <div class="text-center" v-if="!friendStore?.isContainedinAll">
        <div>.</div>
        <div>.</div>
        </div>
        <div class="sticky-bottom" v-if="!friendStore?.isContainedinAll">
            
            <MainRankItemItem 
            class=" myrank mb-3"
            :item="friendStore?.myRank"
            />
        </div>
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