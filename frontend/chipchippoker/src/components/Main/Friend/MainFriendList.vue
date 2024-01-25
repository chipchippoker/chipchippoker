<template>
    <div>
        
        <h4 class="text-center fw-bold mb-3">친구목록</h4>
        <div class="row g-3 d-flex align-items-center mb-3">
                <div class="col-auto">
                    <div for="staticEmail2" class="visually-hidden">닉네임</div>
                    
                </div>
                <div class="col-auto">
                    <label for="friendId" class="visually-hidden">nickname</label>
                    <input v-model="nickname" type="text" class="form-control " id="friendId" placeholder="Nickname">
                </div>
                <div class="col-auto">
                    <button @click="findNickname()" type="submit" class="btn-outline-yellow rounded-2">검색</button>
                </div>
            </div>
        
        <MainFriendItem v-for="item in friendStore.friendList" :key="item.name"
        :item="item"/>
        <!-- 친구목록이 비었다면 -->
        <div v-if="friendStore.friendList?.length == 0" class="d-flex justify-content-center">
        검색결과 없음
        </div>
    </div>
</template>

<script setup>
import MainFriendItem from './MainFriendItem.vue';
import { useFriendStore } from '@/stores/friend';
import { ref } from 'vue';

const friendStore = useFriendStore()
const nickname = ref('')

const findNickname = function(){
    console.log(`${nickname.value} 닉네임 검색 친구`)
    friendStore.getFriendList(nickname.value)
}

</script>

<style lang="scss" scoped>

</style>