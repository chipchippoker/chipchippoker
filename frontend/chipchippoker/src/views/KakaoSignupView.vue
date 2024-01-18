<template>

  <div>
    <div class="d-flex flex-column justify-content-center align-items-center position-absolute top-50 start-50 translate-middle">
    <div class="d-flex justify-content-center">
      <img class="big-logo" src="/src/assets/Logo.png" alt="">
    </div>
    
    <div class="formstyle">
      <div class="h2 fw-bold">회원가입</div>
      <form style="width: 400px;">
        <div class="mb-3">
          <label for="nickName" class="form-label">닉네임</label>
          <div class="input-group mb-3">
            <input v-model="nickName" type="text" class="form-control" id="nickName" placeholder="nickName">
            <button @click="checkNickName" class="btn btn-outline-secondary" type="button" id="check_nickName">중복 확인</button>
          </div>
          <div v-if="isNickDuplicated" class="form-text text-danger">이미 사용 중인 닉네임입니다.</div>
          <div v-if="!isValidNickname" id="nickname" class="fw-lgitighter little-text text-danger">한글 또는 영어 또는 숫자 또는 (_)의 4 ~ 16 글자이어야 합니다.</div>
        </div>
        <div class="fw-lgitighter little-text">
        ※ 닉네임은 4글자 ~ 16글자 사이의 한글, 영어, 숫자, 특수문자 ( _ )로 이루어져야 합니다.
        </div>

        <!-- 아이콘 모달 버튼 -->
        <div class="d-flex justify-content-center">
          <img data-bs-toggle="modal" data-bs-target="#IconModal" class="small-icon" :src='userStore.getIconUrl(userStore.myIcon)' :alt="userStore.myIcon">
        </div>


        <!-- 닉네임 중복 체크를 통과해야만 보임 -->
        <div class="d-grid gap-2 pt-3">
          <button @click="signUp" type="submit" class="btn btn-outline-yellow">회원가입</button>
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
import { useRouter } from "vue-router";
import ModalIconList from "@/components/Modal/ModalIconList.vue";

const userStore = useUserStore()
const isNickDuplicated = ref(null)
const isValidNickname = ref(true)
const router = useRouter()

// 닉네임 변수
const nickName = ref('')

watch([nickName], () => {
isValidNickname.value = userStore.validateNickName(nickName.value);
});


// 닉네임 중복 확인 함수 -> userStore의 닉네임 중복 체크 함수 호출
const checkNickName = function () {
  userStore.checkNickName(nickName.value);
  isNickDuplicated.value = userStore.isNickDuplicated
}

// 회원가입 함수 -> userStore의 카카오 회원가입 함수 호출
const signUp = function(){
  console.log('카카오 회원가입 후 메인페이지로 이동')
  
  // userStore.kakaoSignUp()
  // 메인페이지로 이동
  router.push({name:'main'})
}



</script>

<style scoped>
@import '@/assets/color.css';
@import '@/assets/size.css';
</style>
  
