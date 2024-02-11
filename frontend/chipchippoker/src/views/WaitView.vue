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
      <div class="position-absolute top-50 start-50 translate-middle my-3">
        <h3 class="room-title">{{ roomTitle }}</h3>
      </div>
    </div>

    <!--  body -->
    <div class="container row mt-5 h-100">
      <!-- 캠 -->
      <div class="col-9">
        <!-- 본인이 플레이어면 -->
        <!-- 모든 캠 -->
        <div v-if="roomStore.isWatcher===false" id="video-container">
          <div class="flex-container row g-1 p-0">
            <div class="col-6 mb-5 text-center"
            v-for="(player, index) in gameStore.memberInfos" :key="index">
              <!-- 내 캠 -->
              <div 
                v-if="player.nickname === userStore.myNickname"
                style="width: 400px; height: 300px;">
                <UserVideo 
                :stream-manager="publisherComputed" 
                :is-manager="isManager"
                :room-manager-nickname="roomManagerNickname"
                @force-disconnect="forceDisconnect"
                />
              </div>
              <!-- 다른 유저 캠 -->
              <div 
                v-else
                style="width: 400px; height: 300px;">
                <UserVideo 
                :stream-manager="findVideo(playersComputed, player.nickname)" 
                :is-manager="isManager"
                :room-manager-nickname="roomManagerNickname"
                @force-disconnect="forceDisconnect"
                />
              </div>
              <!-- 닉네임 -->
              <!-- <div style="margin-top: 10px; width: 400px; color: white; text-align: center;">{{ player.nickname }}</div> -->
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
              <div style="width: 410px; height: 310px;">
                <!-- 다른 사람 캠 -->
                <UserVideo
                  :stream-manager="findVideo(playersComputed, player.nickname)"
                  :is-manager="isManager"
                  :room-manager-nickname="roomManagerNickname"
                  @force-disconnect="forceDisconnect"
                  />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 로고, 관전자 목록, 채팅창, 버튼 -->
      <div class="col-3">
        <!-- 로고 -->
        <div class="m-3"><img class="logo" src="/src/assets/icons/Logo.png" alt=""></div>
        
        <!-- 관전자 목록, 채팅창 -->
        <div>

          <!-- 관전자 목록 -->
          <div id="watcher-container">
            <WaitWatcher />
          </div>

          <!-- 채팅창 -->
          <!-- 나중에 <chat-winow />로 넘길수 있도록 해보자. -->
          <div id="chat-container" class="d-flex flex-column justify-content-between">

            <!-- 1. 채팅 목록 -->
            <div id="chat-window">
              <ul id="messageList" class="m-0 chat_ul" style="list-style-type: none;">
                <li class="my-2" v-for="(message, index) in messages.value" :key="index">
                  <strong>{{message.username}}:</strong> {{message.message}}
                </li>
              </ul>
            </div>

            <!-- 2. 입력창 -->
            <div class="m-2" id="chat-input">
              <input class="form-control" type="text" placeholder="입력" v-model="inputMessage" @keyup.enter="sendMessage(inputMessage)">
              <button id="chat-btn" class="btn btn-outline-secondary" @click="sendMessage(inputMessage)">전송</button>
            </div>
          </div>

        </div>

        <!-- 시작(준비), 초대, 나가기 버튼 -->
        <div class="d-flex justify-content-center align-items-center box-btns m-0 pb-4 mt-3">
          <div v-if="roomStore.isWatcher===false">
            <!-- 시작 -->
            <button v-if="myNickname === gameStore.roomManagerNickname" @click="startGame()" class="btn-2 btn-2-blue mx-3"><span>시작하기</span></button>
            <!-- 준비 -->
            <button v-else-if="myNickname !== gameStore.roomManagerNickname && isReady === false" @click="readyGame()" class="btn-2 btn-2-blue mx-3"><span>준비하기</span></button>
            <!-- 준비 취소 -->
            <button v-else-if="myNickname !== gameStore.roomManagerNickname && isReady === true" @click="readyGame()" class="btn-2 btn-2-blue  mx-3"><span>준비 취소</span></button>
          </div>
          <!-- 나가기 -->
          <button class="btn-2 btn-2-red mx-3" data-bs-toggle="modal" data-bs-target="#roomOutModal"><span>나가기</span></button>
        </div>

        <!-- 나가기 모달 -->
        <div data-bs-backdrop="static" class="modal fade" id="roomOutModal" tabindex="-1" aria-labelledby="IconModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content" style="background-color: #ffde76;">
              <div class="modal-header border-0">
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body pt-0">
                <div class="text-center fw-bold fs-3 bg-modal-yellow">
                  정말 나가시겠습니까?
                </div>
                <div class="mt-3 d-flex justify-content-center">
                    <button class="btn-2 btn-2-blue mx-3" data-bs-dismiss="modal"><span>아니요</span></button>
                    <button class="btn-2 btn-2-red mx-3" data-bs-dismiss="modal" @click="leaveRoom()"><span>나가기</span></button>
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
    roomStore.isWatcher = false
    roomStore.leaveWatcher()
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

// 캠 - 아 드디어 했다 와 진짜 죽을거같다 playersComputed가 갱신되기 전에 이 함수가 실행되면서 그렇게 되면
// 무조건 publisherComputed.value가 나올 수 밖에 없기 때문에
// index를 같이 넣어서 갱신되지 않고 만약 이전 그대로면 undefined로 처음부터 uservideo에 들어가지 않게 해서
// 아무 것도 출력하지 않게 하면 나중에 playersComputed가 갱신 되었을 때 그에 맞는 카메라를 넣을 수 있음
const findVideo = function (players, targetNickname) {
  console.log(players);
  console.log(targetNickname);
    for (let i = 0; i < players.length; i++) {
      const player = players[i]
      if (player.nickname === targetNickname) {
        console.log(player)
        console.log(targetNickname)
        return player.player
      }
    }
    return undefined
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
  console.log('스크롤 갱싱 함수');
  console.log(chatUl.scrollTop);
  console.log(chatUl.scrollHeight);
  chatUl.scrollTop = chatUl.scrollHeight; // 스크롤의 위치를 최하단으로
  console.log(chatUl.scrollTop);
}

messages.value = computed(() => openviduStore.messages)

// messages 배열이 변경될 때마다 scrollUl 함수를 호출하여 스크롤 갱신
watch([openviduStore.messages], () => {
  console.log('스크롤 갱신');
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
    //   roomStore.leaveRoom()
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

.logo {
  width: 100%;
  height: auto;
  display: block;
}

#chat-container {
 background-color: #8497c7;
 border-radius: 10px;
 margin-top: 10px;
 height: 320px;
 box-shadow: inset;
}

#chat-window {
  padding: 5px;
  padding-top: 10px;
  height: 267px !important;
}

#messageList {
  max-height: 267px;
  margin: 10px;
  padding: 10px;
  padding-top: 20px;
  padding-bottom: 0;
  overflow-y: auto;
}

#chat-input {
  position: relative;
  margin-top: 5px;
}

#chat-btn {
  position: absolute;
  top: 0;
  right: 0;
}


#watcher-container {
  width: 100%;
  height: 150px;
}

.box-btns {
  margin-top: 10px;
}

.btn-2 {
  height: 35px;
  font-size: 1.2em;
  align-items: center;
}

/* 게임 준비 */
.is-ready {
  border: 5px solid green; /* 준비가 완료되었을 때의 테두리 색상 */
  border-radius: 30px;
  animation: pulse 1s infinite alternate; /* 테두리에 깜빡거리는 애니메이션 효과 */
}


::-webkit-scrollbar {
  width: 10px; /* 스크롤바 너비 */
}

::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.2); /* 스크롤바 썸의 배경색 */
  border-radius: 5px; /* 스크롤바 썸의 모서리 둥글게 */
}

::-webkit-scrollbar-track {
  background-color: transparent; /* 스크롤바 트랙의 배경색 투명하게 */
}
</style>
