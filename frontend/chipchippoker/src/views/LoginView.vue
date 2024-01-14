<template>
    <!-- 로그인 -->
    <div class="d-flex flex-column justify-content-center align-items-center position-absolute top-50 start-50 translate-middle">
      <div class="d-flex justify-content-center">
        <img class="big-logo" src="/src/assets/Logo.png" alt="">
      </div>
        <div class="formstyle">
          <div class="fs-2 fw-bold">로그인</div>
            <form @submit.prevent="generalLogIn" style="width: 400px;">
              <!-- 아이디 -->
              <div class="mb-3">
                <label for="memberId" class="form-label">아이디</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="memberId" placeholder="아이디" v-model="memberId">
                </div>
                <div v-if="isValidMemberId === false">아이디 형식이 맞지 않습니다.</div>
              </div>  
              <!-- 비밀번호 -->
              <div class="mb-3">
                <label for="password" class="form-label">비밀번호</label>
                <div>
                  <input type="password" class="form-control" id="password" placeholder="비밀번호" v-model="password">
                </div>
                <div v-if="isValidPassword === false">비밀번호 형식이 맞지 않습니다.</div>

              </div>
              <!-- 일반 로그인 버튼 -->
              <div class="d-grid gap-2 pt-3">
                <button type="submit" class="btn btn-primary btn-login">로그인</button>
              </div>
              <!-- 카카오 로그인 버튼 -->
              <div class="d-grid gap-2 pt-3">
                <button @click="simpleLogIn" type="button" class="btn btn-secondary btn-kakao-login">
                  <img class="kakao-logo" src="@/assets/icons/kakaologo.png" alt="">카카오로그인
                </button>
              </div>
          </form>
          </div>
      </div>
  </template>
  
  <script setup>
  import { computed, ref } from "vue";
  import { useUserStore } from '@/stores/user'

  const userStore = useUserStore()
  const memberId = ref(null)
  const password = ref(null)
  const isValidMemberId = computed(() => {
    return userStore.validateMemberId(memberId.value)
  })
  const isValidPassword = computed(() => {
    return userStore.validatePassword(password.value)
  })

  // 일반 로그인
  const generalLogIn = function () {
    const payload = {
      memberId: memberId.value,
      password: password.value
    }
    console.log('일반 로그인 요청')
    console.log(payload)
    userStore.generalLogIn(payload)
  }

  // 간편 로그인
  const simpleLogIn = function () {
    console.log('간편 로그인 요청');
    userStore.simpleLogIn()
  }
  </script>
  
  <style scoped>
  @import "@/assets/color.css";
  @import "@/assets/size.css";
  .container {
      background-color: #99A5C1;
      color: white;
      width: 400px;
      margin: 20px;
      border-radius: 20px;
  }
  .login-form {
      max-width: 300px;
  }
  
  label {
      color: black;
  }
  .kakao-logo {
    width: 25px;
    height: 25px;
    display: inline;
    margin: 0px;
  }
  .btn-login {
      box-shadow: 0 0 0 3px #ffffff inset;
      border: 0px;
      background-color: #99A5C1;
      width: 100%;
  }
  .btn-login:hover {
    background-color: rgb(95, 98, 136);
  }
  
  .btn-kakao-login {
      width: 100%;
      background-color: #F7E409;
      color: black;
      border: 0;
      font-weight: bold;
  }
  
  .btn-kakao-login:hover {
    background-color: #ac9e08;
    transition: 0.3s;
  }
  </style>
  