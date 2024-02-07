<template>
  <div
    class="position-absolute container d-flex flex-column align-items-center p-3 m-0"
    style="width: 100%; height: 100%"
  >
    <!-- 옵션, 방제, 아이콘 -->
    <div
      class="d-flex justify-content-end align-items-center position-relative"
      style="width: 100%; height: 100px"
    >
      <!-- 방제 -->
      <div class="position-absolute top-50 start-50 translate-middle">
        <h3 class="room-title">{{ roomTitle }}</h3>
      </div>
    </div>

    <!--  body -->
    <div class="container row mt-5">
      <div class="col-9">
        <!-- 본인이 플레이어면 -->
        <!-- 모든 캠 -->
        <div v-if="roomStore.isWatcher===false" id="video-container">
          <div class="flex-container row g-1 p-0">
            <!-- 내 캠 -->
            <div class="col-6 mb-5"
              v-for="(player, index) in gameStore.memberInfos" :key="index">
              <div
               :class="{ 'is-ready': player.isReady }"
                style="width: 410px; height: 310px;">
                <h3 style="color: white;">{{ player.nickname }}</h3>
                <UserVideo 
                :stream-manager="findVideo(playersComputed, player.nickname)"
                :is-manager="isManager"
                :room-manager-nickname="roomManagerNickname"
                @force-disconnect="forceDisconnect"
                />
              </div>
              <!-- 다른 사람 캠 -->
              <!-- <div v-else style="width: 410px; height: 310px;"
                :class="{ 'is-ready': player.isReady }">
                <UserVideo
                  :stream-manager="findVideo(playersComputed, player.nickname)"
                  :is-manager="isManager"
                  :room-manager-nickname="roomManagerNickname"
                  @force-disconnect="forceDisconnect"
                  />
              </div> -->
            </div>
          </div>
        </div>

        <!-- 본인이 관전자면 -->
        <div v-if="roomStore.isWatcher===true" id="video-container">
          <div class="flex-container row g-1 p-0">
            <div
              class="col-6 mb-5"
              v-for="(player, index) in gameStore.memberInfos"
              :key="index">
              <div :class="{ 'is-ready': player.isReady }" style="width: 410px; height: 310px;">
                <!-- 다른 사람 캠 -->
                <UserVideo
                  :stream-manager="findVideo(playersComputed, player.nickname)"
                  :is-manager="isManager"
                  @force-disconnect="forceDisconnect"
                  />
              </div>
            </div>
          </div>
        </div>

        <!-- 캠활성화, 음소거 버튼 -->
        <!-- <button id="camera-activate" @click="handleCameraBtn">캠 비활성화</button>
        <button id="mute-activate" @click="handleMuteBtn">음소거 활성화</button> -->
      </div>
      <!-- 로고, 관전자 목록, 채팅창, 버튼 -->
      <div class="col-3">
        <!-- 로고 -->
        <div class="mb-3 me-5"><img class="small-logo m-0" src="/src/assets/icons/Logo.png" alt=""></div>
        <div class="">
          <!-- 관전자 목록 -->
          <div id="watcher-container">
            <WaitWatcher />
          </div>
          <!-- 채팅창 -->

          <!-- 나중에 <chat-winow />로 넘길수 있도록 해보자. -->
          <div id="chat-container">
            <div class="bg-lightblue rounded-4 w-100 h-100 d-flex flex-column justify-content-end">
              <ul id="messageList" class="overflow-y-auto m-0 chat_ul p-1" style="list-style-type: none;">
                <li class="my-2" v-for="(message, index) in messages.value" :key="index">
                  <strong>{{message.username}}:</strong> {{message.message}}
                </li>
              </ul>
              <div class="input-group ">
                <input class="form-control" type="text" placeholder="입력" v-model="inputMessage" @keyup.enter="sendMessage(inputMessage)">
                <button class="btn btn-outline-secondary" @click="sendMessage(inputMessage)">전송</button>
              </div>
            </div>
          </div>
        </div>
        <!-- 시작(준비), 초대, 나가기 버튼 -->
        <div class="d-flex flex-column justify-content-center align-items-center box-btns m-0 pb-4 mt-5">
          <div>
            <!-- 시작 -->
            <button v-if="myNickname === gameStore.roomManagerNickname" @click="startGame()" class="custom-btn btn-1 m-1"><span>시작해?</span><span>시작</span></button>
            <!-- 준비 -->
            <button v-else-if="myNickname !== gameStore.roomManagerNickname && isReady === false" @click="readyGame()" class="custom-btn btn-1 m-1"><span>준비해?</span><span>준비</span></button>
            <!-- 준비 취소 -->
            <button v-else-if="myNickname !== gameStore.roomManagerNickname && isReady === true" @click="readyGame()" class="custom-btn btn-1 m-1"><span>준비취소</span><span>준비완료</span></button>
            <!-- 초대 -->
            <button class="custom-btn btn-2 m-1"><span>초대해?</span><span>초대</span></button>
            <!-- 나가기 -->
            <button class="custom-btn btn-3 m-1" data-bs-toggle="modal" data-bs-target="#roomOutModal"><span>나가?</span><span>나가기</span></button>
          </div>
        </div>

        <!-- 나가기 모달 -->
        <div data-bs-backdrop="static" class="modal fade" id="roomOutModal" tabindex="-1" aria-labelledby="IconModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content" style="background-color: #ffde76;">
              <div class="modal-header border-0">
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <div class="text-center fw-bold fs-3 bg-modal-yellow">
                  정말 나가시겠습니까?
                </div>
                <div class="mt-5 d-flex justify-content-around">
                    <button class="custom-btn btn-2" data-bs-dismiss="modal" style="width: 50px;"><span>안 나가?</span><span>아니요</span></button>
                    <button class="custom-btn btn-3" data-bs-dismiss="modal" @click="leaveRoom()"><span>나가?</span><span>나가기</span></button>
                </div>
              </div>
            </div>
          </div>  
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import WaitWatcher from "@/components/Wait/WaitWatcher.vue";
import UserVideo from "@/components/Cam/UserVideo.vue";

import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRoute, useRouter } from "vue-router";
import { useUserStore } from '@/stores/user'
import { useRoomStore } from "@/stores/room";
import { useGameStore } from '@/stores/game'
import { useOpenviduStore } from "@/stores/openvidu";

const userStore = useUserStore()
const roomStore = useRoomStore()
const gameStore = useGameStore()
const openviduStore = useOpenviduStore()

const roomId = ref('')
const roomTitle = ref('')
const totalParticipantCnt = ref('')
const myNickname = ref('')

/// 플레이어, 관전을 위한 변수들 크헝헝
const publisherComputed = computed(() => openviduStore.publisher)
const subscribersComputed = computed(() => openviduStore.subscribers)
const playersComputed = computed(() => openviduStore.players)
const watchersComputed = computed(() => openviduStore.watchers)

const roomManagerNickname = computed(() => gameStore.roomManagerNickname)
const isManager = computed(() => {
  if (myNickname.value === roomManagerNickname.value) {
    return true
  } else {
    return false
  }
})
const isReady = ref(false)

roomId.value = roomStore.roomId
roomTitle.value = roomStore.title
myNickname.value = userStore.myNickname

// 방 나가기
const leaveRoom = function() {
  // 관전자면
  if (roomStore.isWatcher === true) {
    roomStore.leaveWatcher()
    roomStore.isWatcher = false
  } else {
    roomStore.leaveRoom()
  }
}

// 게임 시작
const startGame = function () {
  const payload = {
    title: roomTitle.value
  }
  roomStore.startGame(payload)
}

// 게임 준비
const readyGame = function () {
  // 해당 플레이어 준비 상태 반전
  isReady.value = !isReady.value
  gameStore.sendReady(roomTitle.value, isReady.value)
}

// 강퇴
const forceDisconnect = function(clientData) {
  const payload = {
    title: roomTitle.value,
    nickname: clientData
  }
  console.log(payload);
  roomStore.forceMemberOut(payload)
}

// 캠
const findVideo = function (players, targetNickname) {
  console.log(players.length);
    for (let i = 0; i < players.length; i++) {
      const player = players[i]
      console.log('player ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★')
      console.log(i, player)
      console.log('player ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★')
      console.log(i, player.nickname, targetNickname);
      if (player.nickname === targetNickname) {
        console.log(player)
        console.log(targetNickname)
        console.log(player.nickname);
        console.log(player.player);
        return player.player
      }
    }
    return publisherComputed.value
  }


/////////////////////채팅창을 위한 부분
const inputMessage = ref("")
const messages = ref([])

const sendMessage = function(input) {
  openviduStore.sendMessage(input)
  inputMessage.value = ''
}

// scroll 함수
function scrollUl() {
  // 채팅창 form 안의 ul 요소, (ul 요소 안에 채팅 내용들이 li 요소로 입력된다.)
  let chatUl = document.querySelector('.chat_ul');
  chatUl.scrollTop = chatUl.scrollHeight; // 스크롤의 위치를 최하단으로
}

messages.value = computed(() => openviduStore.messages)

// messages 배열이 변경될 때마다 scrollUl 함수를 호출하여 스크롤 갱신
watch([openviduStore.messages], () => {
  window.setTimeout(scrollUl, 50);
})

onMounted(() => {
  navigator.mediaDevices.getUserMedia({ video: true, audio: true })
    .then((stream) => {
      // 카메라 및 마이크에 대한 성공적인 액세스 처리
    })
    .catch((error) => {
      console.error('카메라 및 마이크 액세스 오류:', error);
    });
  // 프로필 아이콘 안보이기
  userStore.viewProfileIcon = false

  roomId.value = roomStore.roomId
  roomTitle.value = roomStore.title
  totalParticipantCnt.value = roomStore.totalParticipantCnt
  myNickname.value = userStore.myNickname

  // 메인페이지 -> 방 만들기 : 세션 생성
  openviduStore.joinSession()

  window.addEventListener("beforeunload", (event) => {
    event.returnValue = '';
    event.preventDefault()
    // if (roomStore.isWatcher === true  && roomStore.title !== '') {
    //   roomStore.leaveWatcher()
    // } else if (roomStore.title !== '') {
    //     roomStore.leaveRoom()
    // }
  })
  window.addEventListener("popstate", (event) => {
    if (roomStore.isWatcher === true  && roomStore.title !== '') {
      roomStore.leaveWatcher()
    } else if (roomStore.title !== '') {
        roomStore.leaveRoom()
    }
  })
})

// localStorage에서 불러오기
onUnmounted(() => {
  // 프로필 아이콘 보이기
  userStore.viewProfileIcon = true
  roomId.value = roomStore.roomId
  roomTitle.value = roomStore.title
  totalParticipantCnt.value = roomStore.totalParticipantCnt
  myNickname.value = userStore.myNickname

  console.log('방 정보 가져오기 성공!!');

  window.removeEventListener("beforeunload", (event) => {
    event.preventDefault()
    event.returnValue = '';
    // if (roomStore.isWatcher === true  && roomStore.title !== '') {
    //   roomStore.leaveWatcher()
    // } else if (roomStore.title !== '') {
    //     roomStore.leaveRoom()
    // }
  })
  window.removeEventListener("popstate", (event) => {
    if (roomStore.isWatcher === true  && roomStore.title !== '') {
      roomStore.leaveWatcher()
    } else if (roomStore.title !== '') {
        roomStore.leaveRoom()
    }
  })
})

</script>

<style scoped>

.room-title {
  color: white;
}

.flex-container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}

.box-right {
  position: relative;
}


#chat-container {
  margin-top: 20px;
  margin-bottom: 20px;
  width: 100%;
  height: 300px;
  background-color: rgb(92, 128, 192);
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  /* align-items: center; */
}

#chat-window {
  color: white;
  height: 250px;
  max-height: 250px;
}

#chat-history {
  color: white;
  max-height: 250px;
  overflow-y: auto;
}

#chat-write {
  
}

.chat-write-input {
  border: 1px solid rgb(26, 23, 23);
}

.chat-write-btn {
  border-radius: 3px;
  background-color: aqua;
  box-shadow: 1px 1px 1px 1px rgb(21, 19, 19);
  transition-duration: 0.1s;
}
.chat-write-btn:hover {
  border-radius: 3px;
  background-color: aqua;
  opacity: 70%;
}
.chat-write-btn:active {
  margin-left: 5px;
  margin-top: 5px;
  box-shadow: none;
}

#watcher-container {
  width: 100%;
  height: 150px;
}

.box-btns {
  margin-top: 10px;
}
.btn-start {
 background-color: blue;
}

.btn-start:hover {
  background-color: blue;
  opacity: 50%;
  border: 1px solid white;
  color: white;
}
.btn-invite {
  background-color: green;
}

.btn-invite:hover {
  background-color: green;
  border: 1px solid white;
  opacity: 50%;
  color: white;
}

.btn-exit {
  background-color: red;
}

.btn-exit:hover {
  background-color: red;
  opacity: 50%;
  border: 1px solid white;
  color: white;
}

.custom-btn {
  width: 60px;
  height: 40px;
  color: #fff;
  border-radius: 5px;
  padding: 10px 25px;
  font-family: 'Lato', sans-serif;
  font-weight: 500;
  background: transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  display: inline-block;
   box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  outline: none;
}

@-webkit-keyframes shiny-btn1 {
    0% { -webkit-transform: scale(0) rotate(45deg); opacity: 0; }
    80% { -webkit-transform: scale(0) rotate(45deg); opacity: 0.5; }
    81% { -webkit-transform: scale(4) rotate(45deg); opacity: 1; }
    100% { -webkit-transform: scale(50) rotate(45deg); opacity: 0; }
}

/* 12 */
.btn-1{
  position: relative;
  right: 20px;
  bottom: 20px;
  border:none;
  box-shadow: none;
  width: 60px;
  height: 40px;
  line-height: 42px;
  -webkit-perspective: 230px;
  perspective: 230px;
}
.btn-2{
  position: relative;
  right: 20px;
  bottom: 20px;
  border:none;
  box-shadow: none;
  width: 60px;
  height: 40px;
  line-height: 42px;
  -webkit-perspective: 230px;
  perspective: 230px;
}
.btn-3{
  position: relative;
  right: 20px;
  bottom: 20px;
  border:none;
  box-shadow: none;
  width: 60px;
  height: 40px;
  line-height: 42px;
  -webkit-perspective: 230px;
  perspective: 230px;
}
.btn-1 span {
  background: rgb(0,172,238);
  background: linear-gradient(0deg, rgba(0,172,238,1) 0%, rgba(2,126,251,1) 100%);
  display: block;
  position: absolute;
  width: 60px;
  height: 40px;
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  border-radius: 5px;
  margin:0;
  text-align: center;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  -webkit-transition: all .3s;
  transition: all .3s;
}
.btn-2 span {
  background: rgb(45, 217, 82);
  background: linear-gradient(0deg, rgb(45, 217, 82) 0%, rgb(33, 168, 62) 100%);
  display: block;
  position: absolute;
  width: 60px;
  height: 40px;
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  border-radius: 5px;
  margin:0;
  text-align: center;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  -webkit-transition: all .3s;
  transition: all .3s;
}
.btn-3 span {
  background: rgb(218, 62, 62);
  background: linear-gradient(0deg, rgb(218, 62, 62) 0%, rgb(215, 30, 30) 100%);
  display: block;
  position: absolute;
  width: 60px;
  height: 40px;
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  border-radius: 5px;
  margin:0;
  text-align: center;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  -webkit-transition: all .3s;
  transition: all .3s;
}
.custom-btn span:nth-child(1) {
  box-shadow:
   -7px -7px 20px 0px #fff9,
   -4px -4px 5px 0px #fff9,
   7px 7px 20px 0px #0002,
   4px 4px 5px 0px #0001;
  -webkit-transform: rotateX(90deg);
  -moz-transform: rotateX(90deg);
  transform: rotateX(90deg);
  -webkit-transform-origin: 50% 50% -20px;
  -moz-transform-origin: 50% 50% -20px;
  transform-origin: 50% 50% -20px;
}
.custom-btn span:nth-child(2) {
  -webkit-transform: rotateX(0deg);
  -moz-transform: rotateX(0deg);
  transform: rotateX(0deg);
  -webkit-transform-origin: 50% 50% -20px;
  -moz-transform-origin: 50% 50% -20px;
  transform-origin: 50% 50% -20px;
}
.custom-btn:hover span:nth-child(1) {
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
  -webkit-transform: rotateX(0deg);
  -moz-transform: rotateX(0deg);
  transform: rotateX(0deg);
}
.custom-btn:hover span:nth-child(2) {
  box-shadow:inset 2px 2px 2px 0px rgba(255,255,255,.5),
   7px 7px 20px 0px rgba(0,0,0,.1),
   4px 4px 5px 0px rgba(0,0,0,.1);
 color: transparent;
  -webkit-transform: rotateX(-90deg);
  -moz-transform: rotateX(-90deg);
  transform: rotateX(-90deg);
}

.is-ready {
  border: 5px solid green; /* 준비가 완료되었을 때의 테두리 색상 */
  border-radius: 30px;
  animation: pulse 1s infinite alternate; /* 테두리에 깜빡거리는 애니메이션 효과 */
}

@keyframes pulse {
  from {
    border-color: green;
    box-shadow: 0 0 10px green;
  }
  to {
    border-color: #00ff00;
    box-shadow: 0 0 20px #00ff00;
  }
}

</style>
