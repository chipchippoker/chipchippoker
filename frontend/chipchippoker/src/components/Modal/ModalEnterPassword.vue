<template>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content " style="background-color: #fff0c0;">
        <div class="modal-header border-0">
            <h1 class="modal-title fs-5" id="EnterPWModalLabel">{{ roomData?.title }}</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body d-flex justify-content-center">
            <form class="d-flex align-items-center gap-3">
                <div>
                    비밀번호
                </div>
                <div>
                    <input v-model="password" type="password" class="form-control" id="password" autocomplete="off">
                </div>
                <div >
                    <button @click="enterRoomPrivate()" type="submit" class="btn-outline-yellow rounded-2">입장하기</button>
                </div>
            </form>
        </div>
        
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useFriendStore } from '@/stores/friend'
import { useRouter } from 'vue-router'
import { useRoomStore } from '@/stores/room';


const friendStore = useFriendStore()
const password = ref(null)

defineProps({
    roomData:Object
})

const roomStore = useRoomStore()
const enterRoomPrivate = function () {
    const payload = {
        title: roomData.title,
        password: password
    }
    roomStore.enterRoomPrivate(payload)
}

</script>

<style lang="scss" scoped>

</style>