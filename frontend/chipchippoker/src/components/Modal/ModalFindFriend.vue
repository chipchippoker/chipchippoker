<template>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content " style="background-color: #fff0c0;">
            <!-- 닫힘버튼 -->
        <div class="modal-header border-0 position-relative">
            <h1 class="position-absolute top-50 start-50 translate-middle modal-title fs-5 mt-2">친구 찾기</h1>
            <button @click="dataRefresh" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body d-flex justify-content-center align-items-center">
            <div class="row g-3 align-items-center">
                <!-- 모달 내용 -->
                <div class="col-auto">
                    <label for="userId" class="visually-hidden">nickname</label>
                    <input v-model="nickname" type="text" class="form-control" id="userId" placeholder="Nickname">
                </div>
                <div class="col-auto">
                    <button @click="findNickName" type="submit" class="btn-outline-yellow rounded-2">검색</button>
                </div>
            </div>
        </div>
        
        <div class="text-center mb-3 ">
            <div v-if="Object.keys(friendStore.searchedPerson).length">
                <ModalFindFriendItem
                :item="friendStore.searchedPerson"/>
                </div>

            <div v-else>검색 결과 없음</div>
        </div>
        
        </div>
    </div>
</template>

<script setup>
import ModalFindFriendItem from './ModalFindFriendItem.vue';
import { onMounted, ref } from 'vue';
import { useFriendStore } from '@/stores/friend'
import { useRouter } from 'vue-router'


const friendStore = useFriendStore()
const nickname = ref(null)
const findNickName = function(){
    console.log(`${nickname.value} 닉네임 검색`)
    friendStore.findFriendinAll(nickname.value)
}

// 모달 닫을때 데이터 청소
const dataRefresh = function(){
    friendStore.searchedPerson = {}
    nickname.value = null
}

</script>

<style lang="scss" scoped>

</style>