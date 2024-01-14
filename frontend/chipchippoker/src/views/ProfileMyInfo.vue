<template>
  <!-- 나의 정보, 게임 프로필 -->
  <div class="row" style="background-color: #576FAC;">
    <!-- 프로필 사진 -->
    <div class="col-2">
      프사
    </div>
    <!-- 정보 -->
    <div class="col-9">
      <!-- 닉네임, 티어, 포인트 -->
      <div>
        <div></div>
        <div></div>
        <div></div>
      </div>

      <!-- 상세 정보 -->
      <div>
        <!-- 왼쪽 / 랭킹(친구), 랭킹(전체), 게임 수, 승패 -->
        <div></div>
        <!-- 오른쪽 / 승률, 포인트, 최대 연승 -->
        <div></div>
      </div>

      <!-- 버튼들 -->
      <div>
        <button class="btn btn-kakao">
          <img class="kakao-logo" src="/src/assets/icons/kakaologo.png" alt="">
          카카오 연동하기
        </button>
        <button class="btn btn-logout">로그아웃</button>
        <button class="btn btn-signout">회원탈퇴</button>
      </div>
    </div>
    <!-- 탭 -->
    <div class="col-1">
      <p>나의 정보</p>
    </div>
  </div>
</template>
 
<script setup>
 
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
        font-size: 5pt;
        font-weight: bold;
        background-color: #F7E409;
        color: black;
        border: 0;
        padding: 4px 0px;
    }

  .btn-logout {
    width: 60px;
    font-size: 5pt;
    background-color: #576FAC;
    border: solid 1px salmon;
    color: salmon;
    font-weight: bold;
    padding: 4px 0px;

  }

  .btn-signout {
    width: 60px;
    font-size: 5pt;
    font-weight: bold;
    border: solid 1px rgb(63, 63, 63);
    color: rgb(60, 60, 60);
    padding: 4px 0px;

  }
</style>