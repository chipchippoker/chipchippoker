<template>
  <div class="d-flex p-0">
    
  <!-- 나의 정보, 게임 프로필 -->
  <div class="row flex-grow-1" style="background-color: #576FAC;">
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


  <div class="tab">
  <button class="tablinks" onclick="openCity(event, 'London')">London</button>
  <button class="tablinks" onclick="openCity(event, 'Paris')">Paris</button>
  
</div>

<div id="London" class="tabcontent">
  <h3>London</h3>
  <p>London is the capital city of England.</p>
</div>

<div id="Paris" class="tabcontent">
  <h3>Paris</h3>
  <p>Paris is the capital of France.</p>
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



  * {box-sizing: border-box}

/* Style the tab */
.tab {
  float: left;
  border: 1px solid #ccc;
  background-color: #f1f1f1;
  width: 30%;
  height: 300px;
}

/* Style the buttons that are used to open the tab content */
.tab button {
  display: block;
  background-color: inherit;
  color: black;
  padding: 22px 16px;
  width: 100%;
  border: none;
  outline: none;
  text-align: left;
  cursor: pointer;
  transition: 0.3s;
}

/* Change background color of buttons on hover */
.tab button:hover {
  background-color: #ddd;
}

/* Create an active/current "tab button" class */
.tab button.active {
  background-color: #ccc;
}

/* Style the tab content */
.tabcontent {
  float: left;
  padding: 0px 12px;
  border: 1px solid #ccc;
  width: 70%;
  border-left: none;
  height: 300px;
}

</style>