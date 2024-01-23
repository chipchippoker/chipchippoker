<template>
    <div class="position-absolute" style="width:80%;">
        <!-- 로고와 방목록 -->
        <div class="m-3">
            <div class="d-flex align-items-center justify-content-center gap-5" style="width: 100%;" >
                <img class="small-logo m-0" src="/src/assets/icons/Logo.png" alt="">
                <div class="d-flex gap-3">
                    <button @click="changeType('경쟁전')" class="btn-outline-yellow p-1 rounded-2" data-bs-toggle="modal" data-bs-target="#FindRoomModal">경쟁 모드</button>
                    <button @click="changeType('친선전')" class="btn-outline-yellow p-1 rounded-2" data-bs-toggle="modal" data-bs-target="#FindRoomModal">친선 모드</button>
                    <button class="btn-outline-yellow p-1 rounded-2" data-bs-toggle="modal" data-bs-target="#makeRoomModal">방 만들기</button>
                    <button class="btn-outline-yellow p-1 rounded-2" data-bs-toggle="modal" data-bs-target="#FindFriendModal">친구 찾기</button>
                </div>
            </div>
        </div>
        
        <div class="d-flex">
            <!-- 랭킹 -->
            <div>
                <MainRank class="mainstyle m-3"/>
            </div>
            <!-- 방목록 -->
            <div>
                <MainRoom class="mainstyle m-3 px-2 pt-3 pb-0"/>
            </div>
        </div>

        <!-- 친구 목록 -->
        <button class="btn-outline-yellow fixed-left" type="button" data-bs-toggle="offcanvas" data-bs-target="#friendList" aria-controls="friendList" >
            <font-awesome-icon :icon="['fas', 'caret-left']" style="color: #ffffff;" />
        </button>

        <div class="offcanvas offcanvas-end" tabindex="-1" id="friendList" aria-labelledby="friendListLabel">
        <div class="offcanvas-header bg-modal">
            <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body bg-modal">
            <MainFriendList/>
        </div>

    </div>
    <!-- 모달 -->

        <!-- 빠른방 입장 모달 -->
        <div class="modal fade" id="FindRoomModal" tabindex="-1" aria-labelledby="makeRoomModalLabel" aria-hidden="true">
            <ModalFindRoom
            :type="gameType"
            />
        </div>
       
        <!-- 방만들기 모달 -->
        <div class="modal fade" id="makeRoomModal" tabindex="-1" aria-labelledby="makeRoomModalLabel" aria-hidden="true">
            <ModalCreateRoom/>
        </div>
        <!-- 친구 찾기 모달 -->
        <div class="modal fade" id="FindFriendModal" tabindex="-1" aria-labelledby="FindFriendModalLabel" aria-hidden="true">
            <ModalFindFriend/>
        </div>
        

    </div>
    
</template>

<script setup>
import MainFriendList from '@/components/Main/Friend/MainFriendList.vue';
import MainRank from '@/components/Main/Rank/MainRank.vue';
import MainRoom from '@/components/Main/Room/MainRoom.vue'
import ModalCreateRoom from '@/components/Modal/ModalCreateRoom.vue';
import ModalFindFriend from '@/components/Modal/ModalFindFriend.vue';
import ModalFindRoom from '@/components/Modal/ModalFindRoom.vue';
import { useFriendStore } from '@/stores/friend';

import { ref,onMounted } from 'vue';

const gameType = ref('경쟁')
const changeType = function(type){
    gameType.value = type
}
onMounted(()=>{
    // const friendStore = useFriendStore()
    // friendStore.getAllRankList()
    // friendStore.getFriendRankList()
    // friendStore.getMyRankList()
})
</script>

<style scoped>


.btn-outline-weightyellow {
    height: 30px;
    display: flex;
    align-items: center;
}
.fixed-left {
    position: fixed;
    right: 0;
    top: 100px;
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
    border-top-left-radius: 5px;
    border-bottom-left-radius: 5px;
    background-color: #ffde76;    
    height: 100px;
}

</style>