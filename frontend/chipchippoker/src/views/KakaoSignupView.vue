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
            <label for="nickName" class="form-label">닉네임</label>
            <div class="input-group mb-3">
              <input v-model="nickName" type="text" class="form-control" id="nickName" placeholder="nickName">
              <button @click="checkNickName" class="btn btn-outline-secondary" type="button" id="check_nickName">중복 확인</button>
            </div>
            <div v-if="isCorrectNick === false" class="fw-lgitighter little-text text-danger">
              닉네임 형식이 맞지 않습니다.
            </div>
            <div v-if="userStore.isNickDuplicated === true" class="fw-lgitighter little-text text-danger">
                 중복된 닉네임 입니다
            </div>
          </div>
          <div class="fw-lgitighter little-text">
          ※ 닉네임은 4글자 ~ 16글자 사이의 한글, 영어, 숫자, 특수문자 ( _ )로 이루어져야 합니다.
          </div>

          <!-- 닉네임 중복 체크를 통과해야만 보임 -->
          <div class="d-grid gap-2 pt-3" :class="{disable:(isCorrectNick || !(userStore.isNickDuplicated))}">
            <button @click="signUp" type="submit" class="btn bg-lightyellow btn-outline-yellow">회원가입</button>
          </div>
        </form>
      </div>
      </div>
  
    </div>
  
  </template>
    
  <script setup>
  import { ref,computed } from "vue";
  import { useUserStore } from "@/stores/user";
  import { useRouter } from "vue-router";
  
  const userStore = useUserStore()
  const router = useRouter()
  // 닉네임 변수
  const nickName = ref('')
  
  const isCorrectNick = computed(()=>{
    // userStore에 있는 닉네임 유효성 검사를 호출하고, 결과를 반환
    return userStore.validateNickName(nickName.value);
  }) 

  // 닉네임 중복 확인 함수 -> userStore의 닉네임 중복 체크 함수 호출
  const checkNickName = function(){
    console.log('닉네임 중복확인')
    // userStore.checkNickName(nickName.value)
  }
  
  // 회원가입 함수 -> userStore의 카카오 회원가입 함수 호출
  const signUp = function(){
    console.log('카카오 회원가입 후 메인페이지로 이동')
    // userStore.kakaoSignUp(nickName.value)
    // 메인페이지로 이동
    // router.push('메인페이지')
  }
  
  

  </script>
  
  <style scoped>
  @import '@/assets/color.css';
  @import '@/assets/size.css';
  </style>
    
  