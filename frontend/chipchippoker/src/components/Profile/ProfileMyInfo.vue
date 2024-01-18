<template>
    <!-- 나의 정보, 게임 프로필 -->
    <div class="mainstyle d-flex flex-row  profile-box-radius" >
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
          <div>{{ userStore?.profileInfo?.tier }}</div>
          <div>{{ userStore?.profileInfo?.point }}pt</div>
        </div>
  
        <!-- 상세 정보 -->
        <div class="d-flex flex-column profile-outline-darkblue my-3">
          <!-- 랭킹(친구) 승률 -->
          <div class="d-flex w-100 mb-3">
            <div class="d-flex w-50">
              <div class="w-50">랭킹(친구)</div>
              <div class="">3위</div>
              <div></div>
            </div>
            <div class="d-flex w-50">
              <div class="w-50">승률</div>
              <div class="">{{ userStore?.profileInfo?.winningRate }}74.1%</div>
              <div></div>
            </div>
          </div>
          <!-- 랭킹(전체) 포인트 -->
          <div class="d-flex w-100 my-3">
            <div class="d-flex w-50">
              <div class="w-50">랭킹(전체)</div>
              <div class="">{{ userStore?.profileInfo?.rank }}위</div>
              <div></div>
            </div>
            <div class="d-flex w-50">
              <div class="w-50">포인트</div>
              <div class="">{{ userStore?.profileInfo?.point }}pt</div>
              <div></div>
            </div>
          </div>
          <!-- 게임 수 최대 연승 -->
          <div class="d-flex w-100 my-3">
            <div class="d-flex w-50">
              <div class="w-50">게임 수</div>
              <div class="">{{ userStore?.profileInfo?.total }}전</div>
              <div></div>
            </div>
            <div class="d-flex w-50">
              <div class="w-50">최대 연승</div>
              <div class="">{{ userStore?.profileInfo?.maxWin }}연승</div>
              <div></div>
            </div>
          </div>
          <!-- 승패 -->
            <div class="d-flex mt-3">
              <div class="w-25">승패</div>
              <div class="">{{ userStore?.profileInfo?.win }}승 {{ userStore?.profileInfo?.draw }}무 {{ userStore?.profileInfo?.lose }}패</div>
              <div></div>
          </div>
        </div>
  
        <!-- 나의 프로필 -->
        <div v-if="userStore?.profileInfo?.isMine" class="d-flex flex-row-reverse">
          <button class="btn btn-signout" @click="userStore.signOut">회원탈퇴</button>
          <button class="btn btn-logout" @click="userStore.logOut">로그아웃</button>
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
    </div>
  </template>
   
  <script setup>
  import ModalIconList from "../Modal/ModalIconList.vue";
    import { ref, onMounted } from "vue";
    import { useUserStore } from '@/stores/user'
    import { useFriendStore } from "@/stores/friend";
    const userStore = useUserStore()
    const friendStore = useFriendStore()
    const profileInfo = ref({})
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