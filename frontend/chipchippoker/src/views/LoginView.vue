<template>
    <div class="d-flex flex-column justify-content-center align-items-center bg-darkblue">
        <div class="d-flex justify-content-center">
            <img class="big-logo" src="/src/assets/Logo.png" alt="">
        </div>
        <div class="formstyle">
            <div class="fs-2 fw-bold mb-3 px-0">로그인</div>
            <!-- <div> -->
                <form action="" class="d-flex flex-column justify-content-center login-form px-2 mx-1" style="width: 300px;">
                    <div class="mb-3 mx-0" style="width: 100%;">
                        <label for="username" class="form-label">아이디</label>
                        <input type="text" class="form-control" id="username" placeholder="아이디"> 
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">비밀번호</label>
                        <input type="password" class="form-control" id="password" placeholder="비밀번호">
                    </div>
                    <div class="mb-3">   
                        <button type="submit" class="btn btn-primary btn-login">로그인</button>
                    </div>
                    <div>
                        <!-- <img src="/src/assests/icons/kakao.png" alt=""> -->
                        <button type="button" class="btn btn-secondary btn-kakao-login">카카오로그인</button>
                    </div>
                </form>
            <!-- </div> -->
        </div>
    </div>
</template>

<script setup>
import { ref } from "vue";
// import { useUserStore } from "@/stores/user";
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
@import "@/assets/color.css";
@import "@/assets/size.css";
.container {
    background-color: #99A5C1;
    color: white;
    width: 300px;
    margin: 20px;
    border-radius: 20px;
}
.login-form {
    max-width: 300px;
}

label {
    color: black;
}

.btn-login {
    box-shadow: 0 0 0 3px #ffffff inset;
    border: 0px;
    background-color: #99A5C1;
    width: 100%;
}

.btn-kakao-login {
    width: 100%;
    background-color: #F7E409;
    color: black;
    border: 0;
    font-weight: bold;
}
</style>
