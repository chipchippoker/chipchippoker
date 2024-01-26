import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import axios from 'axios'
// import SockJS from 'sockjs-client'
import webstomp, { client } from 'webstomp-client'
import { useUserStore } from './user'
import { useRoomStore } from './room'

export const useGameStore = defineStore('game', () => {
  const useStore = useUserStore()
  const roomStore = useRoomStore()
  const router = useRouter()

  let headers = {'access-token':useStore.accessToken}

  const url = "wss://i10a804.p.ssafy.io/chipchippoker"
  const stompClient = webstomp.client(url)

  const gameRoomTitle = ref('')
  const roomInfo = ref({})
  const player = ref([])
  const myId = ref(null)


  stompClient.connect(headers, (frame) => {
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
      // 구독 아이디 저장
      myId.value = message.headers.subscription
      console.log(`myId.value ${myId.value}`);
      console.log('내가찍은 message',message)
      console.log("subscribe success")

      // 메시지 수신
      const response = JSON.parse(message.body)
      console.log(response.body)
      switch (response.body.code) {
        case "방 생성":
          // 발행 = 구독
          receiveCreateRoom()
          break
        case "방 입장":
          // 발행
          // 메인페이지 -> 대기페이지로 이동
          // 구독
          // 해당 방에 새로운 유저 입장
          receiveCreateRoom()
          break
        case "방 나가기":
          // 발행
          // 대기페이지 -> 메인페이지로 이동
          // 구독
          // 플레이페이지 -> 메인페이지로 이동
          break
        case "게임 준비":
          // 발행 구독
          // 해당 유저 게임 준비 상태로 변경 ( 캠 테두리 효과 )
          break
        case "게임 시작":
          // 구독자
          // 게임이 시작되고 플레이어의 각 코인을 25개로 저장
          // 플레이어의 수에 따라 카드 세트를 조정
          break
        case "배팅":
          // 모든 플레이어의 코인개수가 같아지거나 한명빼고 모두 폴드일때까지 진행
  
          // 발행자
          // 제한시간 내에 배팅이나 폴드를 누르면
          // 배팅 코인 개수를 플레이어의 코인에서 빼고 배팅필드 코인에 추가함
          // 폴드를 했으면 폴드처리
  
          // 구독자
          // 해당 턴인 플레이어의 코인의 변동을 확인
          break
        case "한 라운드 종료":
          // 모두 구독
  
          // 모든 플레이어의 코인개수가 같아지거나 한명빼고 모두 폴드일때 응답이 옴
          // 각자의 카드데이터를 받아 오픈
          // 이긴 사람에게 배팅필드에 있는 코인이 추가되고 해당 라운드에 있던 카드들을 사용된 카드로 이동,
  
          break
        case "게임 종료":
          // 모두 구독
  
          // 한 사람이 코인을 싹쓰리하거나, 카드 덱이 동나면 게임 종료
          // 게임 최종 결과와 포인트가 응답으로 옴
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
        case "경쟁전 매칭":
          // 방의 인원, 플레이어들의 데이터 등을 받아서 보여줌
          break
        case "채팅":
          // 발행 구독
          // 채팅 로그 갱신
          break
      }
    })
  }

  // 게임방 생성 send 
  const sendCreateRoom = function(title, countOfPeople){
    const message = { countOfPeople }
    stompClient.send(`/to/game/create/${title}`, JSON.stringify(message), headers)
  }

  // 게임방 생성 receive
  const receiveCreateRoom = function(){
    console.log('방 생성 이벤트');
    // 어떤 경로로 생성이 되는가?
    // 1. 방 만들기 버튼으로 방 생성 -> 메인페이지에서 대기페이지로 이동
    // 2. 경쟁전 빠른 시작 API 요청
  }

  // 게임방 입장
  const sendJoinRoom = function(gameRoomTitle){
    stompClient.send(`/to/game/enter/${gameRoomTitle}`, JSON.stringify({}), {"access-token":useStore.accessToken})
  }

  // 게임방 입장
  const receiveJoinRoom = function(){
    console.log('방 입장 이벤트');
  }

  // 게임방 나가기
  const leaveRoom = function(gameRoomTitle){
    stompClient.send(`/to/game/exit/${gameRoomTitle}`, JSON.stringify({}), headers)
  }

  // 게임방 나가기 이벤트
  const leaveRoomEvent = function(){
    console.log('방 나가기 이벤트');
  }

  // 게임 준비
  const ready = function(gameRoomTitle){
    stompClient.send(`/to/game/reddy/${gameRoomTitle}`, JSON.stringify({}), headers)
  }

  // 게임 준비 이벤트
  const readyEvent = function(){
    console.log('게임 준비 이벤트');
  }

  // 게임 시작
  const startGame = function(gameRoomTitle){
    stompClient.send("/startGame", JSON.stringify({}), headers)
  }

  // 배팅
  const bet = function(gameRoomTitle, amount){
    const message = {
      action: "bet",
      amount,
    }
    stompClient.send("/bet", JSON.stringify(message), headers)
  }

  // 게임방 사용자 강퇴
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
    leaveRoom,
    ready,
    startGame,
    bet,
    kickUser,
    subscribeHandler,
    stompClient,
  }
})
