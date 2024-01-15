<script setup>
import { RouterLink, RouterView } from 'vue-router'
// import VueWebRTC from 'vue-webrtc';

</script>

<template>
  <div>


    
  <div>
  <RouterLink to="login">로그인</RouterLink> /
  <RouterLink to="signup">회원가입</RouterLink> / 
  <RouterLink to="kakaosignup">카카오 회원가입</RouterLink> /
  <RouterLink to="profile">프로필 나의정보</RouterLink> /
  <RouterLink to="main">메인페이지</RouterLink> 
  </div>
  <div id="app" class="bg-gradation-blue d-flex justify-content-center">
    <div>
      <!-- <video ref="localVideo" autoplay></video>
      <video ref="remoteVideo" autoplay></video> -->
    </div>
    <RouterView />
  </div>
</div>
</template>

<style scoped>
@import '@/assets/color.css';
</style>