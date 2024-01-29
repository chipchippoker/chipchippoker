import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import axios from 'axios'
// import SockJS from 'sockjs-client'
import webstomp from 'webstomp-client'
import { useUserStore } from './user'
import { useRoomStore } from './room'  


export const useGameStore = defineStore('game', () => {
  const userStore = useUserStore()
  const roomStore = useRoomStore()
  const router = useRouter()

  const url = "wss://i10a804.p.ssafy.io/chipchippoker"

  // const sock = new SockJS("wss://i10a804.p.ssafy.io/chipchippoker")
  const stompClient = webstomp.client("wss://i10a804.p.ssafy.io/chipchippoker")

  // 게임 정보
  const gameRoomTitle = ref('')
  const roomInfo = ref({})
  const player = ref([])  // 방에 있는 플레이어들 정보
  const roomManagerNickname = ref('')
  const countOfPeople = ref(0)
  const myId = ref('')

  stompClient.connect({'access-token': userStore.accessToken}, (frame) => {
    console.log("Connect success", gameRoomTitle.value)

      stompClient.subscribe(`/from/chipchippoker/checkConnect/`, (message) => {
        console.log('기본 토픽 구독 성공')
        const response = JSON.parse(message.body)
        console.log(response.body.data)
      })
    })
    // stompClient.reconnect_delay = 5000
    stompClient.heartbeat.outgoing = 20000;
		stompClient.heartbeat.incoming = 0;
    // 구독 핸들러
    const subscribeHandler = (gameRoomTitle) => {
      
      // 토픽 구독 및 수신
      const mySubscribtion = stompClient.subscribe(`/from/chipchippoker/checkConnect/${gameRoomTitle}`, (message) => {
        console.log("subscribe success")
        // 내 구독 아이디 저장 
        
        myId.value = message.headers.subscription
        console.log(myId.value);
        console.log(message.headers);

        const response = JSON.parse(message.body)
        console.log(response.body);
        switch (response.body.code) {
          case "MS001": // 방 생성
            console.log(response.body.message);
            receiveCreateRoom(response.body.data)
            break
          case "MS002": // 방 입장
            console.log(response.body.message);
            receiveJoinRoom(response.body.data)
            break
          case "MS003": // 방 나가기
            console.log(response.body.message);
            receiveExitRoom(response.body.data)
            break     
          case "MS004": // 강퇴(타인)
            // 발행자(방장), 구독자
            // 어느 사용자가 강퇴되었는지 보여줌
            console.log(response.body.message);
            recieveBanYou(response)
            console.log('강퇴 함 ',response)
            break
          case "MS005": // 강퇴(본인)
            // 강퇴되었음을 보여줌
            // unsubscribe 시켜줌
            console.log(response.body.message);
            recieveBanMe(response)
            console.log('강퇴 당함 ',response)
            break
          case "MS006": // 게임 준비
            console.log(response.body.message);
            receiveReady(response.body.data)
            break
        }
      })
    }

  // 방 생성 SEND
  const sendCreateRoom = function(title, countOfPeople){
    const message = { countOfPeople }
    stompClient.send(`/to/game/create/${title}`, JSON.stringify(message), {'access-token': userStore.accessToken})
  }

  // 방 생성 RECEIVE
  const receiveCreateRoom = function(data){
    countOfPeople.value = data.countOfPeople
    player.value = data.memberInfos
    roomManagerNickname.value = data.roomManagerNickname

    router.push({
      name:'wait',
      params: { roomId: roomStore.roomId },
      state: {
        title: roomStore.title,
        totalParticipantCnt: roomStore.totalParticipantCnt
      }
    })
  }

  // 게임방 입장 SEND
  const sendJoinRoom = function(gameRoomTitle){
    stompClient.send(`/to/game/enter/${gameRoomTitle}`, JSON.stringify({}), {'access-token': userStore.accessToken})
  }

  // 게임방 입장 RECEIVE
  const receiveJoinRoom = function(){
    countOfPeople.value = data.countOfPeople
    player.value = data.memberInfos
    roomManagerNickname.value = data.roomManagerNickname

    router.push({
      name:'wait',
      params: { roomId: roomStore.roomId },
      state: {
        title: roomStore.title,
        totalParticipantCnt: roomStore.totalParticipantCnt
      }
    })
  }

  // 게임방 나가기 SEND
  const sendExitRoom = function(gameRoomTitle){
    stompClient.send(`/to/game/exit/${gameRoomTitle}`, JSON.stringify({}), {'access-token': userStore.accessToken})
  }

  // 게임방 나가기 RECEIVE
  const receiveExitRoom = function(data){
    countOfPeople.value = data.countOfPeople
    player.value = data.memberInfos
    roomManagerNickname.value = data.roomManagerNickname

    stompClient.unsubscribe(myId.value)
    router.push({
      name:'main'
    })
  }

  // 게임 준비 SEND
  const sendReady = function(gameRoomTitle, isReady){
    stompClient.send(`/to/game/reddy/${gameRoomTitle}`, JSON.stringify({ isReady }), {'access-token': userStore.accessToken})
  }

  // 게임 준비 RECEIVE
  const receiveReady = function(data){
    countOfPeople.value = data.countOfPeople
    player.value = data.memberInfos
    roomManagerNickname.value = data.roomManagerNickname

    console.log(data);
  }

  // 게임 시작 SEND
  const sendStartGame = function(gameRoomTitle){
    stompClient.send("/startGame", JSON.stringify({}), {'access-token': userStore.accessToken})
  }

  // 게임 시작 RECEIVE
  const receiveStartGame = function(gameRoomTitle){
    stompClient.send("/startGame", JSON.stringify({}), {'access-token': userStore.accessToken})
  }

  // 배팅
  const bet = function(gameRoomTitle, amount){
    const message = {
      action: "bet",
      amount,
    }
    stompClient.send("/bet", JSON.stringify(message), {'access-token': userStore.accessToken})
  }


  const kickUser = function(gameRoomTitle, nickname){
    stompClient.send(`/to/game/ban/${gameRoomTitle}`, nickname, headers)
  }


  // 강퇴 타인
  const recieveBanYou = function(response){
    const countOfPeople = response.body.countOfPeople
    const memberInfos = response.body.memberInfos
    const roomManagerNickname = response.body.roomManagerNickname
    // 플레이어배열에서 강퇴당한 사람을 삭제합니다
    player.value.forEach((item, index) => {
      if ( item.nickname === memberInfos.nickname) {
        player.value.splice(index, 1);
      }
    })
  }


  // 강퇴 본인
  const recieveBanMe = function(response){
    mySubscribtion.unsubscribe(myId.value,headers)
    myId.value = null
    router.push('main')
  }
  
  return {
    rooms: ref([]),
    sendCreateRoom, receiveCreateRoom,
    sendJoinRoom, receiveJoinRoom, 
    sendExitRoom, receiveExitRoom, 
    sendReady, receiveReady,
    bet,
    kickUser,
    subscribeHandler,
    stompClient,
  }
},{persist:true})
