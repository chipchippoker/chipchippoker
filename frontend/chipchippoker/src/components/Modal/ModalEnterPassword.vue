<template>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content " style="background-color: #fff0c0;">
        <div class="modal-header border-0">
            <h1 class="modal-title fs-5" :id="`EnterPWModalLabel${roomData?.roomId}`">{{ roomData?.title }}</h1>
            <button @click="deletePW()" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body d-flex justify-content-center">
            <form class="d-flex align-items-center gap-3">
                <div>
                    비밀번호
                </div>
                <div>
                    <input v-model="password" type="password" class="form-control" :id="`password${roomData?.roomId}`" autocomplete="off">
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

const props = defineProps({
    roomData:Object,
    userType:String
})

const deletePW = function(){
    password.value = null
}
const roomStore = useRoomStore()
const enterRoomPrivate = function () {
    const payload = {
        title: roomData.title,
        password: password.value
    }
    if (props.userType === '플레이어') {
        roomStore.isWatcher = false
        roomStore.enterRoomPrivate(payload)
    } else if (props.userType === '관전자' && roomStore.state === '대기') {
        roomStore.isWatcher = true
        roomStore.watchersNickname.push(userStore.myNickname)
        roomStore.enterWatchPrivate(payload)
    } else {
        roomStore.isWatcher = true
        roomStore.enterWatchPrivateProgress(payload)
    }
    deletePW()
}



</script>

<style lang="scss" scoped>

</style>