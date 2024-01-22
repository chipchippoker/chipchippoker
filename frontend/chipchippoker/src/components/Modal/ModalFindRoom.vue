<template>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content pb-3" style="background-color: #fff0c0;">
            <div class="modal-header border-0">
                <h1 class="modal-title fs-5" id="FindFriendModalLabel">{{ type }}</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body d-flex justify-content-center">

                <!-- 인원수 선택 -->
                <div class="d-flex align-items-center gap-3">
                    <div class="form-check ">
                        <input class="form-check-input " type="checkbox" value="" id="two">
                        <label class="form-check-label" for="two">
                            2인
                        </label>
                    </div>
                    <div class="form-check ">
                        <input class="form-check-input " type="checkbox" value="" id="three">
                        <label class="form-check-label" for="three">
                            3인
                        </label>
                    </div>
                    <div class="form-check ">
                        <input class="form-check-input " type="checkbox" value="" id="four">
                        <label class="form-check-label" for="four">
                            4인
                        </label>
                    </div>

                    <button type="button" class="btn-outline-yellow rounded-2">찾기</button>

                    
                </div>
            </div>
        </div>
        <!-- 방만들기 모달 -->
        <div class="modal fade" id="FindGame" tabindex="-1" aria-hidden="true">
            <ModalFindGame/>
        </div>
    </div>
</template>

<script setup>
import ModalFindGame from '@/components/Modal/ModalFindGame.vue';


defineProps({
    type:String
})
const showFindGameModal = () => {
    // 모달을 트리거하기 위한 JavaScript 로직
    const findGameModal = new bootstrap.Modal(document.getElementById('FindGame'));
    findGameModal.show();
}
</script>

<style lang="scss" scoped></style>