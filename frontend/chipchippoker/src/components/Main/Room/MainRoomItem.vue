<template>
    <div class="bg-light p-2 rounded-2 m-1" >
        <div class="d-flex justify-content-between">
            <div v-if="item?.isPrivate" class="x-little-text">비공개({{ item?.state }}중)</div>
            <div v-else class="x-little-text">공개({{ item?.state }}중)</div>

            <div class="d-flex gap-1">
                <div v-if="item?.isPrivate" data-bs-toggle="modal" data-bs-target="#EnterPWModal" class="btn-lightred x-little-text rounded-1 px-1">입장</div>
                <div v-else class="btn-lightred x-little-text rounded-1 px-1">입장</div>
                <!-- <div class="btn-gray x-little-text rounded-1 px-1">관전
                    <font-awesome-icon :icon="['fas', 'caret-right']" style="color: #8f8f8f;" />
                </div> -->
            </div>
        </div>
        
        <div>
            <div><strong>{{ item?.title }}</strong></div>
        </div>
        <div class="x-little-text">
            참여 인원 : {{ item?.currentParticipantsCnt }}/{{item?.totalParticipantsCnt}} 관전 인원 : {{ item?.currentSpectatorsCnt }}/6
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
import { ref } from "vue";
defineProps({
    item:Object
})




// "isPrivate": false,
// "state": "대기",
// "title": "테스트 방1",
// "totalParticipantsCnt": 2,
// "currentParticipantsCnt": 2,
// "currentSpectatorsCnt": 0

</script>

<style scoped>

</style>