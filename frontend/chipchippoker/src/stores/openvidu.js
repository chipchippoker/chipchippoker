import WaitWatcher from "@/components/Wait/WaitWatcher.vue";
import UserVideo from "@/components/Cam/UserVideo.vue";
import { defineStore } from 'pinia'
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from "vue-router";
import { OpenVidu } from "openvidu-browser";
import { useUserStore } from '@/stores/user'
import { useRoomStore } from "@/stores/room";
import { useGameStore } from '@/stores/game'
import axios from 'axios'


export const useOpenviduStore = defineStore('openvidu', () => {
  const userStore = useUserStore()
  const roomStore = useRoomStore()
  const gameStore = useGameStore()
  const route = useRoute()
  const router = useRouter()

  axios.defaults.headers.post["Content-Type"] = "application/json";

  const APPLICATION_SERVER_URL = 'https://chipchippoker.shop/signal-server/';
  // const APPLICATION_SERVER_URL = process.env.NODE_ENV === 'production' ? '' : 'http://localhost:8500/';

  // OpenVidu objects
  const OV = ref(undefined)
  const session = ref(undefined)
  let mainStreamManager = ref(undefined)
  const publisher = ref(undefined)
  const subscribers = ref([])

  /////////////////////채팅창을 위한 부분
  const inputMessage = ref("")
  const messages = ref([])

  /// 관전을 위한 변수들 크헝헝
  const players = ref([])
  const watchers = ref([])

  function joinSession() {
    if (session.value !== undefined) {
      console.log('세션 이미 존재. joinSession 함수를 실행하지 않습니다.');
      return;
    }

    OV.value = new OpenVidu()
    session.value = OV.value.initSession()
    console.log('조인 세션!');

    session.value.on("streamCreated", ( {stream} )=> {
      // 이미 해당 스트림에 대한 구독이 이루어진 경우, 무시
      if (subscribers.value.some(sub => sub.stream === stream)) {
        return;
      }
      
      const subscriber = session.value.subscribe(stream)
      console.log('세션 생성!!!!');
      console.log(stream);
  
      // 닉네임과 watcher 얻기
      function getConnectionData() {
        const { connection } = stream;
        return JSON.parse(connection.data);
      }
      const { clientData } = getConnectionData()
      console.log(clientData);
      const { isWatcher } = getConnectionData()
      console.log(isWatcher);
      subscribers.value.push({subscriber:subscriber, nickname:clientData})
      // 플레이어, 관전자 리스트에도 추가
      // streammanager ---------------------------------------------
      if (isWatcher === false) {
        players.value.push({player:subscriber, nickname:clientData})
      } else {
        watchers.value.push({watcher:subscriber, nickname:clientData})
        // 관전자 이름들도 추가
        roomStore.watchersNickname.push(clientData)
      }
    })
  
    // On every Stream destroyed...
    session.value.on("streamDestroyed", ( {stream} ) => {
      function getConnectionData() {
        const { connection } = stream;
        return JSON.parse(connection.data);
      }
      const { clientData } = getConnectionData()
      
      // 파괴된 스트림에 연결된 구독자를 제거합니다.
      const index = subscribers.value.findIndex(sub => sub.subscriber.stream === stream);
      if (index !== -1) {
          subscribers.value.splice(index, 1);
      }

      // 파괴된 스트림에 연결된 플레이어를 제거합니다.
      const playerIndex = players.value.findIndex(player => player.player.stream === stream);
      if (playerIndex !== -1) {
          players.value.splice(playerIndex, 1);
      }

      // 파괴된 스트림에 연결된 관전자를 제거합니다.
      const watcherIndex = watchers.value.findIndex(watcher => watcher.watcher.stream === stream);
      if (watcherIndex !== -1) {
          watchers.value.splice(watcherIndex, 1);
      }
      
      // 관전자 이름도 삭제
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
        messageData['username'] = userStore.myNickname
      }
      messages.value.push(messageData);
    });
  
    let resolution = "400x300";  // 기본값 설정

    if (route.name === 'wait') {
      resolution = "400x300";
    } else if (route.name === 'play') {
      resolution = "280x210";
    }
  
    // --- Connect to the session with a valid user token ---
    // Get a token from the OpenVidu deployment
    getToken(String(roomStore.roomId)).then((token) => {
      console.log("토큰 만들어지나");
      session.value.connect(token, {clientData: userStore.myNickname, isWatcher: roomStore.isWatcher})
      .then(() => {
          // Get your own camera stream with the desired properties ---
          let publisher_tmp = OV.value.initPublisher(undefined, {
            audioSource: undefined, // The source of audio. If undefined default microphone
            videoSource: undefined, // The source of video. If undefined default webcam
            // publishAudio: !muted.value, // Whether you want to start publishing with your audio unmuted or not
            // publishVideo: !camerOff.value, // Whether you want to start publishing with your video enabled or not
            resolution: resolution, // The resolution of your video
            frameRate: 30, // The frame rate of your video
            insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
            mirror: true, // Whether to mirror your local video or not
          });
  
          // Set the main video in the page to display our webcam and store our Publisher
          mainStreamManager.value = publisher_tmp
          publisher.value = publisher_tmp
  
          // --- Publish your stream ---
          session.value.publish(publisher.value)
          console.log(players);
        })
        .catch((error) => {
          console.log("There was an error connecting to the session:", error.code, error.message);
        })
    })
  
    window.addEventListener("beforeunload", (event) => {
        leaveSession();
        // Uncomment the line below if you want to show a confirmation message
        // event.returnValue = "Are you sure you want to leave?";
      })
    }
  
  function leaveSession(){
    if(session.value) session.value.disconnect()
    
    // Empty all properties...
    session.value = undefined;
    mainStreamManager.value = undefined;
    publisher.value = undefined;
    subscribers.value = [];
    players.value = []
    watchers.value = []
    inputMessage.value = ''
    messages.value = []
    roomStore.watchersNickname = []
    OV.value = undefined;
  
    // Remove beforeunload listener
    window.removeEventListener("beforeunload", leaveSession)
    console.log('세션나가기')
    
  }
  
  /**
  * --------------------------------------------
  * GETTING A TOKEN FROM YOUR APPLICATION SERVER
  * --------------------------------------------
  */
  async function getToken(roomId) {
    console.log(roomId);
    console.log(typeof(roomId));
    const sessionId = await createSession(roomId);
    return await createToken(sessionId);
  }
  
  async function createSession(sessionId) {
    // const response = await axios.post(APPLICATION_SERVER_URL + 'sessions', { customSessionId: sessionId, userNo: 53, endHour: 1, endMinute: 30, quota: 16, isPrivacy: false}, {
    //   headers: { 'Content-Type': 'application/json', },
    // });
    const response = await axios.post(APPLICATION_SERVER_URL + 'sessions', { customSessionId: sessionId, userNo: 53, endHour: 1, endMinute: 30, quota: 16, isPrivacy: false}, {
      headers: { 'Content-Type': 'application/json', },
    });
    console.log('세션 생성 성공');
    return response.data; // The sessionId
  }
  
  async function createToken(sessionId) {
    // const response = await axios.post(APPLICATION_SERVER_URL + 'sessions/' + sessionId + '/connections', {}, {
    //   headers: { 'Content-Type': 'application/json', },
    // })
    const response = await axios.post(APPLICATION_SERVER_URL + 'sessions/' + sessionId + '/connections', {}, {
      headers: { 'Content-Type': 'application/json', },
    });
    return response.data; // The token
  }
  
  // 채팅창 구현을 위한 함수 제작
  function sendMessage(input) {
    console.log(input);
    if(input.trim()){
      // 다른 참가원에게 메시지 전송하기
      session.value.signal({
        data: JSON.stringify({username: userStore.myNickname, message: input}), // 메시지 데이터를 문자열로 변환해서 전송
        type: 'chat' // 신호 타입을 'chat'으로 설정
      });
      inputMessage.value = '';
    }
  }

  return {
    publisher, subscribers, inputMessage, messages, players, watchers,
    joinSession, leaveSession, sendMessage,
    }
},{persist:true})
