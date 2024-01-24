<template>
    <!-- 각각의 사용자 -->
    <div 
    @mouseover="soundStore.hoverSound()"
     type="button" class="d-flex m-2 rounded-2 p-1 overflow-hidden" 
    :class="[{'myrank':item?.nickname===userStore.myNickName},{'otherrank':item?.nickname!=userStore.myNickName}]"
    @click="gotoProfile(item?.nickname)">
        <div class="d-flex gap-1 align-items-center">
            <div class="text-overflow" style="width:35px;">{{ item?.rank }}등</div>
            <img class="x-small-icon"  :src='userStore.getIconUrl(item?.icon)'>
            <div class="text-overflow" style="width:100px;">{{ item?.nickname }}</div>
            <img class="xx-small-icon"  :src='friendStore.getTierIconUrl(item?.tier)'>
            <div  class="text-overflow text-end" style="width:50px;">{{ item?.point }}</div>
            <div class="me-2">pt</div>
            
        </div>
    </div>

    
</template>

<script setup>

import { useUserStore } from '@/stores/user';
import { useFriendStore } from '@/stores/friend';
import { useRouter } from 'vue-router';
const userStore = useUserStore()
const friendStore = useFriendStore()
import { useSoundStore } from '@/stores/sound';
const soundStore = useSoundStore()
const router = useRouter()
defineProps({item:Object})

const gotoProfile = function(nickName){
    console.log(`${nickName} 페이지로 이동`)
    
    // router.push({name:'profile',params:`${nickName}`})
}

</script>

<style scoped>

</style>