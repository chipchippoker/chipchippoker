<script setup>
import ModalGuide from './components/Modal/ModalGuide.vue';
import ModalNotificationList from './components/Modal/ModalNotificationList.vue';
import ModalMainSettings from './components/Modal/ModalMainSettings.vue';
import { RouterLink, RouterView } from 'vue-router'
import { useRouter } from 'vue-router';

const router = useRouter()
const gotoProfile = function(){
  router.push({name:'profile'})
}
</script>

<template>
  <div>


    
  <div>
  <RouterLink to="login">로그인</RouterLink> /
  <RouterLink to="signup">회원가입</RouterLink> / 
  <RouterLink to="kakaosignup">카카오 회원가입</RouterLink> /
  <RouterLink to="profile">프로필 나의정보</RouterLink> /
  <RouterLink to="main">메인페이지</RouterLink>  /
  <RouterLink to="play">플레이페이지</RouterLink> /
  <RouterLink to="wait">대기페이지</RouterLink>
  </div>

  <!-- 옵션, 로고, 아이콘 -->
  <div class="d-flex justify-content-between align-items-center position-absolute" style="width: 100%; height: 100px;">
      <div class="d-flex flex-row justify-content-between" style="width: 10%;">
        <div class="mx-3">
          <font-awesome-icon icon="bell"  shake size="lg" data-bs-toggle="modal" data-bs-target="#alarmModal" style="color: #ffffff;" />
        </div>
        <div class="mx-3">
          <font-awesome-icon icon="gear" size="lg" data-bs-toggle="modal" data-bs-target="#settingModal" style="color: #ffffff;" />
        </div>
        <div class="ms-3">
          <font-awesome-icon icon="book" size="lg" data-bs-toggle="modal" data-bs-target="#guideModal" style="color: #ffffff;" />
        </div>
      </div>
      <!-- <div class="mx-3">
        <font-awesome-icon @click="gotoProfile" icon="user" size="lg" style="color: #ffffff;"  />
      </div> -->
    </div>


    <div id="app" class="bg-gradation-blue d-flex justify-content-center">
      <RouterView />
    </div>


    <!-- 알림 Modal -->
    <div class="modal fade" id="alarmModal" tabindex="-1" aria-labelledby="alarmModalLabel" aria-hidden="true">
      <div class=" modal-dialog modal-dialog-scrollable modal-dialog-centered">
        <div class="modal-content " style="background-color: #ffde76;">
          <div class="modal-header border-0">
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">

            <ModalNotificationList/>
          </div>
        </div>
      </div>
    </div>
    <!-- 세팅 Modal -->
    <div class="modal fade" id="settingModal" tabindex="-1" aria-labelledby="settingModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered">
        <div class="modal-content" style="background-color: #ffde76;">
          <div class="modal-header border-0">
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <ModalMainSettings/>

          </div>
        </div>
      </div>
    </div>
    <!-- 가이드북 Modal -->
    <div class="modal fade " id="guideModal" tabindex="-1" aria-labelledby="guideModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content" style="background-color: #ffde76;">
          <div class="modal-header border-0">
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <ModalGuide/>


          </div>
        </div>
      </div>
    </div>




  </div>
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Black+Han+Sans&family=Dongle:wght@300;400;700&family=Jua&family=Nanum+Gothic+Coding:wght@400;700&family=Nanum+Pen+Script&display=swap');
@import "@/assets/color.css";
@import "@/assets/size.css";

</style>