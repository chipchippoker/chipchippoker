<template>
    <div class="container">
        <div class="row col-12" id="room-item">
            <!-- 공개 여부, 방 제목, 참여 인원, 관전 인원 -->
            <div class="row col-9">
                
                <!-- 방 제목 -->
                <div class="col-12">
                    <div class="not-overflow-text"><strong>{{ item?.title }}</strong></div>
                </div>

                <!-- 방 정보 -->
                <div class="row col-12">

                    <!-- 공개 여부 -->
                    <div class="col-12 d-flex justify-content-between">
                        <div v-if="item?.isPrivate" class="x-little-text">비공개({{ item?.state }}중)</div>
                        <div v-else class="x-little-text">공개({{ item?.state }}중)</div>
                    </div>

                    <!-- 참여 인원, 관전 인원 -->
                    <div class="col-12">
                        <div v-if="roomStore.roomType == '친선'">
                            <div class="x-little-text not-overflow-text">
                                참여 인원 : {{ item?.currentParticipantCnt }}/{{ item?.totalParticipantCnt }}  
                                관전 인원 : {{ item?.currentSpectatorCnt
                                }}/6
                                 
                            </div>
                        </div>
                        <div v-else>
                            <div class="x-little-text">
                                참여 인원 : {{ item?.currentParticipantCnt }}/{{ item?.totalParticipantCnt }}
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 입장, 관전 버튼 -->
            <div class="row col-4 pe-0" v-if="roomStore.roomType == '친선'">
                <div class="col-12 m-0 p-0">
                    <!-- 친선일 때만 보이기 -->
                    <button v-if="item?.isPrivate" @click="showEnterPWModal('플레이어')"
                        class="btn-2" :disalbed="item?.currentParticipantCnt === item?.totalParticipantCnt"
                        :class="{ 'btn-2-red-done': item?.currentParticipantCnt === item?.totalParticipantCnt, 'btn-2-red': item?.currentParticipantCnt !== item?.totalParticipantCnt }">
                        입장
                    </button>
                    <button v-else @click="enterRoomPublic(item.title, '플레이어')"
                        class="btn-2 btn-2-red" :disalbed="item?.currentParticipantCnt === item?.totalParticipantCnt"
                        :class="{ 'btn-2-red-done': item?.currentParticipantCnt === item?.totalParticipantCnt, 'btn-2-red': item?.currentParticipantCnt !== item?.totalParticipantCnt }">
                        입장
                    </button>
                </div>
                <div class="col-12 m-0 p-0">
                    <button v-if="item?.isPrivate" @click="showEnterPWModal('관전자')"
                        class="btn-2"
                        :class="{ 'btn-2-blue-done': item?.currentParticipantCnt === item?.totalParticipantCnt, 'btn-2-blue': item?.currentParticipantCnt !== item?.totalParticipantCnt }">
                        관전
                        <!-- <font-awesome-icon :icon="['fas', 'caret-right']" style="color: #8f8f8f;" /> -->
                    </button>
                    <button v-else @click="enterRoomPublic(item.title, '관전자')"
                        class="btn-2 btn-2-blue"
                        :class="{ 'btn-2-blue-done': item?.currentParticipantCnt === item?.totalParticipantCnt, 'btn-2-blue': item?.currentParticipantCnt !== item?.totalParticipantCnt }">
                        관전
                        <!-- <font-awesome-icon :icon="['fas', 'caret-right']" style="color: #8f8f8f;" /> -->
                    </button>
                </div>
            </div>

            <!-- 입장 비밀번호 모달 -->
            <div data-bs-backdrop="false" class="modal fade" :id="`EnterPWModal${item?.roomId}`" tabindex="-1"
                :aria-labelledby="`EnterPWModal${item?.roomId}`" aria-hidden="true">
                <ModalEnterPassword :room-data="item" :user-type="userType" />
            </div>
            <!-- 게임 진행 중 모달 -->
            <div data-bs-backdrop="false" class="modal fade" :id="`IsPlayingModal${item?.roomId}`" tabindex="-1"
                :aria-labelledby="`IsPlayingModal${item?.roomId}`" aria-hidden="true">
                <ModalIsPlayingRoomVue />
            </div>
            <!-- 꽉찬 방 모달 -->
            <div data-bs-backdrop="false" class="modal fade" :id="`IsFullModal${item?.roomId}`" tabindex="-1"
                :aria-labelledby="`IsFullModal${item?.roomId}`" aria-hidden="true">
                <ModalIsFullRoomVue />
            </div>
        </div>
    </div>
</template>

<script setup>
import ModalEnterPassword from '@/components/Modal/ModalEnterPassword.vue';
import ModalIsPlayingRoomVue from '../../Modal/ModalIsPlayingRoom.vue';
import ModalIsFullRoomVue from '../../Modal/ModalIsFullRoom.vue';
import { useRoomStore } from '@/stores/room';
import { useUserStore } from '@/stores/user'
import { ref } from "vue";

const userStore = useUserStore()
const roomStore = useRoomStore()

const props = defineProps({
    item: Object
})
const userType = ref('')

const enterRoomPublic = function (title, type) {
    const payload = {
        title: title
    }
    userType.value = type
    roomStore.roomState = props.item.state
    // 꽉찬 방이면
    if (type === '관전자' && props.item?.currentSpectatorCnt === 6) {
        const isFullModal = new bootstrap.Modal(document.getElementById(`IsFullModal${props.item?.roomId}`));
        isFullModal.show()
    } else {

        if (type === '플레이어') {
            roomStore.isWatcher = false
            // 게임 진행 중이면
            if (props.item.state === '진행') {
                const isPlayingModal = new bootstrap.Modal(document.getElementById(`IsPlayingModal${props.item?.roomId}`));
                isPlayingModal.show()
            } else if (props.item?.currentParticipantCnt === props.item?.totalParticipantCnt) { // 플레이어 꽉 찬 방이면
                const isFullModal = new bootstrap.Modal(document.getElementById(`IsFullModal${props.item?.roomId}`));
                isFullModal.show()
            } else {
                roomStore.enterRoom(payload)
            }
        } else { // 관전자
            roomStore.isWatcher = true
            roomStore.watchersNickname.push(userStore.myNickname)
            roomStore.enterWatch(payload)
        }

    }
}

const showEnterPWModal = function (type) {
    roomStore.roomState = props.item.state
    userType.value = type
    // 꽉찬 방이면
    if (type === '관전자' && props.item?.currentSpectatorCnt === 6) {
        const isFullModal = new bootstrap.Modal(document.getElementById(`IsFullModal${props.item?.roomId}`));
        isFullModal.show()
    } else {

        // 플레이어고 진행중이면 진행 중인 게임 들어가지 못하게
        if (type === '플레이어' && props.item.state === '진행') {
            roomStore.isWatcher = false
            const isPlayingModal = new bootstrap.Modal(document.getElementById(`IsPlayingModal${props.item?.roomId}`));
            isPlayingModal.show()
        } else if (type === '플레이어' && props.item?.currentParticipantCnt === props.item?.totalParticipantCnt) { // 플레이어 꽉 찬 방이면
            const isFullModal = new bootstrap.Modal(document.getElementById(`IsFullModal${props.item?.roomId}`));
            isFullModal.show()
        } else {
            const enterPWModal = new bootstrap.Modal(document.getElementById(`EnterPWModal${props.item?.roomId}`));
            enterPWModal.show()
        }

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
/* backdrop */
.not-overflow-text {
    overflow: hidden;
    /* 텍스트 오버플로우를 숨김 */
    text-overflow: ellipsis;
    /* 텍스트 오버플로우시 말줄임(...) 표시 */
    white-space: nowrap;
    /* 텍스트가 넘치면 줄바꿈을 하지 않음 */
}

#room-item {
    background-color: white;
    padding: 10px;
    margin: 5px 10px 10px 0px;
    border-radius: 10px;
}

#room-item:hover {
    background-color: rgb(206, 200, 200);
    box-shadow: inset;
}
</style>