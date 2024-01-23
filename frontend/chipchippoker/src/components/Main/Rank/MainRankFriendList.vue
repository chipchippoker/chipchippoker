<template>
    <div class="rank-scroll bg-lightblue tab-radius-2">
        <MainRankItemItem 
        v-for="item in friendStore.friendRankList" 
        :class="{'sticky-top sticky-bottom myrank':item?.nickname==userStore.myNickName}"
        :key="item?.nickname"
        :item="item"/>
        <!-- 내가 rank안에 없을때 추가로 붙음 -->
        <div class="text-center" v-if="!friendStore.isContainedinFriend">
        <div>.</div>
        <div>.</div>
        </div>
        <div class="sticky-bottom" v-if="!friendStore.isContainedinFriend">
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

<style scoped>
.tab-radius-2 {
    border-top-left-radius: 10px;
    border-top-right-radius: 0;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
}
</style>