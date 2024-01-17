<template>
  <div
    class="container d-flex flex-column align-items-center p-3"
    style="width: 100%; height: 100%"
  >
    <!-- 옵션, 방제, 아이콘 -->
    <div
      class="d-flex justify-content-end align-items-center position-relative"
      style="width: 100%; height: 100px"
    >
      <!-- 아이콘 -->
      <!-- <div class="d-flex flex-row justify-content-between" style="width: 10%">
        <div class="mx-3">
          <font-awesome-icon
            icon="bell"
            shake
            size="lg"
            style="color: #ffffff"
          />
        </div>
        <div class="mx-3">
          <font-awesome-icon icon="gear" size="lg" style="color: #ffffff" />
        </div>
        <div class="ms-3">
          <font-awesome-icon icon="book" size="lg" style="color: #ffffff" />
        </div>
      </div> -->
      <!-- 방제 -->
      <div class="position-absolute top-50 start-50 translate-middle">
        <h3 class="room-title">어서오세요. 싸피의 세계로!!</h3>
      </div>
      <!-- 유저 아이콘 -->
      <div class="d-flex justify-content-center">
          <img class="small-icon" :src='userStore.getIconUrl(userStore.myIcon)' :alt="userStore.myIcon">
        </div>
    </div>

    <!--  body -->
    <div class="d-flex flex-row justify-content-center row pe-4 ps-0 mx-0 mt-1 w-100">

      <!-- 캠 -->
      <div class="d-flex flex-wrap col-10 pe-0">
        <WaitPlayerList />
      </div>

      <!-- 로고, 관전자 목록, 채팅창, 버튼 -->
      <div class="d-flex flex-column col-2 p-0 m-0">
        <!-- 로고 -->
        <div class="my-3 me-5"><img class="small-logo ms-4 mt-1 ps-1" src="/src/assets/Logo.png" alt=""></div>
        <div class="d-flex flex-column justify-content-between h-100">
          <!-- 관전자 목록 -->
          <div class="mt-2 mb-0 box-watcher">
            <WaitWatcher />
          </div>
          <!-- 채팅창 -->
          <div class="mt-0 mb-5 box-chat">
            <WaitTalk />
          </div>
          <!-- 여러 버튼들 -->
          <div class="d-flex flex-column justify-content-center align-items-center box-btns m-0 pb-4">
            <div>
              <button class="custom-btn btn-1 m-1"><span>시작해?</span><span>시작</span></button>
              <button class="custom-btn btn-2 m-1"><span>초대해?</span><span>초대</span></button>
              <button class="custom-btn btn-3 m-1"><span>나가?</span><span>나가기</span></button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import WaitPlayerList from '@/components/Wait/WaitPlayerList.vue'
import WaitWatcher from "@/components/Wait/WaitWatcher.vue";
import WaitTalk from "@/components/Wait/WaitTalk.vue";
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()


</script>

<style scoped>
@import "@/assets/color.css";
@import "@/assets/size.css";

.room-title {
  color: white;
}


.box-right {
  position: relative;
}


.box-watcher {
  height: 130px;
}

.box-chat {
  margin-bottom: 10px;
  height: 200px;
}

button {
  border-radius: 20px;
  border: 1px solid white;
  color: white;
  width: 60px;
  height: 40px;
  font-size: 15px;
  font-weight: bold;
  padding: 0%;
}

.box-btns {
  margin-top: 10px;
}
.btn-start {
 background-color: blue;
}

.btn-start:hover {
  background-color: blue;
  opacity: 50%;
  border: 1px solid white;
  color: white;
}
.btn-invite {
  background-color: green;
}

.btn-invite:hover {
  background-color: green;
  border: 1px solid white;
  opacity: 50%;
  color: white;
}

.btn-exit {
  background-color: red;
}

.btn-exit:hover {
  background-color: red;
  opacity: 50%;
  border: 1px solid white;
  color: white;
}


.custom-btn {
  width: 60px;
  height: 40px;
  color: #fff;
  border-radius: 5px;
  padding: 10px 25px;
  font-family: 'Lato', sans-serif;
  font-weight: 500;
  background: transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  display: inline-block;
   box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  outline: none;
}

@-webkit-keyframes shiny-btn1 {
    0% { -webkit-transform: scale(0) rotate(45deg); opacity: 0; }
    80% { -webkit-transform: scale(0) rotate(45deg); opacity: 0.5; }
    81% { -webkit-transform: scale(4) rotate(45deg); opacity: 1; }
    100% { -webkit-transform: scale(50) rotate(45deg); opacity: 0; }
}


/* 12 */
.btn-1{
  position: relative;
  right: 20px;
  bottom: 20px;
  border:none;
  box-shadow: none;
  width: 60px;
  height: 40px;
  line-height: 42px;
  -webkit-perspective: 230px;
  perspective: 230px;
}
.btn-2{
  position: relative;
  right: 20px;
  bottom: 20px;
  border:none;
  box-shadow: none;
  width: 60px;
  height: 40px;
  line-height: 42px;
  -webkit-perspective: 230px;
  perspective: 230px;
}
.btn-3{
  position: relative;
  right: 20px;
  bottom: 20px;
  border:none;
  box-shadow: none;
  width: 60px;
  height: 40px;
  line-height: 42px;
  -webkit-perspective: 230px;
  perspective: 230px;
}
.btn-1 span {
  background: rgb(0,172,238);
background: linear-gradient(0deg, rgba(0,172,238,1) 0%, rgba(2,126,251,1) 100%);
  display: block;
  position: absolute;
  width: 60px;
  height: 40px;
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  border-radius: 5px;
  margin:0;
  text-align: center;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  -webkit-transition: all .3s;
  transition: all .3s;
}
.btn-2 span {
  background: rgb(45, 217, 82);
background: linear-gradient(0deg, rgb(45, 217, 82) 0%, rgb(33, 168, 62) 100%);
  display: block;
  position: absolute;
  width: 60px;
  height: 40px;
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  border-radius: 5px;
  margin:0;
  text-align: center;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  -webkit-transition: all .3s;
  transition: all .3s;
}
.btn-3 span {
  background: rgb(218, 62, 62);
background: linear-gradient(0deg, rgb(218, 62, 62) 0%, rgb(215, 30, 30) 100%);
  display: block;
  position: absolute;
  width: 60px;
  height: 40px;
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  border-radius: 5px;
  margin:0;
  text-align: center;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  -webkit-transition: all .3s;
  transition: all .3s;
}
.custom-btn span:nth-child(1) {
  box-shadow:
   -7px -7px 20px 0px #fff9,
   -4px -4px 5px 0px #fff9,
   7px 7px 20px 0px #0002,
   4px 4px 5px 0px #0001;
  -webkit-transform: rotateX(90deg);
  -moz-transform: rotateX(90deg);
  transform: rotateX(90deg);
  -webkit-transform-origin: 50% 50% -20px;
  -moz-transform-origin: 50% 50% -20px;
  transform-origin: 50% 50% -20px;
}
.custom-btn span:nth-child(2) {
  -webkit-transform: rotateX(0deg);
  -moz-transform: rotateX(0deg);
  transform: rotateX(0deg);
  -webkit-transform-origin: 50% 50% -20px;
  -moz-transform-origin: 50% 50% -20px;
  transform-origin: 50% 50% -20px;
}
.custom-btn:hover span:nth-child(1) {
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  -webkit-transform: rotateX(0deg);
  -moz-transform: rotateX(0deg);
  transform: rotateX(0deg);
}
.custom-btn:hover span:nth-child(2) {
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
 color: transparent;
  -webkit-transform: rotateX(-90deg);
  -moz-transform: rotateX(-90deg);
  transform: rotateX(-90deg);
}


</style>
