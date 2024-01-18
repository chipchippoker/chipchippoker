<template>
    <!-- 각각의 사용자 -->
    <div class="d-flex  my-2 bg-light rounded-2 p-1 overflow-hidden">
        <div class="d-flex gap-1 align-items-center">
            <div class="text-overflow" style="width:35px;">{{ item?.rank }}등</div>
            <img class="x-small-icon"  :src='userStore.getIconUrl(item?.icon)'>
        
            <div class="text-overflow" style="width:30px;">{{ item?.tier }}</div>
            <div class="text-overflow" style="width:100px;">{{ item?.nickname }}</div>
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
defineProps({item:Object})
</script>

<style scoped>

</style>