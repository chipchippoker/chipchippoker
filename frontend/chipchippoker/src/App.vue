<script setup>
import { RouterLink, RouterView } from 'vue-router'

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
  <RouterLink to="wait">대기페이지</RouterLink> /
  <RouterLink to="video">비디오</RouterLink> /
  <RouterLink to="test">테스트메인</RouterLink>
  </div>
  <div id="app" class="bg-gradation-blue d-flex justify-content-center">
      <RouterView />
    </div>
  </div>
</template>

<style>

@import "@/assets/color.css";
@import "@/assets/size.css";

</style>