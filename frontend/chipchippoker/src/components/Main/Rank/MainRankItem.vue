<template>
    <!-- 각각의 사용자 -->
    <div class="d-flex  m-2 bg-light rounded-2 p-1 overflow-hidden" 
    :class="{'myrank':item?.nickname===userStore.myNickName}">
        <div class="d-flex gap-1 align-items-center">
            <div class="text-overflow" style="width:35px;">{{ item?.rank }}등</div>
            <img class="x-small-icon rounded-4"  :src='userStore.getIconUrl(item?.icon)'>
            <div class="text-overflow" style="width:100px;">{{ item?.nickname }}</div>
            <img class="xx-small-icon "  :src='friendStore.getTierIconUrl(item?.tier)'>
            <div  class="text-overflow text-end" style="width:50px;">{{ item?.point }}</div>
            <div class="me-2">pt</div>
            
        </div>
    </div>

    
</template>

<script setup>
// {
    // "rank": 1,
    //   "tier": "dia",
    //   "icon": 19,
    //   "nickname": "10기_임세환",
    //   "point": 425637
    // }
import { useUserStore } from '@/stores/user';
const userStore = useUserStore()
import { useFriendStore } from '@/stores/friend';
const friendStore = useFriendStore()
defineProps({item:Object})
</script>

<style scoped>

</style>