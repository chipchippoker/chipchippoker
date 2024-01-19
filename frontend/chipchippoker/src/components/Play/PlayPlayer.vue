<template>
  <div class="w-100 h-100">
    <!-- 플레이어 감정, 동영상, 카드 -->
    <div class="w-100 h-100 position-relative">
      <div class="position-absolute top-0 start-0 d-flex flex-column h-50" id="player1" style="width: 500px;">
        <div class="text-white align-self-center ">{{ myUserName }} / 보유코인: 10개</div>
        <div class="d-flex m-2 mt-0 h-100">
          <div>
            <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
          </div>
          <!-- 캠활성화, 음소거 버튼 -->
          <!-- <button id="camera-activate" @click="handleCameraBtn()">캠 비활성화</button>
          <button id="mute-activate" @click="handleMuteBtn()">음소거 활성화</button> -->
          <div class="bg-black m-2" style="width: 300px; height: 210px;">
            <UserVideoVue
            :stream-manager="publisherComputed" 
            />
          </div>
          <div class="m-2 bg-white align-self-center" style="width: 100px; height: 150px;">카드</div>
        </div>
        <!-- <div>
          <select name="cameras" @change="handleCameraChange">
            <option disabled>사용할 카메라를 선택하세요</option>
          </select>
          <select name="audios" @change="handleAudioChange">
            <option disabled>사용할 마이크를 선택하세요</option>
          </select>
        </div> -->
      </div>
      <div class="position-absolute top-0 end-0 d-flex flex-column h-50" id="player2" style="width: 500px;">
        <div class="text-white align-self-center " v-if="subscribersComputed.length > 0" >{{ player1 }} / 보유코인: 10개</div>
        <div class="d-flex flex-row-reverse h-100 m-2 mt-0">
          <div>
            <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
          </div>
          <div class="bg-black m-2" style="width: 300px; height: 210px;">
            <UserVideoVue
            v-if="subscribersComputed.length > 0"
            :stream-manager="subscribersComputed[0]"
            @client-data="fPlayer1"
            />
          </div>
          <div class="m-2 bg-white align-self-center" style="width: 100px; height: 150px;">카드</div>
        </div>
      </div>
      <div class="position-absolute bottom-0 start-0 d-flex flex-column h-50" id="player3" style="width: 500px;">
        <div class="text-white align-self-center " v-if="subscribersComputed.length > 1" >{{ player2 }} / 보유코인: 10개</div>
        <div class="d-flex m-2 mt-0 h-100">
          <div>
            <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
          </div>
          <div class="bg-black m-2" style="width: 300px; height: 210px;">
            <UserVideoVue
            v-if="subscribersComputed.length > 1"
            :stream-manager="subscribersComputed[1]"
            @client-data="fPlayer2"
            />
          </div>
          <div class="m-2 bg-white align-self-center" style="width: 100px; height: 150px;">카드</div>
        </div>
      </div>
      <div class="position-absolute bottom-0 end-0 d-flex flex-column h-50" id="player4" style="width: 500px;">
        <div class="text-white align-self-center" v-if="subscribersComputed.length > 2" >{{ player3 }} / 보유코인: 10개</div>
        <div class="d-flex flex-row-reverse m-2 h-100 mt-0">
          <div>
            <font-awesome-icon :icon="['fas', 'pause']" class="fa-5x" style="color: #ffffff;"/>
          </div>
          <div class="bg-black m-2" style="width: 300px; height: 210px;">
            <UserVideoVue
            v-if="subscribersComputed.length > 2"
            :stream-manager="subscribersComputed[2]"
            @client-data="fPlayer3"
            />
          </div>
          <div class="m-2 bg-white align-self-center" style="width: 100px; height: 150px;">카드</div>
        </div>
      </div>
      <!-- 배팅보드 -->
      <PlayBattingVue />
      <!-- 남은 시간, 총 배팅 코인 -->
      <div class="position-absolute top-0 start-50 translate-middle mt-4 d-flex flex-column align-items-center">
        <div class="text-white fs-3 fw-bold">12 초</div>
        <div class="text-white fw-bold">총 배팅: 21</div>
      </div>
    </div>
    <!-- 나가기 -->
    <div class="position-absolute bottom-0 end-0">
      <button class="bg-danger me-3 text-white fw-bold rounded-pill d-flex justify-content-center align-items-center border border-2 border-white" 
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

  const router = useRouter()

  const mySessionId = ref(null)
  const myUserName = ref(null)
  const props = defineProps({
    mySessionId: String,
    myUserName: String
  });

  mySessionId.value = props.mySessionId
  myUserName.value = props.myUserName

  axios.defaults.headers.post["Content-Type"] = "application/json";

  const APPLICATION_SERVER_URL = process.env.NODE_ENV === 'production' ? '' : 'http://localhost:5000/';

  // OpenVidu objects
  const OV = ref(undefined)
  const session = ref(undefined)
  let mainStreamManager = ref(undefined)
  const publisher = ref(undefined)
  const subscribers = ref([])

  ///////////////////카메라 및 오디오 설정을 위한 부분임
  const muted = ref(false)       // 기본은 음소거 비활성화
  const camerOff = ref(false)    // 기본 카메라 활성화
  const selectedCamera = ref("")  // 카메라 변경시 사용할 변수 
  const selectedAudio  = ref("")  // 오디오 변경시 사용할 변수

  ////다시그려내기 위해 computed 작성
  const mainStreamManagerComputed = computed(() => mainStreamManager.value);
  const publisherComputed = computed(() => publisher.value);
  // const subscribersComputed = computed(() => subscribers);
  const subscribersComputed = computed(() => subscribers.value);
  
  // 자신을 제외한 다른 player들
  const player1 = ref(null)
  const player2 = ref(null)
  const player3 = ref(null)

  const fPlayer1 = function(clientData) {
    player1.value = clientData
  }
  const fPlayer2 = function(clientData) {
    player2.value = clientData
  }
  const fPlayer3 = function(clientData) {
    player3.value = clientData
  }

  const showControls = ref(false);

  function toggleMute() {
    // 오디오 상태를 토글합니다.
    
  }

  function toggleVideo() {
    // 비디오 상태를 토글합니다.
    
  }

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
      const subscriber = session.value.subscribe(stream)
      subscribers.value.push(subscriber)
    })

    // On every Stream destroyed...
    session.value.on("streamDestroyed", ( {stream} ) => {
      const index = subscribers.value.indexOf(stream.streamManager, 0)
      if(index >= 0){
        subscribers.value.splice(index, 1)
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
    getToken(mySessionId.value).then((token) => {
      // First param is the token. Second param can be retrieved by every user on event
      // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
      // session.value.connect(token, {clientData: myUserName})
      session.value.connect(token, {clientData: myUserName.value})
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
            publishAudio: !muted.value, // Whether you want to start publishing with your audio unmuted or not
            publishVideo: !camerOff.value, // Whether you want to start publishing with your video enabled or not
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
          getMedia()  // 세션이 만들어졌을때 미디어 불러옴
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

  async function getToken(mySessionId) {
    const sessionId = await createSession(mySessionId);
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

    // 캠, 오디오 등 기기와 관련된 함수
  // 카메라와 오디오를 가져옴.
  async function getMedia() {
    try {
      const devices = await navigator.mediaDevices.enumerateDevices();
      const cameras = devices.filter((device) => device.kind === 'videoinput');
      const audios = devices.filter((device) => device.kind === 'audioinput');
      // const audios = undefined

      const cameraSelect = document.querySelector('select[name="cameras"]');
      const audioSelect = document.querySelector('select[name="audios"]');
      
      // 카메라 및 오디오 선택기 요소가 존재하는지 확인
      // if (cameraSelect && audioSelect) {
      if (cameras) {
        cameras.forEach((camera) => {
          const option = document.createElement('option');
          option.value = camera.deviceId;
          option.text = camera.label;
          cameraSelect.appendChild(option);
        });
      } else {
        const notCamera = cameraSelect.querySelector('option:disabled');
        notCamera.innerText = '사용 가능한 카메라가 없습니다.'
        // console.error('Camera selector not found');
      }
      if(audios){
        audios.forEach((audio) => {
          const option = document.createElement('option');
          option.value = audio.deviceId;
          option.text = audio.label;
          audioSelect.appendChild(option);
        });
      } else {
        const notAudio = audioSelect.querySelector('option:disabled');
        notAudio.innerText = '사용 가능한 마이크가 없습니다.'
        // console.error('Audio selector not found');
      }
    } catch (error) {
      console.error('Error getting media devices:', error);
    }
  }


  // // 음소거, 캠 활성화 버튼 작동
  // function handleCameraBtn() {
  //   console.log(publisher.value)
  //   if (!publisher.value) return;
  //   // 카메라 상태 토글
  //   camerOff.value = !camerOff.value;
  //   const cameraActivate = document.getElementById('camera-activate')
  //   if(camerOff.value){   //카메라 비활성화상태
  //     cameraActivate.innerText = '카메라 활성화'
  //   }else{                //카메라 활성화상태
  //     cameraActivate.innerText = '카메라 비활성화'
  //   }
    
  //   // 카메라 작동 상태를 적용
  //   publisher.value.publishVideo(!camerOff.value);
  // }

  // function handleMuteBtn() {
  //   if (!publisher.value) return;

  //   // 음소거 상태 토글
  //   muted.value = !muted.value;
  //   const muteActivate = document.getElementById('mute-activate')
  //   if(muted.value){   //음소거 활성화상태
  //     muteActivate.innerText = '음소거 비활성화'
  //   }else{                //음소거 비활성화상태
  //     muteActivate.innerText = '음소거 활성화'
  //   }
  //   // 음소거 설정을 적용
  //   publisher.value.publishAudio(!muted.value);
  // }
  
  // select태그에서 사용할 기기를 선택했을때
  // async function handleCameraChange(event) {
  //   selectedCamera.value = event.target.value;
  //   await replaceCameraTrack(selectedCamera.value);
  // }

  // async function handleAudioChange(event) {
  //   selectedAudio.value = event.target.value;
  //   await replaceAudioTrack(selectedAudio.value);
  // }

  async function replaceCameraTrack(deviceId) {
    if (!publisher.value) return;

    const newConstraints = {
        audio: false,
        video: {
            deviceId: { exact: deviceId },
        },
    };

    try {
        const newStream = await navigator.mediaDevices.getUserMedia(newConstraints);
        const newVideoTrack = newStream.getVideoTracks()[0];
        await publisher.value.replaceTrack(newVideoTrack);
    } catch (error) {
        console.error("Error replacing video track:", error);
    }
  }

  async function replaceAudioTrack(deviceId) {
    if (!publisher.value) return;

    const newConstraints = {
        audio: {
            deviceId: { exact: deviceId },
        },
        video: false,
    };

    try {
        const newStream = await navigator.mediaDevices.getUserMedia(newConstraints);
        const newAudioTrack = newStream.getAudioTracks()[0];
        await publisher.value.replaceTrack(newAudioTrack);
    } catch (error) {
        console.error("Error replacing audio track:", error);
    }
  }

  onMounted (() => {
    joinSession()
  })
</script>

<style scoped>
@import '@/assets/color.css';
@import '@/assets/size.css';

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
