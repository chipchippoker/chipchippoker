<template>
    <div class="position-absolute" style="width:80%;">
        <!-- 로고와 방목록 -->
        <div class="m-3">
            <div class="d-flex align-items-center justify-content-center gap-5" style="width: 100%;" >
                <img @click="reLoad()" type="button" class="small-logo m-0" src="/src/assets/icons/Logo.png" alt="">
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
        <button @click="friendStore.getFriendList('')" class="btn-outline-yellow fixed-left" type="button" data-bs-toggle="offcanvas" data-bs-target="#friendList" aria-controls="friendList" >
            <font-awesome-icon :icon="['fas', 'caret-left']" style="color: #ffffff;" />
        </button>

        <div  data-bs-backdrop="false" class="offcanvas offcanvas-end" tabindex="-1" id="friendList" aria-labelledby="friendListLabel">
            <div class="offcanvas-header bg-modal">
                <button  type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
            </div>
            <div class="offcanvas-body bg-modal">
                <MainFriendList/>
            </div>
        </div>
    <!-- 모달 -->

        <!-- 빠른방 입장 모달 -->
        <div data-bs-backdrop="static" class="modal fade" id="FindRoomModal" tabindex="-1" aria-labelledby="findRoomModalLabel" aria-hidden="true">
            <ModalFindRoom @showFindGame="handleShowFindGame" :type="gameType" />
        </div>

        <!-- 게임 찾는중 모달 -->
        <div data-bs-backdrop="static" class="modal fade" id="FindGameModal" tabindex="-1" aria-labelledby="findGameModalLabel" aria-hidden="true">
            <ModalFindGame @close="matchStore.stopFindGame()" />
        </div>

        <!-- 친선 방이 없는 것을 말해주는 모달 -->
        <div data-bs-backdrop="static" class="modal fade" id="NotExistRoom" tabindex="-1" aria-hidden="true">
            <ModalNotExistRoom/>
        </div>
       
        <!-- 방만들기 모달 -->
        <div data-bs-backdrop="static" class="modal fade" id="makeRoomModal" tabindex="-1" aria-labelledby="makeRoomModalLabel" aria-hidden="true">
            <ModalCreateRoom/>
        </div>

        <!-- 친구 찾기 모달 -->
        <div data-bs-backdrop="static" class="modal fade" id="FindFriendModal" tabindex="-1" aria-labelledby="FindFriendModalLabel" aria-hidden="true">
            <ModalFindFriend/>
        </div>

        <!-- 이미 있는 방 제목입니다 모달 -->
        <div data-bs-backdrop="static" class="modal fade" id="IsExistRoomModal" tabindex="-1" aria-labelledby="IsExistRoomModalLabel" aria-hidden="true">
            <ModalIsExistRoom/>
        </div>
        
    </div>
    
</template>

<script setup>
import MainFriendList from '@/components/Main/Friend/MainFriendList.vue';
import MainRank from '@/components/Main/Rank/MainRank.vue';
import MainRoom from '@/components/Main/Room/MainRoom.vue'
// 모달
import ModalFindRoom from '@/components/Modal/ModalFindRoom.vue';   // 경쟁 모드, 친선 모드 빠른 시작
import ModalFindGame from '@/components/Modal/ModalFindGame.vue';   // 게임을 찾는 중입니다.
import ModalNotExistRoom from '@/components/Modal/ModalNotExistRoom.vue';
import ModalCreateRoom from '@/components/Modal/ModalCreateRoom.vue';
import ModalFindFriend from '@/components/Modal/ModalFindFriend.vue';
import ModalIsExistRoom from '@/components/Modal/ModalIsExistRoom.vue';
import { ref,onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useFriendStore } from '@/stores/friend';
import { useSoundStore } from '@/stores/sound';
import { useUserStore } from '@/stores/user';
import { useRoomStore } from '@/stores/room';
import { useMatchStore } from '@/stores/match';
import { useGameStore } from '@/stores/game'
// import bootstrap from 'bootstrap';

const userStore = useUserStore()
const soundStore = useSoundStore()
const gameStore = useGameStore()
const friendStore = useFriendStore()
const roomStore = useRoomStore()
const matchStore = useMatchStore()
const router = useRouter()
const gameType = ref('경쟁전')
const changeType = function(type){
    roomStore.isWatcher = false
    gameType.value = type
}

// -------------------모달 테스트-------------------
const handleShowFindGame = function (payload) {
    console.log('빠른 모드 탐색 시작');
    if (gameType.value === '경쟁전') {
        const modalElement = document.getElementById('FindGameModal'); // 모달의 HTML 요소 가져오기
        const modalInstance = new bootstrap.Modal(modalElement); // 모달 인스턴스 생성
        modalInstance.show(); // 모달 열기
        matchStore.matchCompete(payload)
    } else {
        matchStore.matchFriend(payload)
        .then(result => {
            if (!result) {
                const notExistRoomModal = new bootstrap.Modal(document.getElementById('NotExistRoom'));
                notExistRoomModal.show();
            }
        })
    }
}
// ------------------------------------------------


// 방을 생성할 때 때 이미 있는 방제면
watch(() => roomStore.isRoom, (newValue) => {
    console.log(roomStore.isRoom)
    if (newValue) {
        // const IsExistRoomModal = new bootstrap.Modal(document.getElementById('IsExistRoomModal'));
        // IsExistRoomModal.show()
        roomStore.isRoom = false
    }
})


// 메인 페이지 프로필 눌렀을 때 새로고침
const reLoad = function() {
    router.go(0)
}

onMounted(()=>{
    friendStore.getAllRankList()
    // friendStore.getMyRankList()
    // soundStore.bgmOn()
    // location.reload()
    gameStore.connectHandler()
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