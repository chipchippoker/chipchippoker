<template>
    <!-- 나의 정보, 게임 프로필 -->
    <div class="mainstyle d-flex flex-row" >
      

      <!-- 정보 -->
      <div class="d-flex flex-column w-100">
        <!-- 프사, 닉네임, 티어, 포인트 -->
        <div class="d-flex align-items-end fs-3">
          <!-- 프로필 사진 -->
          <div
          class="d-flex justify-content-center mx-5 fade-in"
          style="position: relative;">
            <!-- 내프로필 -->
            <img v-if="userStore?.profileInfo?.isMine" 
            class="profile-icon-mine" 
            type="button"
            data-bs-toggle="modal" data-bs-target="#IconModal" 
            :src='userStore.getIconUrl(userStore?.profileInfo?.icon)' :alt="userStore?.profileInfo?.icon">

            <font-awesome-icon
            v-if="userStore?.profileInfo?.isMine"
            class="xx-small-icon"
            style="position: absolute; top: 35px; color: #ffffff;"
            type="button"
            data-bs-toggle="modal" data-bs-target="#IconModal" 
            :icon="['fas', 'arrows-rotate']"
            />
            <!-- 다른사람 프로필 -->
            <img v-else class="profile-icon-other" :src='userStore.getIconUrl(userStore?.profileInfo?.icon)' :alt="userStore?.profileInfo?.icon">
            
          </div>

          <!-- 닉네임 티어, 포인트 -->
          <div class="d-flex align-items-end gap-5 fw-bold">
            <div>
              {{ userStore?.profileInfo?.nickname }}
            </div>
            <div style="width: 56px; height: 50px;">
              <img class=""  :src='friendStore.getTierIconUrl(userStore?.profileInfo?.tier)' style="width: 100%; height: 100%; object-fit: cover;">
            </div>
            <div>
              {{ userStore?.profileInfo?.point }}pt
            </div>
          </div>
        </div>
        <!-- 최근 전적 -->
          <ul class="profile-outline-darkblue my-3 d-flex flex-column gap-1 overflow-y-auto" style="max-height: 300px;">
            <div 
              class="bg-lightblue p-1 rounded-3" 
              v-for="recentPlay in userStore?.profileInfo?.recentPlayList"
              :key="recentPlay.id">
              <div class="d-flex gap-2 fw-bold">
                <div>{{ recentPlay.gameMode }}</div>
                <div>{{ recentPlay.memberNum }}인</div>
              </div>
              <div class="d-flex gap-3">
                <div v-for="(result, player) in recentPlay.opponents" :key="player">
                    {{ player }}: {{ result }}
                </div>
                <div
                class="fw-bold"
                :class="[{'text-primary':recentPlay?.pointChange>0},
                {'text-danger':recentPlay?.pointChange<0}]"
                >{{ recentPlay?.pointChange  }}pt
              </div>
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
    profileInfo.value = userStore.profileInfo
  })
  </script>
  
<style scoped>


  ::-webkit-scrollbar {
    width: 10px; /* 스크롤바 너비 */
  }

  ::-webkit-scrollbar-thumb {
    background-color: rgba(0, 0, 0, 0.2); /* 스크롤바 썸의 배경색 */
    border-radius: 5px; /* 스크롤바 썸의 모서리 둥글게 */
  }

  ::-webkit-scrollbar-track {
    background-color: transparent; /* 스크롤바 트랙의 배경색 투명하게 */
  }
</style>