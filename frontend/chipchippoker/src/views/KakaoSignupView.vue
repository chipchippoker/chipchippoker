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
            <div v-if="nickname!=='' && !isValidNickname" id="nickname" class="fw-lgitighter x-little-text text-danger">한글 또는 영어 또는 숫자 또는
              (_)의 2 ~ 12 글자이어야 합니다.</div>
            <div v-if="nickname!=='' && isNickDuplicated===null" id="nickname" class="fw-lgitighter x-little-text text-danger">닉네임 중복확인을 해주세요.</div>
            <div v-if="isValidNickname && isNickDuplicated===false" id="id" class="fw-lgitighter x-little-text text-primary">사용 가능한 닉네임입니다.</div>
          </div>

          <!-- 아이콘 모달 버튼 -->
          <div class="d-flex justify-content-center">
            <div type="button" class="btn-transparency">
              <img data-bs-toggle="modal" data-bs-target="#IconModal" class="small-icon" :src='userStore.getIconUrl(userStore.myIcon)' :alt="userStore.myIcon">
            </div>
          </div>

          <!-- 닉네임 중복 체크를 통과해야만 보임 -->
          <div class="d-grid gap-2 pt-3">
            <button :disabled="!(isValidNickname && isNickDuplicated === false)" type="submit" class="btn btn-primary btn-login">회원가입</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 아이콘 모달 팝업 -->
    <div data-bs-backdrop="static" class="modal fade" id="IconModal" tabindex="-1" aria-labelledby="IconModalLabel" aria-hidden="true">
      <ModalIconList/>
    </div>
  </div>
</template>
  
<script setup>
import { ref,watch } from "vue";
import { useUserStore } from "@/stores/user";
import ModalIconList from "@/components/Modal/ModalIconList.vue";
import { icon } from "@fortawesome/fontawesome-svg-core";

const userStore = useUserStore()
const isNickDuplicated = ref(null)
const isValidNickname = ref(true)
const nickname = ref('')

watch([nickname], () => {
  isValidNickname.value = userStore.validateNickname(nickname.value);
  // 입력값이 변경될 때마다 중복 확인 상태 초기화
  isNickDuplicated.value = null;
});

// 닉네임 중복 확인
const checkNickname = function () {
  userStore.checkNickname(nickname.value)
  .then(result => {
    isNickDuplicated.value = result
  })
  .catch(err => alert(err))
}

// 회원가입 함수 -> userStore의 카카오 회원가입 함수 호출
const signUp = function(){
  const payload = ref({
    nickname: nickname.value,
    icon: userStore.myIcon
  })
  userStore.kakaoSignUp(payload.value)
  .then(result => {
    if (result) {
      nickname.value = null
      userStore.myIcon = '1'
    } else {
      alert("회원가입 실패했습니다.")
    }
  })
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
  
