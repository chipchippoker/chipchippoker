<template>
  <div>
    <div class="d-flex flex-column justify-content-center align-items-center position-absolute top-50 start-50 translate-middle">
      <!-- 칩칩포커 로고 -->
      <div class="d-flex justify-content-center">
        <img class="big-logo" src="/src/assets/icons/Logo.png" alt="">
      </div>
      
      <div class="formstyle">
        <div class="h2 fw-bold">회원가입</div>
        <form @submit.prevent="signUp" style="width: 400px;">
          <div class="mb-3">
            <label for="nickname" class="form-label">닉네임</label>
            <div class="input-group mb-3">
              <input v-model="nickname" type="text" class="form-control" id="nickname" placeholder="nickname">
              <!-- 닉네임 중복 확인 버튼 -->
              <button @click="checkNickname" class="btn btn-outline-secondary" type="button" id="nickname">중복 확인</button>
            </div>
            <div v-if="isNickDuplicated" class="form-text text-danger">이미 사용 중인 닉네임입니다.</div>
            <div v-if="!isValidNickname" id="nickname" class="fw-lgitighter little-text text-danger">한글 또는 영어 또는 숫자 또는 (_)의 4 ~ 16 글자이어야 합니다.</div>
          </div>
          <div class="fw-lgitighter little-text">
          ※ 닉네임은 4글자 ~ 16글자 사이의 한글, 영어, 숫자, 특수문자 ( _ )로 이루어져야 합니다.
          </div>

          <!-- 아이콘 모달 버튼 -->
          <div class="d-flex justify-content-center">
            <div type="button" class="btn-transparency">
              <img data-bs-toggle="modal" data-bs-target="#IconModal" class="small-icon" :src='userStore.getIconUrl(userStore.myIcon)' :alt="userStore.myIcon">
            </div>
          </div>

          <!-- 닉네임 중복 체크를 통과해야만 보임 -->
          <div class="d-grid gap-2 pt-3" v-if="isValidNickname && !isNickDuplicated">
            <button type="submit" class="btn btn-primary btn-login">회원가입</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 아이콘 모달 팝업 -->
    <div class="modal fade" id="IconModal" tabindex="-1" aria-labelledby="IconModalLabel" aria-hidden="true">
      <ModalIconList/>
    </div>
  </div>
</template>
  
<script setup>
import { ref,watch } from "vue";
import { useUserStore } from "@/stores/user";
import ModalIconList from "@/components/Modal/ModalIconList.vue";

const userStore = useUserStore()
const isNickDuplicated = ref(true)
const isValidNickname = ref(false)
const nickname = ref('')

watch([nickname], () => {
  isValidNickname.value = userStore.validateNickname(nickname.value);
});

// 닉네임 중복 확인 함수 -> userStore의 닉네임 중복 체크 함수 호출
const checkNickname = function () {
  isNickDuplicated.value = userStore.checkNickname(nickname.value)
}

// 회원가입 함수 -> userStore의 카카오 회원가입 함수 호출
const signUp = function(){
  console.log('회원가입 요청');
  const payload = ref({
    nickname: nickname.value,
    icon: userStore.myIcon
  })
  console.log(payload.value);
  userStore.kakaoSignUp(payload.value)
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
  
