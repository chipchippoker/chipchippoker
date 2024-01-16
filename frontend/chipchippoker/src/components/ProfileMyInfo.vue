<template>
    <!-- 나의 정보, 게임 프로필 -->
    <div class="mainstyle d-flex flex-row  profile-box-radius" >
      <!-- 프로필 사진 -->
      <div class="" style="width: 200px;">
        프사
      </div>
      <!-- 정보 -->
      <div class="d-flex flex-column me-3 w-100">
        <!-- 닉네임, 티어, 포인트 -->
        <div class="d-flex justify-content-around">
          <div>닉네임</div>
          <div>티어</div>
          <div>포인트</div>
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
              <div class="">74.1%</div>
              <div></div>
            </div>
          </div>
          <!-- 랭킹(전체) 포인트 -->
          <div class="d-flex w-100 my-3">
            <div class="d-flex w-50">
              <div class="w-50">랭킹(전체)</div>
              <div class="">257위</div>
              <div></div>
            </div>
            <div class="d-flex w-50">
              <div class="w-50">포인트</div>
              <div class="">1200pt</div>
              <div></div>
            </div>
          </div>
          <!-- 게임 수 최대 연승 -->
          <div class="d-flex w-100 my-3">
            <div class="d-flex w-50">
              <div class="w-50">게임 수</div>
              <div class="">520전</div>
              <div></div>
            </div>
            <div class="d-flex w-50">
              <div class="w-50">최대 연승</div>
              <div class="">4연승</div>
              <div></div>
            </div>
          </div>
          <!-- 승패 -->
            <div class="d-flex mt-3">
              <div class="w-25">승패</div>
              <div class="">329승 171패 20무</div>
              <div></div>
          </div>
        </div>
  
        <!-- 버튼들 -->
        <div class="d-flex flex-row-reverse">
          <button class="btn btn-signout">회원탈퇴</button>
          <button class="btn btn-logout">로그아웃</button>
          <button class="btn btn-kakao">
            <img class="kakao-logo" src="/src/assets/icons/kakaologo.png" alt="">
            카카오 연동하기
          </button>
        </div>
      </div>
    </div>
  </template>
   
  <script setup>
    import { ref, onMounted } from "vue";
    import { useUserStore } from '@/stores/user'
    import { useRouter } from 'vue-router'
  
    const userStore = useUserStore()
  
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