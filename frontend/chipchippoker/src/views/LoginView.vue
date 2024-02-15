<template>
  <!-- 로그인 -->
  <div class="d-flex flex-column justify-content-center align-items-center position-absolute top-50 start-50 translate-middle">
    <div class="d-flex justify-content-center">
      <img class="big-logo" src="/src/assets/icons/Logo.png" alt="">
    </div>
      <div class="formstyle">
        <div class="fs-2 fw-bold">로그인</div>
          <form @submit.prevent="generalLogIn()" style="width: 400px;">
            <!-- 아이디 -->
            <div class="mb-3">
              <label for="memberId" class="form-label">아이디</label>
              <div class="input-group">
                <input type="text " class="form-control" id="memberId" placeholder="아이디" v-model="memberId">
              </div>
              <div class="text-danger little-text" v-if="isValidMemberId === false">아이디 형식이 맞지 않습니다.</div>
            </div>  
            <!-- 비밀번호 -->
            <div class="mb-3">
              <label for="password" class="form-label">비밀번호</label>
              <div>
                <input type="password" class="form-control" id="password" placeholder="비밀번호" autocomplete="on" v-model="password">
              </div>
            <div class="text-danger  little-text" v-if="isValidPassword === false">비밀번호 형식이 맞지 않습니다.</div>

          </div>
          <!-- 일반 로그인 버튼 -->
          <div class="d-grid gap-2 pt-3">
            <button type="submit" class="btn-3-indigo"><div class="fs-6 pt-1" style="width: 100%; height: 100%;">로그인</div></button>
          </div>
          <!-- 카카오 로그인 버튼 -->
          <div class="d-grid gap-2 pt-3">
            <button @click="simpleLogIn()" type="button" class="btn btn-secondary btn-kakao-login">
              <img class="kakao-logo" src="@/assets/icons/kakaologo.png" alt="">카카오로그인
            </button>
          </div>
      </form>
    </div>
    <div class="text-white mt-3">
      아직 회원이 아니십니까? 
      <a type="button" @click="goSignUp()" style="color:#70E5FF;">
        회원가입 ></a>
    </div>
  </div>
</template>
  
<script setup>
  import { computed, ref } from "vue";
  import { useUserStore } from '@/stores/user'
  import { useRoute,useRouter } from 'vue-router'
  import { useSoundStore } from "@/stores/sound";

  const userStore = useUserStore()
  const soundStore = useSoundStore()
  const route = useRoute()
  const router = useRouter()
  
  const memberId = ref(null)
  const password = ref(null)
  const authorizationCode = ref(null)

  // 유효성 검사
  const isValidMemberId = computed(() => {
    return userStore.validateId(memberId.value)
  })
  const isValidPassword = computed(() => {
    return userStore.validatePassword(password.value)
  })

  // 일반 로그인
  const generalLogIn = async function () {
    const payload = {
      memberId: memberId.value,
      password: password.value
    }
    await soundStore.bgmOn()
    userStore.generalLogIn(payload)
    .then(result => {
      if (result) {
        memberId.value = null
        password.value = null
      } else {
        alert("로그인 실패했습니다.")
      }
    })
  }
  
  // 카카오 인가코드 요청
  const simpleLogIn = function () {
    userStore.getKakaoCode()
  }

  // 카카오 인가코드 받기
  authorizationCode.value = route.query.code
  userStore.authorizationCode = authorizationCode.value

  // 인가코드 받으면
  if (authorizationCode.value) {
    userStore.simpleLogInRequest(authorizationCode.value)
    .then(result => {
      if (result) {
        authorizationCode.value = null
      } else {
        alert("간편 로그인 실패했습니다.")
        router.push({ name: 'login' }).catch(() => {})
      }
    })
  }

  // 회원가입으로 이동
  const goSignUp = function(){
    router.push({name:'signup'})
  }
</script>
  
<style lang="scss" scoped>
  
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
 
  .btn-3-indigo {
    text-align: center;
    width: 400px;
    height: 40px;
    margin: 0;
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
  