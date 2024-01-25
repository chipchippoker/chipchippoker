<template>
    <div class="bg-white rounded-1 d-flex justify-content-between align-items-center px-3 py-1 " >
        <div class="d-flex">
        <div class="overflow-hidden text-overflow" style="width:100px">
        {{ item?.nickname }}
        </div>
        <div>님의 친구 요청</div>
        </div>  
        <div v-if="isAccepted===false && isRejected===false" class="d-flex gap-1">
            <div @click="accept(item?.nickname)" class="btn btn-outline-primary py-0 px-1">수락</div>
            <div @click="reject(item?.nickname)" class="btn btn-outline-danger py-0 px-1">거절</div>
        </div>
        <!-- 버튼 누르고 바뀜 -->
        <div v-if="isAccepted===true" class="bg-secondary-subtle p-1 rounded-3">
            수락하였습니다
        </div>
        <div v-if="isRejected===true" class="bg-secondary-subtle p-1 rounded-3">
            거절하였습니다
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
defineProps({
    item:Object
})
const isAccepted = ref(false)
const isRejected = ref(false)
import { useFriendStore } from '@/stores/friend';
const friendStore = useFriendStore()

// 수락 거절
const accept = function (nickname){
    console.log(`${nickname}의 신청을 수락합니다`)
    isAccepted.value=true
    friendStore.acceptFriendRequest(nickname)
}
const reject = function (nickname){
    console.log(`${nickname}의 신청을 거절합니다`)
    isRejected.value=true
    friendStore.rejectFriendRequest(nickname)
}

</script>

<style scoped>

</style>