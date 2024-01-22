<template>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content " style="background-color: #fff0c0;">
        <div class="modal-header border-0">
            <h1 class="modal-title fs-5" id="EnterPWModalLabel">{{ roomData?.title }}</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body d-flex justify-content-center">
            <div class="d-flex align-items-center gap-3">
                <div>
                    비밀번호
                </div>
                <div>
                    <input v-model="password" type="password" class="form-control" id="password" >
                </div>
                <div >
                    <button @click="enterRoom" type="submit" class="btn-outline-yellow rounded-2">입력하기</button>
                </div>
            </div>
        </div>
        
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useFriendStore } from '@/stores/friend'
import { useRouter } from 'vue-router'

const friendStore = useFriendStore()
const password = ref(null)
const enterRoom = function(){
    console.log(`${password.value} 비밀번호 입력`)
}
defineProps({
    roomData:Object
})

</script>

<style lang="scss" scoped>

</style>