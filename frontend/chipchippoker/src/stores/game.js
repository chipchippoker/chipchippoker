import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import axios from 'axios'
// import SockJS from 'sockjs-client'
import webstomp, { client } from 'webstomp-client'
import { useUserStore } from './user'
import { useRoomStore } from './room'  


export const useGameStore = defineStore('game', () => {

  const userStore = useUserStore()
  const roomStore = useRoomStore()
  const router = useRouter()

  const url = "wss://i10a804.p.ssafy.io/chipchippoker"

  const stompClient = webstomp.client(url)

  const gameRoomTitle = ref('')
  const roomInfo = ref({})
  const player = ref([])
  const myId = ref('')

  stompClient.connect({'access-token': userStore.accessToken}, (frame) => {
    console.log("Connect success", gameRoomTitle.value)

      stompClient.subscribe(`/from/chipchippoker/checkConnect/`, (message) => {
        console.log('기본 토픽 구독 성공')
        const response = JSON.parse(message.body)
        console.log(response.body.data)
      })
    })

    // 구독 핸들러
    const subscribeHandler = (gameRoomTitle) => {

      // 토픽 구독 및 수신
      const mySubscribtion = stompClient.subscribe(`/from/chipchippoker/checkConnect/${gameRoomTitle}`, (message) => {
        console.log("subscribe success")
        // 구독 아이디 추출
        console.log(message.headers);
        // 내 구독 아이디 저장 
        myId.value = message.headers.subscription


        switch (response.body.code) {
          case "성공": // 방 생성
            receiveCreateRoom()
            break
          case "MS002": // 방 입장
            receiveJoinRoom()
            break
          case "MS003": // 방 나가기
            receiveExitRoom()
            break     
          case "MS004": // 강퇴(타인)
            // 발행자(방장), 구독자
            // 어느 사용자가 강퇴되었는지 보여줌
            recieveBanYou(response)
            console.log('강퇴 함 ',response)
            break
          case "MS005": // 강퇴(본인)
            // 강퇴되었음을 보여줌
            // unsubscribe 시켜줌
            recieveBanMe(response)
            console.log('강퇴 당함 ',response)
          
            break
        }
      })
  // 
  const sendCreateRoom = function(title, countOfPeople){
    const message = { countOfPeople }
    stompClient.send(`/to/game/create/${title}`, JSON.stringify(message), {'access-token': userStore.accessToken})
  }

  const receiveCreateRoom = function(){
    console.log('방 생성 이벤트')
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
    console.log('방 입장 이벤트')
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
  const receiveExitRoom = function(){
    console.log('방 나가기 이벤트')
  }

  // 게임 준비 SEND
  const sendReady = function(gameRoomTitle){
    stompClient.send(`/to/game/reddy/${gameRoomTitle}`, JSON.stringify({}), {'access-token': userStore.accessToken})
  }

  // 게임 준비 RECEIVE
  const receiveReady = function(){
    console.log('게임 준비 이벤트');
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
    leaveRoom,
    ready,
    startGame,
    bet,
    kickUser,
    subscribeHandler,
    stompClient,
  }
}
},{persist:true})
