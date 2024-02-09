<template>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content " style="background-color: #fff0c0;">
        <div class="modal-header border-0">

            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body d-flex flex-column justify-content-center align-items-center gap-4">
            <h3>로그아웃 하시겠습니까?</h3>
            <div class="d-flex gap-5">
                <button class="btn-2 btn-2-blue" data-bs-dismiss="modal" aria-label="Close">돌아가기</button>
                <button class="btn-2 btn-3-red" data-bs-dismiss="modal" @click="logOut">로그아웃</button>
            </div>
        </div>
        
        </div>
    </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user';

const userStore = useUserStore()
const logOut = function(){
    console.log('로그아웃')
    userStore.logOut()
}

</script>

<style lang="scss" scoped>
button {
    width: 75px;
    height: 30px;
    font-size: 17px;
    margin-bottom: 10px;
    margin-top: 0%;
}
</style>