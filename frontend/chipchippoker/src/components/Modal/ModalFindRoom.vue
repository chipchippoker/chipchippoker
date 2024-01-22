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
    </div>
</template>

<script setup>
defineProps({
    type:String
})
</script>

<style lang="scss" scoped></style>