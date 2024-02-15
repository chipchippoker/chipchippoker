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
                        <input v-model="checkedOptions" class="form-check-input " type="radio" value=2 id="two2">
                        <label class="form-check-label" for="two2">
                            2인
                        </label>
                    </div>
                    <div class="form-check ">
                        <input v-model="checkedOptions" class="form-check-input " type="radio" value=3 id="three3">
                        <label class="form-check-label" for="three3">
                            3인
                        </label>
                    </div>
                    <div class="form-check ">
                        <input v-model="checkedOptions" class="form-check-input " type="radio" value=4 id="four4">
                        <label class="form-check-label" for="four4">
                            4인
                        </label>
                    </div>

                    <!-- <button type="button" class="btn-outline-yellow rounded-2" @click="showFindGameModal" >찾기</button> -->
                    <button type="button" class="btn-outline-yellow rounded-2" @click="showFindGameModal" data-bs-dismiss="modal" aria-label="Close">찾기</button>
                </div>
            </div>
        </div>
        <!-- 게임 찾기 모달 -->
        <!-- <div data-bs-backdrop="static" class="modal" id="FindGame" tabindex="-1">
            <ModalFindGame
            @close="stopFindGame()"/>
        </div> -->
        <!-- 친선 방이 없는 것을 말해주는 모달 -->
        <!-- <div data-bs-backdrop="static" class="modal fade" id="NotExistRoom" tabindex="-1" aria-hidden="true">
            <ModalNotExistRoom/>
        </div> -->
    </div>
</template>

<script setup>
import ModalFindGame from '@/components/Modal/ModalFindGame.vue';
import ModalNotExistRoom from './ModalNotExistRoom.vue';
import { isMemoSame, ref, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useGameStore } from '@/stores/game'
import { useMatchStore } from '@/stores/match';
import { useRoomStore } from '@/stores/room';

const matchStore = useMatchStore()
const gameStore = useGameStore()
const roomStore = useRoomStore()
const router = useRouter()

const props = defineProps({
    type:String
})

const checkedOptions = ref(2); // 초기 상태 설정

const emit = defineEmits(['showFindGame'])
// 모달 테스트
const showFindGameModal = function () {
  const payload = {
    totalParticipantCnt: checkedOptions.value
  };
  emit('showFindGame', payload); // 이벤트 발생
};
</script>

<style lang="scss" scoped></style>