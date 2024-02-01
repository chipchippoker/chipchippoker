import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import axios from 'axios'

import webstomp from 'webstomp-client'
import { useUserStore } from './user'
import { useRoomStore } from './room'
import { useMatchStore } from './match'  

export const useGameStore = defineStore('game', () => {
  const userStore = useUserStore()
  const roomStore = useRoomStore()
  const matchStore = useMatchStore()
  const router = useRouter()

  const url = `wss://i10a804.p.ssafy.io/chipchippoker`

  const stompClient = webstomp.client(url)
  const subscriptions = ref([])
  // 게임 정보
  const receiveMessage = ref('')
  
  // 게임 방 정보
  const roomInfo = ref({})
  const gameRoomTitle = ref('')
  const memberInfos = ref([])  // 방에 있는 플레이어들 닉네임 순서
  const countOfPeople = ref(0)

  // const totalParticipantCnt = ref(0)
  const myOrder = ref(0)
  const roomManagerNickname = ref('')
  const myId = ref('')
  const isMatch = ref(false)

  const indexing = function (nickname) {
    memberInfos.value.forEach((member, index) => {
      if (member.nickname === nickname) {
        myOrder.value = index
      }
    })
  }

  // 게임 관련 데이터 -> 라운드상태, 최근라운드, 턴, 게임 멤버 정보, 배팅 정보
  const roundState = ref(false)
  const currentRound = ref(0)
  const yourTurn = ref(null)
  const gameMemberInfos = ref([])
  const bettingCoin = ref(0)
  console.log(stompClient.ws);

  // 배팅 잘못했을 때 모달
  const notMatchRound = ref(false)
  const notYourTurn = ref(false)
  const cannotBat = ref(false)
  
  // 친구신청 알림
  const isAlarmArrive = ref(false)

  // stompClient.ws.onclose = event => {
  //   alert("WebSocket connection closed");
  //   console.log("WebSocket connection closed");
  //   stompClient = webstomp.client("wss://i10a804.p.ssafy.io/chipchippoker")

  //   // 재연결 로직
  //   setTimeout(function() {
  //     stompClient.connect({ 'access-token': userStore.accessToken, 'heart-beat':'2000,5000' }, (frame) => {console.log('재연결 성공');})
  //   }, 100); // 0.1초 후에 다시 연결을 시도합니다.
  // }

  stompClient.connect({ 'access-token': userStore.accessToken}, (frame) => {
    console.log("Connect success", gameRoomTitle.value)

    stompClient.reconnect_delay = 2000
    // stompClient.heartbeat.outgoing = 2000;
    // stompClient.heartbeat.incoming = 0;
    console.log(stompClient);
    // 개인 메세지함 구독(기본구독)
    const subscription = stompClient.subscribe(`/from/chipchippoker/member/${userStore.myNickname}`, (message) => {

      console.log('개인 메세지함 구독 성공')
      const response = JSON.parse(message.body).body
      console.log(response)

      switch (response.code) {

        case "MS004":
          console.log('현재 진행 중인 라운드와 일치하지 않습니다.')
          notMatchRound.value = true
          break

        case "MS005":
          console.log('본인의 차례가 아닙니다.')
          notYourTurn.value = true
          break

        case "MS006":
          console.log('배팅이 불가능합니다.')
          cannotBat.value = true
          break

        case "MS007": // 게임 진행
          // 게임 데이터 저장 -> 5초건 텀 두고 데이터 받기..
          setTimeout(()=>{
            roundState.value = response.data.roundState
            currentRound.value = response.data.currentRound
            yourTurn.value = response.data.yourTurn
            gameMemberInfos.value = data.gameMemberInfos
            for (let i = 0; i < memberInfos.value.length; i++) {
              // 플레이어 순서에 맞게 데이터 넣기
              const item = response.data.gameMemberInfos.filter((p)=>
              {p.nickname === memberInfos.value[i]})
              gameMemberInfos.value.push(item)
            }
      
            console.log('게임시작');
            console.log("roundState", roundState.value);
            console.log("currentRound", currentRound.value);
            console.log("yourTurn", yourTurn.value);
            console.log("gameMemberInfos", gameMemberInfos.value);
          },5000)
          break
        case "MS012": // 친구요청 받음
          isAlarmArrive.value = true
          break

        case "ME002": // 모두 준비상태가 아닙니다
          console.log("모두 준비상태가 아닙니다");
          break
        
        case "ME003": // 방장이 아닌사람이 start 한다면
          console.log("님은 방장이 아님");
          break
      }
    })
    subscriptions.value.push(subscription)
  })

  // 구독 핸들러
  const subscribeHandler = (gameRoomTitle) => {
    
    // 토픽 구독 및 수신
    const subscribtion = stompClient.subscribe(`/from/chipchippoker/checkConnect/${gameRoomTitle}`, (message) => {
      console.log('방 구독하기');
      console.log("subscribe success")
      // 내 구독 아이디 저장 
      
      myId.value = message.headers.subscription
      console.log(myId.value);
      console.log(message.headers);
      const response = JSON.parse(message.body).body
      console.log(response.body)

      switch (response.code) {
        case "MS001": // 방 생성
          console.log(response.message);
          receiveCreateRoom(response.data)
          break
        case "MS002": // 방 입장
          console.log(response.message);
          receiveJoinRoom(response.data)
          break
        case "MS003": // 방 나가기
          console.log(response.message);
          receiveExitRoom(response.data) 
          break     
        case "MS004": // 강퇴(타인)
          console.log(response.member)
          receiveBanYou(response.data)
          break
        case "MS005": // 강퇴(본인)
          console.log(response.member);
          receiveBanMe(response.data)
          break
      case "MS006": // 게임 준비 완료
        receiveReady(response.data)
        break
      case "MS007": // 게임 시작 성공
      case "ME002": // 모두 준비상태가 아님
      case "ME003": // 방장이 아님
        receiveStartGame(response)
        break
      case "MS008": // 라운드 종료
        receiveGameFinish(receiveMessage.value?.data)
        break
      case "MS011": // 매칭
        console.log(response.message)
        receiveMatching(response.data)
      }
    })
    subscriptions.value.push(subscribtion)
  }
  
  // 게임 매칭 SEND
  const sendMatching = function(title, countOfPeople){
    isMatch.value = true
    gameRoomTitle.value = title
    countOfPeople.value = countOfPeople
    console.log(title, countOfPeople)
    stompClient.send(`/to/game/matching/${title}`, JSON.stringify({countOfPeople}), {'access-token': userStore.accessToken})
  }

  // 게임 매칭 RECEIVE
  const receiveMatching = function(data){
    // 다 모이면 플레이 페이지로 이동
    console.log(data);
    memberInfos.value = data.memberInfos
    console.log(memberInfos.value);
    if (data.memberInfos.length === countOfPeople.value) {
      isMatch.value = false
      console.log('매치 성공!!');
      router.push({
        name:'play',
        params: { roomId: matchStore.roomId },
      })
    }
  }

  // 방 생성 SEND
  const sendCreateRoom = function(title, countOfPeople){
    console.log(title, countOfPeople)
    stompClient.send(`/to/game/create/${title}`, JSON.stringify({countOfPeople}), {'access-token': userStore.accessToken})
  }

  // 방 생성 RECEIVE
  const receiveCreateRoom = function(data){
    countOfPeople.value = data.countOfPeople
    memberInfos.value = data.memberInfos
    roomManagerNickname.value = data.roomManagerNickname
    gameRoomTitle.value = roomStore.title
    router.push({
      name:'wait',
      params: { roomId: roomStore.roomId },
    })
  }

  // 게임방 입장 SEND
  const sendJoinRoom = function(gameRoomTitle){
    stompClient.send(`/to/game/enter/${gameRoomTitle}`, JSON.stringify({}), {'access-token': userStore.accessToken})
  }

  // 게임방 입장 RECEIVE
  const receiveJoinRoom = function(data){
    countOfPeople.value = data.countOfPeople
    memberInfos.value = data.memberInfos
    roomManagerNickname.value = data.roomManagerNickname
    gameRoomTitle.value = roomStore.title
    router.push({
      name:'wait',
      params: { roomId: roomStore.roomId },
    })
  }

  // 게임방 나가기 SEND
  const sendExitRoom = function (gameRoomTitle) {
    stompClient.send(`/to/game/exit/${gameRoomTitle}`, JSON.stringify({}), { 'access-token': userStore.accessToken })
  }

  // 손 봐야함
  // 게임방 나가기 RECEIVE
  const receiveExitRoom = function(data){
    // 내가 나갈때
    if(data.memberInfos.nickname === userStore.myNickname){
      // 게임 관련 데이터 초기화 시켜주기
      memberInfos.value = []
      gameRoomTitle.value = ''
      roomManagerNickname.value = ''
      countOfPeople.value = 0
      myId.value = ''
      isMatch.value = false
      roundState.value = false
      currentRound.value = 0
      yourTurn.value = null
      gameMemberInfos.value = []
      bettingCoin.value = 0
      // 구독 취소하고 메인페이지로 보내기
      mySubscribtion.unsubscribe(myId.value)
      router.push('main')
    } else {
      countOfPeople.value = data.countOfPeople
      roomManagerNickname.value = data.roomManagerNickname
      memberInfos.value = data.memberInfos
      gameMemberInfos.value = 
      // 나가는 사람 memberInfos와 gameMemberInfos에서 삭제
      memberInfos.value.forEach((memeber, index) => {
        if (memeber && !data.memberInfos.find(info => info.nickname === memeber)) {
          memberInfos.value.splice(index, 1)
          gameMemberInfos.value.splice(index, 1)
        }
      })
    }    
  }

  // 게임 준비 SEND
  const sendReady = function (gameRoomTitle, isReady) {
    stompClient.send(`/to/game/ready/${gameRoomTitle}`, JSON.stringify({isReady}), { 'access-token': userStore.accessToken })
  }

  // 게임 준비 RECEIVE
  const receiveReady = function(data){
    countOfPeople.value = data.countOfPeople
    memberInfos.value = data.memberInfos
    roomManagerNickname.value = data.roomManagerNickname
  }

  // 게임 시작 SEND
  const sendStartGame = function (gameRoomTitle) {
    stompClient.send(`/to/game/start/${gameRoomTitle}`, JSON.stringify({}), { 'access-token': userStore.accessToken })
  }

  // 게임 시작 RECEIVE
  const receiveStartGame = function (body) {
    if (body.code === 'MS007'){  // 게임 진행
      roundState.value = body.data.roundState
      currentRound.value = body.data.currentRound
      yourTurn.value = body.data.yourTurn
      gameMemberInfos.value = body.data.gameMemberInfos
      router.push({
        name:'play',
        params: { roomId: roomStore.roomId },
      })
    } else if (body.code === 'ME002') {  // 준비 안됨
      alert(body.message)
    } else if (body.code === 'ME003') {  // 방장 아님
      alert(body.message)
    }
  }

  // 배팅
  const bet = function (gameRoomTitle, action, bettingCoin ) {
    const message = {
      currentRound:currentRound.value,
      action: action,
      bettingCoin:bettingCoin
    }
    stompClient.send(`/to/game/betting/${gameRoomTitle}`, JSON.stringify(message), { 'access-token': userStore.accessToken })
  }

  // 강퇴 send
  const sendBan = function (gameRoomTitle, nickname) {
    stompClient.send(`/to/game/ban/${gameRoomTitle}`, JSON.stringify({nickname}), { 'access-token': userStore.accessToken })
  } 
  
  // 강퇴 타인
  const receiveBanYou = function (data) {
    countOfPeople.value = data.countOfPeople
    memberInfos.value = data.memberInfos
    roomManagerNickname.value = data.roomManagerNickname
  }
  // 강퇴 본인
  const receiveBanMe = function () {
    countOfPeople.value = 0
    memberInfos.value = []
    roomManagerNickname.value = ''
    mySubscribtion.unsubscribe(myId.value)
    myId.value = null
    router.push('main')
  }
  
  // 라운드 종료
  const receiveGameFinish = function(data){
    console.log('라운드 종료');
    roundState.value = data?.roundState
    currentRound.value = data?.currentRound
    yourTurn.value = data?.yourTurn
    gameMemberInfos.value = data?.gameMemberInfos
    console.log("roundState", roundState.value);
    console.log("currentRound", currentRound.value);
    console.log("yourTurn", yourTurn.value);
    console.log("gameMemberInfos", gameMemberInfos.value);
  }
  
  // 친구신청 Send
  const sendFriendRequest = function(nickname){
    stompClient.send("/to/friend/request", JSON.stringify({"nickname":nickname}), { 'access-token': userStore.accessToken })
  }
  
  return {
    // State
    rooms: ref([]),
    stompClient,
    roundState,
    currentRound,
    yourTurn,
    gameMemberInfos,
    memberInfos,



    // Action

    receiveMessage,
    sendMatching, receiveMatching,
    sendCreateRoom, receiveCreateRoom,
    sendJoinRoom, receiveJoinRoom,
    sendExitRoom, receiveExitRoom,
    sendReady, receiveReady,
    sendBan, receiveBanMe, receiveBanYou,
    sendStartGame, receiveStartGame,
    sendFriendRequest,
    bet,
    subscribeHandler,
    stompClient,
    roundState,
    currentRound,
    yourTurn,
    gameMemberInfos,
    bettingCoin,
    isAlarmArrive,
    // 배팅 시 오류 메세지
    notMatchRound,
    notYourTurn,
    cannotBat,
    indexing,
    myOrder,
  }
}, { persist: true }) 
