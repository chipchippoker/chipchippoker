<template>
  <div class="w-100 h-100">
    <!-- 플레이어 감정, 동영상, 카드 -->
    <div class="w-100 h-100 position-relative">
      <!-- 관전 중이 아니고 게임하는 플레이어일 때 -->
      <div v-if="roomStore.isWatcher===false">
        <div class="position-absolute top-0 start-0 d-flex flex-column h-50" id="player1" style="width: 500px;">
          <div class="text-white align-self-center ">{{ myNickname }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 0">보유코인: {{gameStore?.gameMemberInfos[0]?.havingCoin}}</div>

          </div>
          <div class="d-flex m-2 mt-0 h-100">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              :stream-manager="publisherComputed"
              view="playView"
              />
            </div>
            <!-- 내 카드는 뒷면을 기본적으로 보여주기 -->
            <img class="object-fit-contain" style="width: 150px;" :src=getBackCardUrl() alt="?">
            
          </div>
        </div>
        <div v-if="playersComputed.length > 0" class="position-absolute top-0 end-0 d-flex flex-column h-50" id="player2" style="width: 500px;">
          <div class="text-white align-self-center " v-if="subscribersComputed.length > 0" >{{ player1 }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 1">보유코인: {{gameStore?.gameMemberInfos[1]?.havingCoin}}</div>
          
          </div>
          <div class="d-flex flex-row-reverse h-100 m-2 mt-0">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              v-if="playersComputed.length > 0"
              :stream-manager="playersComputed[0]"
              view="playView"
              @client-data="fPlayer1"
              />
            </div>
            <img class="object-fit-contain" style="width: 150px;" :src=getCardUrl(1,gameStore?.gameMemberInfos[1].cardNumber) alt="?">
            
          </div>
        </div>
        <div v-if="playersComputed.length > 1" class="position-absolute bottom-0 start-0 d-flex flex-column h-50" id="player3" style="width: 500px;">
          <div class="text-white align-self-center ">{{ player2 }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 2">보유코인: {{gameStore?.gameMemberInfos[2]?.havingCoin}}</div>
          
          </div>
          <div class="d-flex m-2 mt-0 h-100">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              v-if="playersComputed.length > 1"
              :stream-manager="playersComputed[1]"
              view="playView"
              @client-data="fPlayer2"
              />
            </div>
            <img class="object-fit-contain" style="width: 150px;" :src=getCardUrl(1,gameStore?.gameMemberInfos[2].cardNumber) alt="?">
          </div>
        </div>
        <div v-if="playersComputed.length > 2" class="position-absolute bottom-0 end-0 d-flex flex-column h-50" id="player4" style="width: 500px;">
          <div class="text-white align-self-center">{{ player3 }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 3">보유코인: {{gameStore?.gameMemberInfos[3]?.havingCoin}}</div>
          
          </div>
          <div class="d-flex flex-row-reverse m-2 h-100 mt-0">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              v-if="playersComputed.length > 2"
              :stream-manager="playersComputed[2]"
              view="playView"
              @client-data="fPlayer3"
              />
            </div>

            <img class="object-fit-contain" style="width: 150px;" :src=getCardUrl(1,gameStore?.gameMemberInfos[3].cardNumber) alt="?">
            
          </div>
        </div>
      </div>
      <!-- 관전 중일 때 -->
      <div v-else>
        <div v-if="playersComputed.length > 0" class="position-absolute top-0 start-0 d-flex flex-column h-50" id="player1" style="width: 500px;">
          <div class="text-white align-self-center ">{{ gameStore?.gameMemberInfos[0]?.nickname }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 0">보유코인: {{gameStore?.gameMemberInfos[0]?.havingCoin}}</div>

          </div>
          <div class="d-flex m-2 mt-0 h-100">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              :stream-manager="playersComputed[0]"
              view="playView"
              />
            </div>
            <img class="object-fit-contain" style="width: 150px;" :src=getBackCardUrl(1,gameStore?.gameMemberInfos[0].cardNumber) alt="?">         
          </div>
        </div>
        <div v-if="playersComputed.length > 1" class="position-absolute top-0 end-0 d-flex flex-column h-50" id="player2" style="width: 500px;">
          <div class="text-white align-self-center " >{{ gameStore?.gameMemberInfos[1]?.nickname }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 1">보유코인: {{gameStore?.gameMemberInfos[1]?.havingCoin}}</div>
          

          </div>
          <div class="d-flex flex-row-reverse h-100 m-2 mt-0">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              :stream-manager="playersComputed[1]"
              view="playView"
              @client-data="fPlayer1"
              />
            </div>
            <img class="object-fit-contain" style="width: 150px;" :src=getCardUrl(1,gameStore?.gameMemberInfos[1].cardNumber) alt="?">
          </div>
        </div>
        <div v-if="playersComputed.length > 2" class="position-absolute bottom-0 start-0 d-flex flex-column h-50" id="player3" style="width: 500px;">
          <div class="text-white align-self-center ">{{ gameStore?.gameMemberInfos[2]?.nickname }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 2">보유코인: {{gameStore?.gameMemberInfos[2]?.havingCoin}}</div>
          

          </div>
          <div class="d-flex m-2 mt-0 h-100">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              :stream-manager="playersComputed[2]"
              view="playView"
              @client-data="fPlayer2"
              />
            </div>
            <img class="object-fit-contain" style="width: 150px;" :src=getCardUrl(1,gameStore?.gameMemberInfos[2].cardNumber) alt="?">
          </div>
        </div>
        <div v-if="playersComputed.length > 3" class="position-absolute bottom-0 end-0 d-flex flex-column h-50" id="player4" style="width: 500px;">
          <div class="text-white align-self-center">{{ gameStore?.gameMemberInfos[3]?.nickname }} / 
          <!-- 보유코인 연결 -->
            <div v-if="gameStore.player.length > 3">보유코인: {{gameStore?.gameMemberInfos[3]?.havingCoin}}</div>
          
          </div>
          <div class="d-flex flex-row-reverse m-2 h-100 mt-0">
            <div>
              <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
            </div>
            <div class="bg-black m-2" style="width: 300px; height: 210px;">
              <UserVideoVue
              :stream-manager="playersComputed[3]"
              view="playView"
              @client-data="fPlayer3"
              />
            </div>
            <img class="object-fit-contain" style="width: 150px;" :src=getCardUrl(1,gameStore?.gameMemberInfos[3].cardNumber) alt="?">
          </div>
        </div>
      </div>
      <!-- 배팅보드 -->
      <PlayBattingVue 
      :session="session"
      />
      <!-- 남은 시간, 총 배팅 코인 -->
      <div class="position-absolute top-0 start-50 translate-middle mt-4 d-flex flex-column align-items-center">
        <div class="text-white fs-3 fw-bold">12 초</div>
        <div class="text-white fw-bold">총 배팅: 21</div>
      </div>
    </div>
    <!-- 나가기 -->
    <div class="position-absolute bottom-0 end-0">
      <button class="bg-danger me-3 mb-3 text-white fw-bold rounded-pill d-flex justify-content-center align-items-center border border-2 border-white" 
      data-bs-toggle="modal" data-bs-target="#roomOutModal" style="width: 70px; height: 30px" >나가기</button>
    </div>

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
                <button class="custom-btn btn-2" data-bs-dismiss="modal" style="width: 50px;"><span>게임하기</span><span>아니요</span></button>
                <button class="custom-btn btn-3" data-bs-dismiss="modal" @click="leaveSession()"><span>나가?</span><span>나가기</span></button>
            </div>
          </div>
      
        </div>
      </div>  
    </div>
  </div>
</template>

<script setup>
  import PlayBattingVue from "@/components/Play/PlayBatting.vue";
  import UserVideoVue from "../Cam/UserVideo.vue";
  import { ref, defineProps, computed, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import axios from 'axios'
  import { OpenVidu } from "openvidu-browser";
  import { useGameStore } from "@/stores/game";
  import { useRoomStore } from "@/stores/room";

  const gameStore = useGameStore()
  const roomStore = useRoomStore()

  // 앞장 카드 가져오기
  const getCardUrl = function (setnum, cardnum) {
      return new URL(`/src/assets/cards/set${setnum}/card${cardnum}.png`, import.meta.url).href;
  };
  // 뒷장 카드 가져오기
  const getBackCardUrl = function () {
      return new URL(`/src/assets/cards/back.png`, import.meta.url).href;
  }

  const router = useRouter()

  const roomId = ref(null)
  const myNickname = ref(null)
  const props = defineProps({
    roomId: String,
    myNickname: String
  });

  roomId.value = props.roomId
  myNickname.value = props.myNickname

  axios.defaults.headers.post["Content-Type"] = "application/json";

  const APPLICATION_SERVER_URL = process.env.NODE_ENV === 'production' ? '' : 'http://localhost:5000/';

  // OpenVidu objects
  const OV = ref(undefined)
  const session = ref(undefined)
  let mainStreamManager = ref(undefined)
  const publisher = ref(undefined)
  const subscribers = ref([])

  
  ////다시그려내기 위해 computed 작성
  const mainStreamManagerComputed = computed(() => mainStreamManager.value);
  const publisherComputed = computed(() => publisher.value);
  // const subscribersComputed = computed(() => subscribers);
  const subscribersComputed = computed(() => subscribers.value);
  
  // 자신을 제외한 다른 player들
  const player1 = ref(null)
  const player2 = ref(null)
  const player3 = ref(null)

  // 플레이어 배열 초기화
  gameStore.player = [myNickname.value]
  
  const fPlayer1 = function(clientData) {
    player1.value = clientData
    // 게임 store 에서 player 저장
    if (player1.value != null){
      gameStore.player.push(player1.value)
    }
  }
  const fPlayer2 = function(clientData) {
    player2.value = clientData
    if (player2.value != null){
      gameStore.player.push(player2.value)
    }
  }
  const fPlayer3 = function(clientData) {
    player3.value = clientData
    if (player3.value != null){
      gameStore.player.push(player3.value)
    }
  }
  
  /// 관전을 위한 변수들 크헝헝
  const players = ref([])
  const watchers = ref([])
  const playersComputed = computed(() => players.value);

  // vue2에서의 methods 부분을 vue3화 시키기
  function joinSession() {
    // --- 1) Get an OpenVidu object ---
    OV.value = new OpenVidu()
    
    // --- 2) Init a session ---
    session.value = OV.value.initSession()

    // --- 3) Specify the actions when events take place in the session ---
    // On every new Stream received...
    session.value.on("streamCreated", ( {stream} )=> {
      // const subscriber = session.subscribe(stream)
      const subscriber = session.value.subscribe(stream)// 닉네임과 watcher 얻기
      function getConnectionData() {
          const { connection } = stream;
          return JSON.parse(connection.data);
      }
      const clientData = computed(() => {
        const { clientData } = getConnectionData();
        return clientData;
      });
      const isWatcher = computed(() => {
        const { isWatcher } = getConnectionData();
        return isWatcher;
      });

      subscribers.value.push(subscriber)
      // 플레이어, 관전자 리스트에도 추가
      if (isWatcher === false) {
        players.value.push(subscriber)
      } else {
        watchers.value.push(subscriber)
        // 관전자 이름들도 추가
        roomStore.watchersNickname.push(clientData)
      }
    })

    // On every Stream destroyed...
    session.value.on("streamDestroyed", ( {stream} ) => {
      const index = subscribers.value.indexOf(stream.streamManager, 0)
      if(index >= 0){
        subscribers.value.splice(index, 1)
      }
      // 플레이어, 관전자 리스트에도 삭제
      if (players.value.includes(stream.streamManager)) {
        const index1 = players.value.indexOf(stream.streamManager, 0)
        if(index1 >= 0){
          players.value.splice(index1, 1)
        }      
      }
      if (watchers.value.includes(stream.streamManager)) {
        const index2 = watchers.value.indexOf(stream.streamManager, 0)
        if(index2 >= 0){
          watchers.value.splice(index2, 1)
        }      
      }
      // 관전자 이름도 삭제
      function getConnectionData() {
        const { connection } = stream;
        return JSON.parse(connection.data);
      }
      const clientData = computed(() => {
        const { clientData } = getConnectionData();
        return clientData;
      });
      
      
      if (roomStore.watchersNickname.includes(clientData)) {
        const index3 = roomStore.watchersNickname.indexOf(clientData, 0)
        if(index3 >= 0){
          roomStore.watchersNickname.splice(index3, 1)
        }      
      }
    })

    // On every asynchronous exception...
    session.value.on("exception", ({ exception }) => {
      console.warn(exception);
    });

    session.value.on('reconnecting', () => console.warn('Oops! Trying to reconnect to the session'));
    session.value.on('reconnected', () => console.log('Hurray! You successfully reconnected to the session'));
    session.value.on('sessionDisconnected', (event) => {
        if (event.reason === 'networkDisconnect') {
            console.warn('Dang-it... You lost your connection to the session');
        } else {
            // Disconnected from the session for other reason than a network drop
        }
    });

    // 채팅 이벤트 수신 처리 함. session.on이 addEventListenr 역할인듯.
    session.value.on('signal:chat', (event) => { // event.from.connectionId === session.value.connection.connectionId 이건 나와 보낸이가 같으면임
      const messageData = JSON.parse(event.data);
      if(event.from.connectionId === session.value.connection.connectionId){
        messageData['username'] = '나'
      }
      messages.value.push(messageData);
    });


    // --- 4) Connect to the session with a valid user token ---
    // Get a token from the OpenVidu deployment
    // getToken(mySessionId).then((token) => {
    getToken(roomId.value).then((token) => {
      // First param is the token. Second param can be retrieved by every user on event
      // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
      // session.value.connect(token, {clientData: myUserName})
      session.value.connect(token, {clientData: myNickname.value, isWatcher: roomStore.isWatcher})
      .then(() => {
          // --- 5) Get your own camera stream with the desired properties ---

          // const cameraSelect = document.querySelector('select[name="cameras"]');
          // const audioSelect = document.querySelector('select[name="audios"]');

          // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
          // element: we will manage it on our own) and with the desired properties
          let publisher_tmp = OV.value.initPublisher(undefined, {
            audioSource: undefined, // The source of audio. If undefined default microphone
            videoSource: undefined, // The source of video. If undefined default webcam
            // audioSource: audioSelect.value, // The source of audio. If undefined default microphone
            // videoSource: cameraSelect.value, // The source of video. If undefined default webcam
            // publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
            // publishVideo: true, // Whether you want to start publishing with your video enabled or not
            // publishAudio: !muted.value, // Whether you want to start publishing with your audio unmuted or not
            // publishVideo: !camerOff.value, // Whether you want to start publishing with your video enabled or not
            resolution: "300x210", // The resolution of your video
            frameRate: 30, // The frame rate of your video
            insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
            mirror: false, // Whether to mirror your local video or not
          });

          // Set the main video in the page to display our webcam and store our Publisher
          mainStreamManager.value = publisher_tmp
          publisher.value = publisher_tmp

          // --- 6) Publish your stream ---
          // session.publish(publisher)
          session.value.publish(publisher.value)
          // getMedia()  // 세션이 만들어졌을때 미디어 불러옴
        })
        .catch((error) => {
          console.log("There was an error connecting to the session:", error.code, error.message);
        })
    })

    window.addEventListener("beforeunload", (event) => {
      leaveSession();
      // Uncomment the line below if you want to show a confirmation message
      // event.returnValue = "Are you sure you want to leave?";
    });
  }

  function leaveSession(){
    // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
    if(session.value) session.value.disconnect()
    
    // Empty all properties...
    session.value = undefined;
    mainStreamManager.value = undefined;
    publisher.value = undefined;
    subscribers.value = [];
    players.value = []
    watchers.value = []
    roomStore.watchersNickname = []
    OV.value = undefined;

    // Remove beforeunload listener
    window.removeEventListener("beforeunload", leaveSession)
    router.push(
      {name: 'main'}
    )
  }

  function updateMainVideoStreamManager(stream) {
    if (mainStreamManager.value === stream) return
    mainStreamManager.value = stream
  }

  async function getToken(roomId) {
    const sessionId = await createSession(roomId);
    return await createToken(sessionId);
  }

  async function createSession(sessionId) {
    const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId, userNo: 53, endHour: 1, endMinute: 30, quota: 16, isPrivacy: false}, {
      headers: { 'Content-Type': 'application/json', },
    });
    return response.data; // The sessionId
  }

  async function createToken(sessionId) {
    const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections', {}, {
      headers: { 'Content-Type': 'application/json', },
    });
    return response.data; // The token
  }

  onMounted (() => {
    navigator.mediaDevices.getUserMedia({ video: true, audio: true })
      .then((stream) => {
        // 카메라 및 마이크에 대한 성공적인 액세스 처리
      })
      .catch((error) => {
        console.error('카메라 및 마이크 액세스 오류:', error);
      });

    joinSession()
  })
</script>

<style scoped>
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
  width: 80px;
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
  width: 80px;
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
