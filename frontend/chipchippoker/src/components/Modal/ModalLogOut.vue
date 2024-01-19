<template>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content " style="background-color: #ffde76;">
        <div class="modal-header border-0">

            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body d-flex flex-column justify-content-center align-items-center gap-5">
            <h3>로그아웃 하시겠습니까?</h3>
            <div class="d-flex gap-5">
                <button class="btn btn-outline-secondary" @click="logOut">로그아웃</button>
                <button class="btn btn-primary"  data-bs-dismiss="modal" aria-label="Close">돌아가기</button>
            </div>
        </div>
        
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useUserStore } from '@/stores/user';
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const logOut = function(){
    console.log('로그아웃')
    // userStore.logOut()
}

</script>

<style lang="scss" scoped>

</style>