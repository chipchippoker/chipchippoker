<template>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content pb-3" style="background-color: #fff0c0;">
            <div class="modal-header border-0 position-relative">
                <div class="position-absolute top-50 start-50 translate-middle modal-title mt-2 fs-5" id="FindFriendModalLabel">{{ type }}</div>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body d-flex justify-content-center">

                <!-- 인원수 선택 -->
                <div class="d-flex align-items-center gap-3">
                    <div class="form-check ">
                        <input v-model="checkedOptions" class="form-check-input " type="radio" value="2" id="two">
                        <label class="form-check-label" for="two">
                            2인
                        </label>
                    </div>
                    <div class="form-check ">
                        <input v-model="checkedOptions" class="form-check-input " type="radio" value="3" id="three">
                        <label class="form-check-label" for="three">
                            3인
                        </label>
                    </div>
                    <div class="form-check ">
                        <input v-model="checkedOptions" class="form-check-input " type="radio" value="4" id="four">
                        <label class="form-check-label" for="four">
                            4인
                        </label>
                    </div>

                    <button type="button" class="btn-outline-yellow rounded-2" @click="showFindGameModal">찾기</button>

                    
                </div>
            </div>
        </div>
        <!-- 게임 찾기 모달 -->
        <div class="modal fade" id="FindGame" tabindex="-1" aria-hidden="true">
            <ModalFindGame
            @close="stopFindGame()"/>
        </div>
        <!-- 친선 방이 없는 것을 말해주는 모달 -->
        <div class="modal fade" id="NotExistRoom" tabindex="-1" aria-hidden="true">
            <ModalNotExistRoomVue/>
        </div>
    </div>
</template>

<script setup>
import ModalFindGame from '@/components/Modal/ModalFindGame.vue';
import ModalNotExistRoomVue from './ModalNotExistRoom.vue';
import { useMatchStore } from '@/stores/match';
import { isMemoSame, ref } from 'vue';
import { useRouter } from 'vue-router';

const matchStore = useMatchStore()
const router = useRouter()

const props = defineProps({
    type:String
})

const checkedOptions = ref('2'); // 초기 상태 설정

const showFindGameModal = function () {
    const findGameModal = new bootstrap.Modal(document.getElementById('FindGame'));
    findGameModal.show()
    
    // 3초 동안은 게임 찾기
    setTimeout(() => {
      console.log(checkedOptions.value);
        const payload = {
            totalParticipantCnt: Number(checkedOptions.value)
        }
        console.log(payload);
        if (props.type === '경쟁전') {
            matchStore.matchCompete(payload)
            // 매치 잡히면 넘어가기
            if (matchStore.isMatch === true) {
                findGameModal.hide()
                router.push({
                    name:'play',
                    params: { roomId: matchStore.roomId },
                    state: {
                    title: matchStore.title,
                    totalParticipantCnt: matchStore.totalParticipantCnt
                    }
                })
            }
        } else {
            matchStore.matchFriend(payload)
            // 매치 잡히면 넘어가기
            if (matchStore.isMatch === true) {
                findGameModal.hide()
                console.log(payload)
                router.push({
                    name:'wait',
                    params: { roomId: matchStore.roomId },
                    state: {
                    title: matchStore.title,
                    totalParticipantCnt: matchStore.totalParticipantCnt
                    }
                })
            } else if (matchStore.isMatch === false)  { // 생성된 친선 방이 없으면
                findGameModal.hide()
                const notExistRoom = new bootstrap.Modal(document.getElementById('NotExistRoom'));
                notExistRoom.show()
            }
        }
    }, 3000)
}


const stopFindGame = function() {
    matchStore.stopFindGame()
}
</script>

<style lang="scss" scoped></style>