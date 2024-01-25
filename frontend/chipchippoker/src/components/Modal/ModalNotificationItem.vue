<template>
    <div class="bg-white rounded-1 d-flex justify-content-between align-items-center px-3 py-1 " >
        <div class="d-flex">
        <div class="overflow-hidden text-overflow" style="width:100px">
        {{ item?.nickname }}
        </div>
        <div>님의 친구 요청</div>
        </div>  
        <div class="d-flex gap-1">
            <div @click="accept(item?.nickname)" class="btn btn-outline-primary py-0 px-1">수락</div>
            <div @click="reject(item?.nickname)" class="btn btn-outline-danger py-0 px-1">거절</div>
        </div>

    </div>
</template>

<script setup>
defineProps({
    item:Object
})

import { useFriendStore } from '@/stores/friend';
const friendStore = useFriendStore()

// 수락 거절
const accept = function (nickname){
    console.log(`${nickname}의 신청을 수락합니다`)
    friendStore.acceptFriendRequest(nickname)
}
const reject = function (nickname){
    console.log(`${nickname}의 신청을 거절합니다`)
    friendStore.rejectFriendRequest(nickname)
}

</script>

<style scoped>

</style>