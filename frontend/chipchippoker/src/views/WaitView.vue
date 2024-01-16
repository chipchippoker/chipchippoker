<template>
  <div
    class="container d-flex flex-column align-items-center p-3"
    style="width: 100%; height: 100%"
  >
    <!-- 옵션, 방제, 아이콘 -->
    <div
      class="d-flex justify-content-between align-items-center position-relative"
      style="width: 100%; height: 100px"
    >
      <!-- 아이콘 -->
      <div class="d-flex flex-row justify-content-between" style="width: 10%">
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
      </div>
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
              <button class="btn-start m-1">시작</button>
              <button class="btn-invite m-1">초대</button>
              <button class="btn-exit m-1">나가기</button>
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
}

.box-btns {
  margin-top: 10px;
}
.btn-start {
 background-color: blue;
}

.btn-invite {
  background-color: green;
}

.btn-exit {
  background-color: red;
}

</style>
