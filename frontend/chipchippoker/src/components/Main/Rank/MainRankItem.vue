<template>
    <!-- 각각의 사용자 -->
    <div 
     type="button" class="d-flex m-2 rounded-2 p-1 overflow-hidden" 
    :class="[{'sticky-bottom sticky-top myrank':item?.nickname===userStore.myNickname},{'otherrank':item?.nickname!=userStore.myNickname},
{'rainbow-box':item?.rank ===1}, {'first-place':item?.rank===1}]"
    @click="goProfile(item?.nickname)">


        <div class="d-flex gap-1 align-items-center">
            <div class="text-overflow" style="width:35px;">{{ item?.rank }}등</div>
            <!-- 1등은 레어 아이콘 -->
            <img class="x-small-icon"  :src='userStore.getIconUrl(item?.icon)'>
            <div class="text-overflow" style="width:100px;">{{ item?.nickname }}</div>
            <img v-if="item?.rank === 1" class="xx-small-icon"  :src='friendStore.getTierIconUrl("rare")'>
            <img v-else class="xx-small-icon"  :src='friendStore.getTierIconUrl(item?.tier)'>
            
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

const goProfile = function(nickname){
    console.log(`${nickname} 페이지로 이동`)
    userStore.profileNickname = nickname
    router.push({name:'profile'})
}

</script>

<style scoped>
@keyframes glowing {
  0% {
    border-color: #FF5733; /* 불꽃 효과의 시작 색상 */
    box-shadow: 0 0 10px #FF5733; /* 불꽃 효과의 시작 그림자 */
  }
  50% {
    border-color: #FFC300; /* 불꽃 효과의 중간 색상 */
    box-shadow: 0 0 20px #FFC300; /* 불꽃 효과의 중간 그림자 */
  }
  100% {
    border-color: #FF5733; /* 불꽃 효과의 끝 색상 */
    box-shadow: 0 0 10px #FF5733; /* 불꽃 효과의 끝 그림자 */
  }
}

.first-place {
  animation: glowing 2s infinite alternate; /* 애니메이션 적용: 2초 간격으로 불꽃이 번갈아가며 반복됨 */
}

</style>