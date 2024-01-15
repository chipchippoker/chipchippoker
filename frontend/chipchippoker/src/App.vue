<script setup>
import { RouterLink, RouterView } from 'vue-router'
// import VueWebRTC from 'vue-webrtc';

</script>

<template>
  <div id="app">
    <h1 class="text-3xl font-bold underline">
      Hello world!
    </h1>
    <div>
      <video ref="localVideo" autoplay></video>
      <video ref="remoteVideo" autoplay></video>
    </div>
    <RouterLink to="login">로그인</RouterLink> /
    <RouterLink to="signup">회원가입</RouterLink> / 
    <RouterLink to="kakaosignup">카카오 회원가입</RouterLink> /
    <RouterLink to="profile">프로필 나의정보</RouterLink> /
    <RouterView />
  </div>
</template>

<style scoped>
  @import '@/assets/color.css';
 
  #app {
    height: 100vh;
    background-image: linear-gradient(to bottom, #152856, #334A82);
    /* background-attachment: fixed; 또는 scroll */
  }
</style>