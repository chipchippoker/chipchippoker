<template>
    <!-- 나의 정보, 게임 프로필 -->
    <div class="mainstyle d-flex flex-row" >
      <!-- 프로필 사진 -->
      <div class="" style="width: 200px;">
        <img v-if="userStore?.profileInfo?.isMine" data-bs-toggle="modal" data-bs-target="#IconModal"  class="small-icon" :src='userStore.getIconUrl(userStore?.profileInfo?.icon)' :alt="userStore?.profileInfo?.icon">
        <img v-else class="small-icon" :src='userStore.getIconUrl(userStore?.profileInfo?.icon)' :alt="userStore?.profileInfo?.icon">
      </div>
      <!-- 정보 -->
      <div class="d-flex flex-column me-3 w-100">
        <!-- 닉네임, 티어, 포인트 -->
        <div class="d-flex justify-content-around">
          <div>{{ userStore?.profileInfo?.nickname }}</div>
          <div><img class="x-small-icon"  :src='friendStore.getTierIconUrl(userStore?.profileInfo?.tier)'></div>
          <div>{{ userStore?.profileInfo?.point }}</div>
        </div>
        <!-- 최근 전적 -->
        <ul class="profile-outline-darkblue my-3 d-flex flex-column gap-1" >
          
          <div class="bg-lightblue p-1 rounded-3 " v-for="recentPlay in userStore?.profileInfo?.recentPlayList" :key="recentPlay.id">
            <div class="d-flex gap-2 fw-bold">
              <div>{{ recentPlay.gameMode }}</div>
              <div>{{ recentPlay.memberNum }}인</div>
            </div>
            <div class="d-flex gap-3">
              <div v-for="player in recentPlay.opponents" :key="Object.keys(player)[0]">
                <div>{{ Object.keys(player)[0] }}: {{ Object.values(player)[0] }}</div>
              </div>
              <div>{{ recentPlay?.PointChange }}pt</div>
            </div>
          </div>
        </ul>
      </div>
         <!-- 아이콘 모달 팝업 -->
    <div class="modal fade" id="IconModal" tabindex="-1" aria-labelledby="IconModalLabel" aria-hidden="true">
      <ModalIconList/>
    </div>
    </div>
  </template>
  
  <script setup>
  import ModalIconList from "../Modal/ModalIconList.vue";
  import { ref, onMounted } from "vue";
  import { useUserStore } from '@/stores/user'
  import { useFriendStore } from "@/stores/friend";
  const profileInfo = ref({})  
  const friendStore = useFriendStore()
  const userStore = useUserStore()

  onMounted(() => {
    // profileInfo.value = userStore.profileInfo
  })
  </script>
  
  <style scoped>
    @import "@/assets/color.css";
    @import "@/assets/size.css";
  </style>