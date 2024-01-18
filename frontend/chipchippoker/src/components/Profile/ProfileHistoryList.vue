<template>
    <!-- 나의 정보, 게임 프로필 -->
    <div class="mainstyle d-flex flex-row" >
      <!-- 프로필 사진 -->
      <div class="" style="width: 200px;">
        프사
        <img class="small-icon" :src='userStore.getIconUrl(profileInfo.icon)' :alt="profileInfo.icon">
      </div>
      <!-- 정보 -->
      <div class="d-flex flex-column me-3 w-100">
        <!-- 닉네임, 티어, 포인트 -->
        <div class="d-flex justify-content-around">
          <div>{{ profileInfo.nickname }}닉네임</div>
          <div>{{ profileInfo.tier }}티어</div>
          <div>{{ profileInfo.point }}포인트</div>
        </div>
        <!-- 최근 전적 -->
        <ul class="profile-outline-darkblue my-3">
          <li class="">최근 전적</li>
          <div v-for="recentPlay in profileInfo.recentPlayList" :key="recentPlay.id">
            <div class="d-flex">
              <div>{{ recentPlay.gameMode }}</div>
              <div>{{ recentPlay.memberNum }}인</div>
            </div>
            <div class="d-flex">
              <div v-for="player in recentPlay.opponents" :key="Object.keys(player)[0]">
                <p>{{ Object.keys(player)[0] }}: </p>
                <p>{{ Object.values(player)[0] }}</p>
              </div>
              <div>{{ pointChange }}</div>
            </div>
          </div>
        </ul>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from "vue";
  import { useUserStore } from '@/stores/user'
  
  const profileInfo = ref({})  
  const userStore = useUserStore()

  onMounted(() => {
    // profileInfo.value = userStore.profileInfo
  })
  </script>
  
  <style scoped>
    @import "@/assets/color.css";
    @import "@/assets/size.css";
  </style>