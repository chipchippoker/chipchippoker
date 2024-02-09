<template>
  <!-- 나의 정보, 게임 프로필 -->
  <div class="container mainstyle d-flex flex-row">
    <!-- 정보 -->
    <div class="d-flex flex-column w-100">
      <!-- 프사, 닉네임, 티어, 포인트 -->
      <div class="d-flex align-items-end fs-3">
        <!-- 프로필 사진 -->
        <div
          class="d-flex justify-content-center ms-5"
          style="position: relative"
        >
          <!-- 내프로필 -->
          <img
            v-if="userStore?.profileInfo?.isMine"
            class="profile-icon-other"
            :src="userStore.getIconUrl(userStore?.myIcon)"
            :alt="userStore?.myIcon"
          />

          <!-- 다른사람 프로필 -->
          <img
            v-else
            class="profile-icon-other"
            :src="userStore.getIconUrl(userStore?.profileInfo?.icon)"
            :alt="userStore?.profileInfo?.icon"
          />
        </div>

        <!-- 닉네임 티어, 포인트 -->
        <div class="w-75 d-flex justify-content-center">
          <div class="d-flex align-items-center gap-5 fw-bold">
            <div>
              {{ userStore?.profileInfo?.nickname }}
            </div>
            <div style="width: 56px;">
              <img v-if="userStore?.profileInfo?.rank === 1" 
              :src='friendStore.getTierIconUrl("rare")' 
              style="width: 100%; height: 100%; object-fit: cover;">
              <img v-else :src='friendStore.getTierIconUrl(userStore?.profileInfo?.tier)' style="width: 100%; height: 100%; object-fit: cover;">
            </div>
            <div>{{ userStore?.profileInfo?.point }}pt</div>
          </div>
        </div>
      </div>

      <!-- 최근 전적 -->
      <ul
        v-if="userStore?.profileInfo?.recentPlayList?.length > 0"
        class="profile-outline-darkblue my-3 d-flex flex-column-reverse gap-1 overflow-y-auto"
        style="max-height: 300px"
      >
        <!-- 게임 모드, 인원 -->
        <div
          class="bg-lightblue p-1 rounded-3"
          v-for="recentPlay in userStore?.profileInfo?.recentPlayList"
          :key="recentPlay.id"
        >
          <div class="d-flex gap-2 fw-bold">
            <div>{{ recentPlay.gameMode }}</div>
            <div>{{ recentPlay.memberNum }}인</div>
          </div>
          <!-- 같이한 사람 이름 -->
          <div class="d-flex justify-content-between me-2">
            <div class="d-flex gap-3">
              <div
                v-for="(result, player) in recentPlay.opponents"
                :key="player"
              >
                {{ player }}: {{ result }}
              </div>
            </div>
            <!-- 포인트 -->
            <div
              v-if="recentPlay.gameMode === '경쟁'"
              class="fw-bold"
              :class="[
                { 'text-primary': recentPlay?.pointChange > 0 },
                { 'text-danger': recentPlay?.pointChange < 0 },
              ]"
            >
              {{ recentPlay?.pointChange }}pt
            </div>
          </div>
        </div>
      </ul>
      <div
        v-else
        class="profile-outline-darkblue my-3 d-flex flex-column gap-1 overflow-y-auto text-center"
      >
        최근 전적이 없습니다.
      </div>
    </div>
  </div>
</template>

<script setup>
import ModalIconList from "../Modal/ModalIconList.vue";
import { ref, onMounted } from "vue";
import { useUserStore } from "@/stores/user";
import { useFriendStore } from "@/stores/friend";
const profileInfo = ref({});
const friendStore = useFriendStore();
const userStore = useUserStore();

onMounted(() => {
  profileInfo.value = userStore.profileInfo;
});
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
