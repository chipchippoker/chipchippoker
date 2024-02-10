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
            <!-- <div> -->
                <MainRank class="mainstyle m-3"/>
            <!-- </div> -->
            <!-- 방목록 -->
            <!-- <div> -->
                <MainRoom class="mainstyle m-3 px-2 pt-3 pb-0"/>
            <!-- </div> -->
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
        <div class="modal fade" id="FindRoomModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="findRoomModalLabel" aria-hidden="true">
            <ModalFindRoom @showFindGame="handleShowFindGame" :type="gameType" />
        </div>

        <!-- 게임 찾는중 모달 -->
        <div class="modal fade" id="FindGameModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="findGameModalLabel" aria-hidden="true">
            <ModalFindGame @closeModal="closeModalHandler"/>
        </div>

        <!-- 친선 방이 없는 것을 말해주는 모달 -->
        <div class="modal fade" id="NotExistRoom" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-hidden="true">
            <ModalNotExistRoom/>
        </div>
       
        <!-- 방만들기 모달 -->
        <div class="modal fade" id="makeRoomModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="makeRoomModalLabel" aria-hidden="true">
            <ModalCreateRoom/>
        </div>

        <!-- 친구 찾기 모달 -->
        <div class="modal fade" id="FindFriendModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="FindFriendModalLabel" aria-hidden="true">
            <ModalFindFriend/>
        </div>

        <!-- 이미 있는 방 제목입니다 모달 -->
        <div class="modal fade" id="IsExistRoomModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="IsExistRoomModalLabel" aria-hidden="true">
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
import { ref, onMounted, watch } from 'vue';
import { useRouter, onBeforeRouteLeave  } from 'vue-router';
import { useFriendStore } from '@/stores/friend'
import { useSoundStore } from '@/stores/sound'
import { useUserStore } from '@/stores/user'
import { useRoomStore } from '@/stores/room'
import { useMatchStore } from '@/stores/match'
import { useGameStore } from '@/stores/game'
import { useOpenviduStore } from '@/stores/openvidu';

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

// ===================== 게임 찾기 모달 감시=======================
const modalWatch = watch(() => matchStore.isSearching, (newVal, oldVal) => {
    const findGameModal = new bootstrap.Modal(document.getElementById('FindGameModal'))
    console.log(matchStore.isSearching)
    console.log(newVal);
    if (!newVal) {
        console.log('모달 닫기');
        // findGameModal.hide()
    } else {
        console.log('모달 열기');
        findGameModal.show()
    }
})

const closeModalHandler = function () {
    console.log('닫기 이벤트 발생!!');
    const findGameModal = new bootstrap.Modal(document.getElementById('FindGameModal'))
    findGameModal.hide()
}

// ================================================================
// -------------------모달 테스트-------------------
const handleShowFindGame = function (payload) {
    console.log('빠른 모드 탐색 시작');
    if (gameType.value === '경쟁전') {
        const modalInstance = new bootstrap.Modal(document.getElementById('FindGameModal'))
        matchStore.matchCompete(payload)
        .then(code => {
            if (code === '성공') {
                // modalInstance.show()
                console.log(matchStore.title, matchStore.totalParticipantCnt);
                gameStore.sendMatching(matchStore.title, matchStore.totalParticipantCnt)
            }
        })
    } else {
        matchStore.matchFriend(payload)
        .then(result => {
            console.log('친선전 매칭 결과', result);
            
            if (result === false) {
                const notExistRoomModal = new bootstrap.Modal(document.getElementById('NotExistRoom'));
                notExistRoomModal.show();
            } else if (result === 'FB010') {
                console.log('이미 방 입장 중')
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

// 메인페이지 떠나기 전에
onBeforeRouteLeave((to, from, next) => {
    const modalInstance = new bootstrap.Modal(document.getElementById('FindGameModal'))
    modalInstance.hide()
    // 모달 백드랍 제거
    console.log('메인페이지 떠나기 전에~~~~');
    const backdrop = document.querySelector('.modal-backdrop');
    if (backdrop) {
        console.log('백드롭 제거');
        backdrop.remove();
    }
    next(); // 다음 라우터로 이동
})

onMounted(()=>{
    gameStore.connectHandler()
    friendStore.getAllRankList()
    // friendStore.getMyRankList()
    // soundStore.bgmOn()
    console.log(roomStore.title);
    if (roomStore.isWatcher === true  && roomStore.title !== '') {
      roomStore.leaveWatcher()
    } else if (roomStore.title !== '') {
        roomStore.leaveRoom()
    }
})

</script>

<style scoped>

@media only screen and (max-width: 1000px) {
  .mainstyle {
    display: none;
  }
}
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