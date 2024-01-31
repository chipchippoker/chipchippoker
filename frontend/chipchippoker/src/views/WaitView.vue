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
            <div class="col-6">
              <div style="width: 400px; height: 300px;">
                <UserVideo 
                :stream-manager="publisherComputed" 
                :is-manager="isManager"
                :room-manager-nickname="roomManagerNickname"
                />
              </div>
            </div>
            <!-- 다른 사람 캠 -->
            <div
              class="col-6 mb-5"
              v-for="(sub, index) in playersComputed"
              :key="index">
              <div style="width: 400px; height: 300px;">
                <UserVideo
                  :stream-manager="sub"
                  :is-manager="isManager"
                  @force-disconnect="forceDisconnect"
                  />
              </div>
            </div>
          </div>
        </div>

        <!-- 본인이 관전자면 -->
        <div v-if="roomStore.isWatcher===true" id="video-container">
          <div class="flex-container row g-1 p-0">
            <div
              class="col-6 mb-5"
              v-for="sub in playersComputed"
              :key="sub.stream.connection.connectionId">
              <div style="width: 400px; height: 300px;">
                <!-- 다른 사람 캠 -->
                <UserVideo
                  :stream-manager="sub"
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
        <div class="my-3 me-5"><img class="small-logo ms-4 mt-1 ps-1" src="/src/assets/icons/Logo.png" alt=""></div>
        <div class="">
          <!-- 관전자 목록 -->
          <div id="watcher-container">
            <WaitWatcher />
          </div>
          <!-- 채팅창 -->
          <!-- 나중에 <chat-winow />로 넘길수 있도록 해보자. -->
          <div id="chat-container">
            <div id="chat-window" scrollTop>
              <ul id="chat-history">
                <li v-for="(message, index) in messages" :key="index">
                  <strong>{{message.username}}:</strong> {{message.message}}
                </li>
              </ul>
            </div>
            <form id="chat-write me-3">
              <input class="chat-write-input ms-4 me-3" type="text" placeholder="입력" v-model="inputMessage">
              <button class="chat-write-btn" @click="openviduStore.sendMessage">전송</button>
            </form>
          </div>
        </div>
          <!-- 시작(준비), 초대, 나가기 버튼 -->
          <div class="d-flex flex-column justify-content-center align-items-center box-btns m-0 pb-4 mt-5">
            <div>
              <!-- 시작 -->
              <button v-if="myNickname === roomManagerNickname" @click="startGame()" class="custom-btn btn-1 m-1"><span>시작해?</span><span>시작</span></button>
              <!-- 준비 -->
              <button v-else-if="myNickname !== roomManagerNickname && isReady === false" @click="readyGame()" class="custom-btn btn-1 m-1"><span>준비해?</span><span>준비</span></button>
              <!-- 준비 취소 -->
              <button v-else-if="myNickname !== roomManagerNickname && isReady === true" @click="readyGame()" class="custom-btn btn-1 m-1"><span>준비취소</span><span>준비완료</span></button>
              <!-- 초대 -->
              <button class="custom-btn btn-2 m-1"><span>초대해?</span><span>초대</span></button>
              <!-- 나가기 -->
              <button class="custom-btn btn-3 m-1" data-bs-toggle="modal" data-bs-target="#roomOutModal"><span>나가?</span><span>나가기</span></button>
            </div>
          </div>

          <!-- 나가기 모달 -->
          <div class="modal fade" id="roomOutModal" tabindex="-1" aria-labelledby="IconModalLabel" aria-hidden="true">
            <!-- 나가기 모달 -->
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

import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from "vue-router";
import { useUserStore } from '@/stores/user'
import { useRoomStore } from "@/stores/room";
import { useGameStore } from '@/stores/game'
import { useOpenviduStore } from "@/stores/openvidu";

const userStore = useUserStore()
const roomStore = useRoomStore()
const gameStore = useGameStore()
const openviduStore = useOpenviduStore()
const router = useRouter()
const route = useRoute()

const roomId = ref('')
const roomTitle = ref('')
const totalParticipantCnt = ref('')
const myNickname = ref('')

const roomManagerNickname = ref('')
const isManager = ref(false)
const isReady = ref(false)

roomId.value = roomStore.roomId
roomTitle.value = roomStore.title
roomManagerNickname.value = roomStore.roomManagerNickname
myNickname.value = userStore.myNickname
if (myNickname.value === roomManagerNickname.value) {
  isManager.value = true
}

// 방 나가기
const leaveRoom = function() {
  roomStore.leaveRoom()
  openviduStore.leaveSession()
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
  gameStore.sendReady(title.value, isReady.value)
}

const forceDisconnect = function(clientData) {
  const payload = {
    title: roomTitle.value,
    nickname: clientData
  }
  roomStore.forceMemberOut(payload)
}

/////////////////////채팅창을 위한 부분
const inputMessage = ref("")
const messages = ref([])

openviduStore.inputMessage = computed(() => inputMessage.value)
openviduStore.messages = computed(() => messages.value)


/// 플레이어, 관전을 위한 변수들 크헝헝
const publisherComputed = computed(() => openviduStore.publisher);
const subscribersComputed = computed(() => openviduStore.subscribers);
const playersComputed = computed(() => openviduStore.players);
const watchersComputed = computed(() => openviduStore.watchers);


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
  height: 100px;
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


</style>
