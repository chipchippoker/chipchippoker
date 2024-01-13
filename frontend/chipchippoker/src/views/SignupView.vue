<template>
    <div class="d-flex flex-column justify-content-center align-items-center position-absolute top-50 start-50 translate-middle">
        <div class="d-flex justify-content-center">
            <img class="big-logo" src="/src/assets/Logo.svg" alt="">
        </div>
      <div class="formstyle">
        <div class="h2 fw-bold">회원가입</div>
        <form style="width: 400px;">
          <div class="mb-3">
            <label for="nickname" class="form-label">닉네임</label>
            <div class="input-group">
              <input type="text" class="form-control" id="nickname" placeholder="nickname">
              <button class="btn btn-outline-secondary btn-light" type="button" id="check_nickname">중복 확인</button>
            </div>
            <div id="nickname" class="form-text">한글, 영어, 숫자, 특수문자(_) 의 16 글자</div>
          </div>
          <div class="mb-3">
            <label for="id" class="form-label">아이디</label>
            <div class="input-group">
              <input type="text" class="form-control" id="id" placeholder="id">
              <button class="btn btn-outline-secondary btn-light" type="button" id="check_id">중복 확인</button>
            </div>
            <div id="id" class="form-text">영어, 숫자의 6 ~ 16 글자</div>
          </div>
          <div class="mb-3">
            <label for="password1" class="form-label">비밀번호</label>
            <input type="password" class="form-control" id="password1" placeholder="password">
          </div>
          <div class="mb-3">
            <label for="password2" class="form-label">비밀번호 확인</label>
            <input type="password" class="form-control" id="password2" placeholder="password">
          </div>
          <div class="d-grid gap-2 pt-3">
            <button type="submit" class="btn btn-outline-yellow">제출하기</button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from "vue";
  import { useUserStore } from "@/stores/user";
  
  
  // const store = useUserStore()
  // const username = ref(null)
  // const password1 = ref(null)
  // const password2 = ref(null)
  // const nickname = ref(null)
  // const gender = ref(null)
  // const age = ref(null)
  // const masks = ref({
  //   modelValue: 'YYYY-MM-DD',
  // })
  
  // const signup = function () {
  //   const payload = {
  //     username: username.value,
  //     password1: password1.value,
  //     password2: password2.value,
  //     nickname: nickname.value,
  //     gender: gender.value,
  //     age: age.value,
  //   }
  //   store.signup(payload)
  // }
  </script>
  
  <style scoped>
  @import '@/assets/color.css';
  @import "@/assets/size.css";
  </style>