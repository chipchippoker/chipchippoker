<template>
    <div class="bg-light p-2 rounded-2 m-1" >
        <div class="d-flex justify-content-between">
            <div v-if="item?.isPrivate" class="x-little-text">비공개({{ item?.state }}중)</div>
            <div v-else class="x-little-text">공개({{ item?.state }}중)</div>

            <div class="d-flex gap-1">
                <!-- 친선일 때만 입장 보이기 -->
                <div v-if="roomStore.roomType=='친선'">
                    <div v-if="item?.isPrivate" type="button" data-bs-toggle="modal" data-bs-target="#EnterPWModal" class="btn-lightred x-little-text rounded-1 px-1">입장</div>
                    <div v-else type="button" @click="enterRoomPublic(item.title, '플레이어')" class="btn-lightred x-little-text rounded-1 px-1">입장</div>
                </div>
                <div v-if="item?.isPrivate" type="button" data-bs-toggle="modal" data-bs-target="#EnterPWModal" class="btn-gray x-little-text rounded-1 px-1">관전
                    <font-awesome-icon :icon="['fas', 'caret-right']" style="color: #8f8f8f;" /></div>
                <div v-else type="button" @click="enterRoomPublic(item.title, '관전자')" class="btn-gray x-little-text rounded-1 px-1">관전
                    <font-awesome-icon :icon="['fas', 'caret-right']" style="color: #8f8f8f;" />
                </div>

            </div>
        </div>
        
        <div>
            <div><strong>{{ item?.title }}</strong></div>
        </div>
        <div class="x-little-text">
             참여 인원 : {{ item?.currentParticipantCnt }}/{{item?.totalParticipantCnt}} <!--관전 인원 : {{ item?.currentSpectatorCnt }}/6 -->
        </div>


        <!-- 입장 비밀번호 모달 -->
        <div class="modal fade" id="EnterPWModal" tabindex="-1" aria-labelledby="EnterRoomModalLabel" aria-hidden="true">
            <ModalEnterPassword
            :roomData="item"/>
        </div>
    </div>
</template>

<script setup>
import ModalEnterPassword from '@/components/Modal/ModalEnterPassword.vue';
import { useRoomStore } from '@/stores/room';
import { ref } from "vue";

const roomStore = useRoomStore()

const props = defineProps({
    item:Object
})


const enterRoomPublic = function (title, type) {
    const payload = {
        title: title
    }
    if (type === '플레이어') {
        roomStore.enterRoomPublic(payload)
        roomStore.isWatcher = false
        roomStore.roomState = props.item.state
    } else {
        roomStore.enterWatchPublic(payload)
        roomStore.isWatcher = true
        roomStore.roomState = props.item.state
    }
}


// "isPrivate": false,
// "state": "대기",
// "title": "테스트 방1",
// "totalParticipantCnt": 2,
// "currentParticipantsCnt": 2,
// "currentSpectatorsCnt": 0

</script>

<style scoped>

</style>