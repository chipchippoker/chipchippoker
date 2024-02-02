<template>
  <div>
    <div
      class="d-flex flex-column justify-content-center align-items-center position-absolute top-50 start-50 translate-middle">
      <div class="d-flex justify-content-center">
        <img class="small-logo" src="/src/assets/icons/Logo.png" alt="">
      </div>

      <div class="formstyle">
        <div class="h2 fw-bold">회원가입</div>
        <!-- 회원가입 -->
        <form @submit.prevent="signUp" style="width: 400px;">
          <!-- 닉네임 -->
          <div class="mb-3">
            <label for="nickname" class="form-label">닉네임</label>
            <div class="input-group">
              <input type="text" class="form-control" id="nickname" placeholder="nickname" v-model="nickname">
              <button class="btn btn-outline-secondary btn-light" type="button" id="check_nickname"
                @click="checkNickname">중복 확인</button>
            </div>
            <div v-if="isNickDuplicated" class="form-text text-danger">이미 사용 중인 닉네임입니다.</div>
            <div v-if="nickname!=='' && !isValidNickname" id="nickname" class="fw-lgitighter x-little-text text-danger">한글 또는 영어 또는 숫자 또는
              (_)의 2 ~ 12 글자이어야 합니다.</div>
            <div v-if="nickname!=='' && isNickDuplicated===null" id="nickname" class="fw-lgitighter x-little-text text-danger">닉네임 중복확인을 해주세요.</div>
            <div v-if="isValidNickname && isNickDuplicated===false" id="id" class="fw-lgitighter x-little-text text-primary">사용 가능한 닉네임입니다.</div>
          </div>
          <!-- 아이디 -->
          <div class="mb-3">
            <label for="id" class="form-label">아이디</label>
            <div class="input-group">
              <input type="text" class="form-control" id="id" placeholder="id" v-model="memberId">
              <button class="btn btn-outline-secondary btn-light" type="button" id="check_id" @click="checkMemberId">중복
                확인</button>
            </div>
            <div v-if="isIdDuplicated" class="form-text text-danger">이미 사용 중인 아이디입니다.</div>
            <div v-if="memberId!=='' && !isValidMemberId" id="id" class="fw-lgitighter x-little-text text-danger">영어 또는 숫자의 6 ~ 16 글자이어야
              합니다.</div>
            <div v-if="memberId!=='' && memberId!==null && isIdDuplicated===null" id="id" class="fw-lgitighter x-little-text text-danger">아이디 중복확인을 해주세요.</div>
            <div v-if="isValidMemberId && isIdDuplicated===false" id="id" class="fw-lgitighter x-little-text text-primary">사용 가능한 아이디입니다.</div>
          </div>
          <!-- 비밀번호 -->
          <div class="mb-3">
            <label for="password1" class="form-label">비밀번호</label>
            <input type="password" class="form-control" id="password1" placeholder="password" v-model="password1">
            <div v-if="password1!=='' && !isValidPassword1" id="id" class="fw-lgitighter x-little-text text-danger">영어, 숫자, 특수문자를 모두 포함한 8 ~
              30 글자이어야 합니다.</div>
          </div>
          <!-- 비밀번호 확인 -->
          <div class="mb-3">
            <label for="password2" class="form-label">비밀번호 확인</label>
            <input type="password" class="form-control" id="password2" placeholder="password" v-model="password2">
            <div v-if="password2!=='' && !isValidPassword2" id="id" class="fw-lgitighter x-little-text text-danger">비밀번호가 똑같아야 합니다.</div>
            <div v-if="password1 && isValidPassword1 && isValidPassword2 && password1===password2" id="id" class="fw-lgitighter x-little-text text-primary">비밀번호 확인이 완료되었습니다.</div>
          </div>

          <!-- 아이콘 모달 버튼 -->
          <div class="d-flex justify-content-center">
            <button class="btn-transparency" type="button">
              <img data-bs-toggle="modal" data-bs-target="#IconModal" class="small-icon"
                :src='iconUrl' :alt="userStore.myIcon">
            </button>
          </div>

          <!-- 회원가입 버튼 -->
          <div class="d-grid gap-2 pt-3">
            <button :disabled="!(isValidMemberId && isValidPassword1 && isValidPassword2 && isValidNickname && isNickDuplicated === false && isIdDuplicated === false)"
              type="submit" class="btn btn-primary btn-login">회원가입</button>
          </div>
        </form>
      </div>
      <div class="text-white mt-3">
        이미 회원이십니까?
        <a type="button" @click="goLogIn()" style="color:#70E5FF;">
          로그인 ></a>
      </div>
    </div>

    <!-- 아이콘 모달 팝업 -->
    <div class="modal fade" id="IconModal" tabindex="-1" aria-labelledby="IconModalLabel" aria-hidden="true">
      <ModalIconList />
    </div>

  </div>
</template>
  
<script setup>
  import { ref, watch,computed } from "vue";
  import { useUserStore } from "@/stores/user";
  import ModalIconList from "@/components/Modal/ModalIconList.vue";
  import { useRouter } from "vue-router";
  import { useRoomStore } from "../stores/room";

  const router = useRouter()
  const userStore = useUserStore()
  const memberId = ref('')
  const password1 = ref('')
  const password2 = ref('')
  const nickname = ref('')

  const isNickDuplicated = ref(null)
  const isIdDuplicated = ref(null)
  const isValidMemberId = ref(true)
  const isValidPassword1 = ref(true)
  const isValidPassword2 = ref(true)
  const isValidNickname = ref(true)

  // 아이콘
  const iconUrl = computed(() => userStore.getIconUrl(userStore.myIcon))

  // 회원가입
  const signUp = function () {
    const payload = {
      memberId: memberId.value,
      password: password1.value,
      passwordConfirm: password2.value,
      nickname: nickname.value,
      icon: userStore.myIcon
    }
    userStore.signUp(payload)
    .then(result => {
      if (result) {
        memberId.value = null
        password1.value = null
        password2.value = null
        nickname.value = null
      } else {
        alert("회원가입 실패했습니다.")
      }
    })
  }

  // 닉네임 중복 확인
  const checkNickname = function () {
    userStore.checkNickname(nickname.value)
    .then(result => {
      isNickDuplicated.value = result
    })
    .catch(err => alert(err))
  }

  // 아이디 중복 확인
  const checkMemberId = function () {
    userStore.checkMemberId(memberId.value)
    .then(result => {
      isIdDuplicated.value = result
    })
    .catch(err => alert(err))
  }

  // 유효성 검사
  watch([memberId, password1, password2, nickname], () => {
    isValidMemberId.value = userStore.validateId(memberId.value);
    isValidPassword1.value = userStore.validatePassword(password1.value);
    isValidNickname.value = userStore.validateNickname(nickname.value);


    if (password2.value !== null && password1.value !== password2.value) {
      isValidPassword2.value = false
    } else {
      isValidPassword2.value = true
    }
  })

  watch([nickname], () => {
    // 입력값이 변경될 때마다 중복 확인 상태 초기화
    isNickDuplicated.value = null;
  })

  watch([memberId], () => {
    // 입력값이 변경될 때마다 중복 확인 상태 초기화
    isIdDuplicated.value = null;
  })

  // 로그인으로 이동
  const goLogIn = function(){
    router.push({name:'login'})
  }
</script>

  
<style scoped>
.btn-login {
      box-shadow: 0 0 0 3px #ffffff inset;
      border: 0px;
      background-color: #99A5C1;
      width: 100%;
  }
  .btn-login:hover {
    background-color: rgb(95, 98, 136);
  }
</style>