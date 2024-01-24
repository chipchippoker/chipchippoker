<template>
  <div>
    <div
      class="d-flex flex-column justify-content-center align-items-center position-absolute top-50 start-50 translate-middle">
      <div class="d-flex justify-content-center">
        <img class="small-logo" src="/src/assets/icons/Logo.png" alt="">
      </div>

      <div class="formstyle">
        <div class="h2 fw-bold">회원가입</div>
        <form @submit.prevent="signUp" style="width: 400px;">
          <div class="mb-3">
            <label for="nickname" class="form-label">닉네임</label>
            <div class="input-group">
              <input type="text" class="form-control" id="nickname" placeholder="nickname" v-model="nickname">
              <button class="btn btn-outline-secondary btn-light" type="button" id="check_nickname"
                @click="checkNickname">중복 확인</button>
            </div>
            <div v-if="isNickDuplicated" class="form-text text-danger">이미 사용 중인 닉네임입니다.</div>
            <div v-if="!isValidNickname" id="nickname" class="fw-lgitighter x-little-text text-danger">한글 또는 영어 또는 숫자 또는
              (_)의 4 ~ 16 글자이어야 합니다.</div>
            <div v-if="isNickDuplicated===null" id="nickname" class="fw-lgitighter x-little-text text-danger">닉네임 중복확인을 해주세요.</div>
          </div>
          <div class="mb-3">
            <label for="id" class="form-label">아이디</label>
            <div class="input-group">
              <input type="text" class="form-control" id="id" placeholder="id" v-model="memberId">
              <button class="btn btn-outline-secondary btn-light" type="button" id="check_id" @click="checkMemberId">중복
                확인</button>
            </div>
            <div v-if="isIdDuplicated" class="form-text text-danger">이미 사용 중인 아이디입니다.</div>
            <div v-if="!isValidMemberId" id="id" class="fw-lgitighter x-little-text text-danger">영어 또는 숫자의 6 ~ 16 글자이어야
              합니다.</div>
            <div v-if="isValidMemberId===null" id="id" class="fw-lgitighter x-little-text text-danger">아이디 중복확인을 해주세요.</div>
          </div>
          <div class="mb-3">
            <label for="password1" class="form-label">비밀번호</label>
            <input type="password" class="form-control" id="password1" placeholder="password" v-model="password1">
            <div v-if="!isValidPassword1" id="id" class="fw-lgitighter x-little-text text-danger">영어, 숫자, 특수문자를 모두 포함한 8 ~
              30 글자이어야 합니다.</div>
          </div>
          <div class="mb-3">
            <label for="password2" class="form-label">비밀번호 확인</label>
            <input type="password" class="form-control" id="password2" placeholder="password" v-model="password2">
            <div v-if="!isValidPassword2" id="id" class="fw-lgitighter x-little-text text-danger">비밀번호가 똑같아야 합니다.</div>
          </div>




          <!-- 아이콘 모달 버튼 -->
          <div class="d-flex justify-content-center">
            <button class="btn-transparency" type="button">
              <img data-bs-toggle="modal" data-bs-target="#IconModal" class="small-icon"
                :src='userStore.getIconUrl(userStore.myIcon)' :alt="userStore.myIcon">
            </button>
          </div>

          <div class="d-grid gap-2 pt-3">
            <button :disabled="!(isValidMemberId && isValidPassword1 && isValidPassword2 && isValidNickname && !isNickDuplicated && !isIdDuplicated)"
              type="submit" class="btn btn-primary btn-login">회원가입</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 아이콘 모달 팝업 -->
    <div class="modal fade" id="IconModal" tabindex="-1" aria-labelledby="IconModalLabel" aria-hidden="true">
      <ModalIconList />
    </div>


  </div>
</template>
  
<script setup>
import { ref, watch } from "vue";
import { useUserStore } from "@/stores/user";
import ModalIconList from "@/components/Modal/ModalIconList.vue";
import { useRouter } from "vue-router";

const userStore = useUserStore()
const memberId = ref(null)
const password1 = ref(null)
const password2 = ref(null)
const nickname = ref(null)
const isNickDuplicated = ref(null)
const isIdDuplicated = ref(null)
const isValidMemberId = ref(true)
const isValidPassword1 = ref(true)
const isValidPassword2 = ref(true)
const isValidNickname = ref(true)
const router = useRouter()

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
  router.push({ name: 'main' })
}

// 닉네임 중복 확인
const checkNickname = function () {
  userStore.checkNickname(nickname.value);
  isNickDuplicated.value = userStore.isNickDuplicated
}

// 아이디 중복 확인
const checkMemberId = function () {
  userStore.checkMemberId(memberId.value);
  isIdDuplicated.value = userStore.isIdDuplicated;
}

watch([memberId, password1, password2, nickname], () => {
  isValidMemberId.value = userStore.validateId(memberId.value);
  isValidPassword1.value = userStore.validatePassword(password1.value);
  isValidNickname.value = userStore.validateNickname(nickname.value);
  if (password2.value !== null && password1.value !== password2.value) {
    isValidPassword2.value = false
  } else {
    isValidPassword2.value = true
  }
});

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