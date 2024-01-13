<template>

    <div class="bg-darkblue">
      <div class="d-flex flex-column justify-content-center align-items-center position-absolute top-50 start-50 translate-middle">
      <div class="d-flex justify-content-center">
        <img class="big-logo" src="/src/assets/Logo.png" alt="">
      </div>
      
      <div class="p-3 d-flex flex-column justify-content-center align-items-center bg-lightblue rounded-4">
        <div class="h2 fw-bold">회원가입</div>
        <form style="width: 400px;">
          <div class="mb-3">
            <label for="nickname" class="form-label">닉네임</label>
            <div class="input-group mb-3">
              <input type="text" class="form-control" id="nickname" placeholder="nickname">
              <button class="btn btn-outline-secondary" type="button" id="check_nickname">중복 확인</button>
            </div>
          </div>
          <div class="fw-lgitighter little-text">
          ※ 닉네임은 4글자 ~ 16글자 사이의 한글, 영어, 숫자, 특수문자 ( _ )로 이루어져야 합니다.
          </div>
          <div class="d-grid gap-2 pt-3">
            <button type="submit" class="btn btn-outline-yellow">제출하기</button>
          </div>
        </form>
      </div>
      </div>
  
    </div>
  
  </template>
    
  <script setup>
  import { ref } from "vue";
  import { useUserStore } from "@/stores/user";
  import { useRouter } from "vue-router";
  
  const router = useRouter()
  
  </script>
  
  <style scoped>
  @import '@/assets/color.css';
  @import '@/assets/size.css';
  </style>
    
  