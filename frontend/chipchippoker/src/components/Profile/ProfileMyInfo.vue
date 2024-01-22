<template>
    <!-- 나의 정보, 게임 프로필 -->
    <div class="container mainstyle d-flex flex-row profile-box-radius" >
      <!-- 프로필 사진 -->
      <div class="" style="position: relative; width: 200px;">
        <img v-if="userStore?.profileInfo?.isMine" data-bs-toggle="modal" data-bs-target="#IconModal"  class="small-icon" :src='userStore.getIconUrl(userStore?.profileInfo?.icon)' :alt="userStore?.profileInfo?.icon">
        <img v-else class="small-icon-others" :src='userStore.getIconUrl(userStore?.profileInfo?.icon)' :alt="userStore?.profileInfo?.icon">
        <font-awesome-icon
        v-if="userStore?.profileInfo?.isMine"
        data-bs-toggle="modal" data-bs-target="#IconModal" 
        class="xx-small-icon"
        :icon="['fas', 'arrows-rotate']"
        beat-fade
        style="position: absolute; bottom: 15px; right: 50px; color: #ffffff;"
        />
      </div>

      <!-- 정보 -->
      <div class="d-flex flex-column me-3 w-100">
        <!-- 닉네임, 티어, 포인트 -->
        <div class="d-flex justify-content-between fs-3">
          <strong>{{ userStore?.profileInfo?.nickname }}</strong>
          <div style="width: 56px; height: 50px;">
            <img class=""  :src='friendStore.getTierIconUrl(userStore?.profileInfo?.tier)' style="width: 100%; height: 100%; object-fit: cover;">
          </div>
          <strong>{{ userStore?.profileInfo?.point }}pt</strong>
        </div>
        
        <!-- 상세 정보 -->
        <div class="row profile-outline-darkblue my-3">
          <!-- 왼쪽 -->
          <div class="col-6 row p-3">
            <!-- 랭킹 친구 -->
            <div class="col-6 mb-3">
              <strong>랭킹(친구)</strong>
            </div>
            <div class="col-6 mb-3">3위</div>
            <!-- 랭킨(전체) -->
            <div class="col-6 mb-3">
              <strong>랭킹(전체)</strong>
            </div>
            <div class="col-6 mb-3">{{ userStore?.profileInfo?.rank }}위</div>
            <!-- 게임 수 -->
            <div class="col-6 mb-3">
              <strong>게임 수</strong>
            </div>
            <div class="col-6 mb-3">{{ userStore?.profileInfo?.total }}전</div>
            <!-- 승패 -->
            <div class="col-6">
              <strong>승패</strong>
            </div>
            <div class="col-6" style="width: 40%;">{{ userStore?.profileInfo?.win }}승 {{ userStore?.profileInfo?.draw }}무 {{ userStore?.profileInfo?.lose }}패</div>
          </div>

          <!-- 오른쪽 -->
          <div class="col-6 row">
            <!-- 승률 -->
            <div class="col-6 my-3">
              <strong>승률</strong>
            </div>
            <div class="col-6 my-3">{{ userStore?.profileInfo?.winningRate }}74.1%</div>
            <!-- 포인트 -->
            <div class="col-6 mb-3">
              <strong>포인트</strong>
            </div>
            <div class="col-6 mb-3">{{ userStore?.profileInfo?.point }}pt</div>
            <!-- 최대 연승 -->
            <div class="col-6">
              <strong>최대연승</strong>
            </div>
            <div class="col-6 mb-3">{{ userStore?.profileInfo?.maxWin }}연승</div>
          </div>
        </div>

        <!-- 나의 프로필 -->
        <div v-if="userStore?.profileInfo?.isMine" class="d-flex flex-row-reverse">
          <button class="btn btn-signout" data-bs-toggle="modal" data-bs-target="#SignOutModal">회원탈퇴</button>
          <button class="btn btn-logout" data-bs-toggle="modal" data-bs-target="#LogOutModal">로그아웃</button>
          <button class="btn btn-kakao">
            <img class="kakao-logo" @click="userStore.kakaoConnect" src="/src/assets/icons/kakaologo.png" alt="">
            카카오 연동하기
          </button>
        </div>
        <!-- 친구의 프로필 -->
        <div v-else class="d-flex flex-row-reverse">
          <button class="btn btn-signout">
            <a href="https://docs.google.com/forms/d/e/1FAIpQLSdQLmWHJoz263PcrL3G_SLOzQUY28fVmG2wXJUtObYnEK-_WQ/viewform?usp=sf_link">신고하기</a>
          </button>
          <button @click="friendRequest" class="btn btn-signout primary">친구 신청</button>
        </div>
      </div>
             
      <!-- 아이콘 모달 팝업 -->
      <div class="modal fade" id="IconModal" tabindex="-1" aria-labelledby="IconModalLabel" aria-hidden="true">
        <ModalIconList/>
      </div>

      <!-- 로그아웃 모달 팝업 -->
      <div class="modal fade" id="LogOutModal" tabindex="-1" aria-labelledby="LogOutLabel" aria-hidden="true">
        <ModalLogOut />
      </div>
      <!-- 로그아웃 모달 팝업 -->
      <div class="modal fade" id="SignOutModal" tabindex="-1" aria-labelledby="SignOutLabel" aria-hidden="true">
        <ModalSignOut />
      </div>
    </div>
</template>
  
<script setup>
  import ModalIconList from "../Modal/ModalIconList.vue";
  import ModalLogOut from "../Modal/ModalLogOut.vue";
  import ModalSignOut from "../Modal/ModalSignOut.vue";

  import { ref, onMounted } from "vue";
  import { useUserStore } from '@/stores/user'
  import { useFriendStore } from "@/stores/friend";
  const userStore = useUserStore()
  const friendStore = useFriendStore()
  // const profileInfo = ref({})
  const totalGame = ref(null)

  // 친구 요청
  const friendRequest = function(){
    console.log('친구신청 걸었음!!!')
    // friendStore.friendRequest(userStore?.profileInfo?.nickname)
  }
  onMounted(() => {
    // profileInfo.value = userStore.profileInfo
    // totalGame.value = profileInfo.win + profileInfo.draw + profileInfo.lose
  })
</script>
  
<style scoped>
  @import "@/assets/color.css";
  @import "@/assets/size.css";

  .kakao-logo {
    width: 15px;
    height: 15px;
    display: inline;
    margin: 0px;
  }
  .btn-kakao {
        width: 100px;
        font-size: 7pt;
        font-weight: bold;
        background-color: #F7E409;
        color: black;
        border: 0;
        padding: 4px 0px;
        margin: 2px;
    }

  .btn-logout {
    width: 60px;
    font-size: 7pt;
    background-color: #576FAC;
    border: solid 1px salmon;
    color: salmon;
    font-weight: bold;
    padding: 4px 0px;
    margin: 2px;
  }

  .btn-signout {
    width: 60px;
    font-size: 7pt;
    font-weight: bold;
    border: solid 1px rgb(63, 63, 63);
    color: rgb(60, 60, 60);
    padding: 4px 0px;
    margin: 2px;
  }
  .detail-info {
    border: 1px solid black;
  }
</style>