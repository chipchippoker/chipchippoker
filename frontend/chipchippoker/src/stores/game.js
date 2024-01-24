import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import axios from 'axios'
// import SockJS from 'sockjs-client'
import webstomp, { client } from 'webstomp-client'
import { useUserStore } from './user'

export const useGameStore = defineStore('game', () => {
  const useStore = useUserStore()
  const url = "ws://i10a804.p.ssafy.io:8082/chipchippoker"
  const stompClient = webstomp.client(url)
  const gameRoomTitle = ref('')
  const roomInfo = ref({})
  const headers = {
    "access-token": "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJpZCI6MSwibmlja25hbWUiOiLstZztmITquLAiLCJzdWIiOiJhY2Nlc3MtdG9rZW4iLCJleHAiOjE3MDcyNzg2ODJ9.bjZaHiyqfghkCEQx1tPVsV40FYdLn-fb9XB1SuTju6I"
  }

  stompClient.connect(headers, (frame) => {
    console.log("Connect success", gameRoomTitle.value)
  })

  // 구독 핸들러
  // 토픽 경로 변수로 저장하기
  const subscribeHandler = (gameRoomTitle) => {
    stompClient.subscribe(`/from/chipchippoker/checkConnect/${gameRoomTitle}`, (message) => {
      console.log("subscribe success")
      // 응답 데이터 처리
      const response = JSON.parse(message.body)
      console.log(response.body.data)


      if (response.body.event === '채팅'){
        // 발행 구독
        // 채팅 로그 갱신
      } else if (response.body.event === '방 생성'){
        // 발행 구독
        // 메인페이지 -> 대기페이지로 이동
      } else if (response.body.event === '방 입장'){
        // 발행
        // 메인페이지 -> 대기페이지로 이동
        // 구독
        // 해당 방에 새로운 유저 입장
      } else if (response.body.event === '방 나가기'){
        // 발행
        // 대기페이지 -> 메인페이지로 이동
        // 구독
        // 플레이페이지 -> 메인페이지로 이동
      } else if (response.body.event === '게임 준비'){
        // 발행 구독
        // 해당 유저 게임 준비 상태로 변경 ( 캠 테두리 효과 )
      } else if (response.body.event === '게임 시작'){
        // 구독자
        // 게임이 시작되고 플레이어의 각 코인을 25개로 저장
        // 플레이어의 수에 따라 카드 세트를 조정
      } else if (response.body.event === '배팅'){
        // 모든 플레이어의 코인개수가 같아지거나 한명빼고 모두 폴드일때까지 진행

        // 발행자
        // 제한시간 내에 배팅이나 폴드를 누르면
        // 배팅 코인 개수를 플레이어의 코인에서 빼고 배팅필드 코인에 추가함
        // 폴드를 했으면 폴드처리

        // 구독자
        // 해당 턴인 플레이어의 코인의 변동을 확인
      } else if (response.body.event === '한 라운드 종료'){
        // 모두 구독

        // 모든 플레이어의 코인개수가 같아지거나 한명빼고 모두 폴드일때 응답이 옴
        // 각자의 카드데이터를 받아 오픈
        // 이긴 사람에게 배팅필드에 있는 코인이 추가되고 해당 라운드에 있던 카드들을 사용된 카드로 이동,

      } else if (response.body.event === '게임 종료'){
        // 모두 구독

        // 한 사람이 코인을 싹쓰리하거나, 카드 덱이 동나면 게임 종료
        // 게임 최종 결과와 포인트가 응답으로 옴
      } else if (response.body.event === '강퇴'){
        // 발행자(방장), 구독자(본인)
        // 어느 사용자가 강퇴되었는지 보여줌
        // 구독자(타인)
        // 강퇴되었음을 보여줌
        // unsubscribe 시켜줌
      } else if (response.body.event === '경쟁전 매칭'){
        // 방의 인원, 플레이어들의 데이터 등을 받아서 보여줌
      } 
    })
  }

  // 게임방 생성 요청
  const createRoom = function(title, countOfPeople){
    gameRoomTitle.value = title
    console.log('게임방 생성 요청');
    const message = { countOfPeople }
    stompClient.send(`/to/game/create/${title}`, JSON.stringify(message), headers)
  }

  // 게임방 생성 이벤트
  const createRoomEvent = function(){
    console.log('방 생성 이벤트');
  }

  // 게임방 입장
  const joinRoom = function(gameRoomTitle){
    stompClient.send(`/to/game/enter/${gameRoomTitle}`, JSON.stringify({}), headers)
  }

  // 게임방 입장 이벤트
  const joinRoomEvent = function(){
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
  const kickUser = function(gameRoomTitle, kickedUser){
    const message = {
      action: "kickUser",
      kickedUser,
    }
    stompClient.send("/kickUser", JSON.stringify(message), headers)
  }

  return {
    rooms: ref([]),
    createRoom,
    joinRoom,
    leaveRoom,
    ready,
    startGame,
    bet,
    kickUser,
    subscribeHandler,
    stompClient,
  }
})
