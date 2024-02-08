import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'

import webstomp from 'webstomp-client'
import { useUserStore } from './user'
import { useRoomStore } from './room'
import { useMatchStore } from './match'
import { useOpenviduStore } from './openvidu'
import { faL } from '@fortawesome/free-solid-svg-icons'


export const useGameStore = defineStore('game', () => {
  const userStore = useUserStore()
  const roomStore = useRoomStore()
  const matchStore = useMatchStore()
  const openviduStore = useOpenviduStore()
  const router = useRouter()

  const url = `wss://chipchippoker.shop/chipchippoker`

  const stompClient = webstomp.client(url)

  // 구독 정보
  const subscriptionGame = ref(undefined)
  const subscriptionPrivate = ref(undefined)

  // 게임 정보
  const receiveMessage = ref('')
  // 게임 종료 결과 리스트
  const memberEndGameInfos = ref([])
  // 라운드 승자
  const winnerNickname = ref('')
  // 게임 방 정보
  const roomInfo = ref({})
  const gameRoomTitle = ref('')
  const memberInfos = ref([])  // 방에 있는 플레이어들 닉네임 순서
  const countOfPeople = ref(0)
  const totalCountOfPeople = ref(0)

  const myOrder = ref(0)
  const roomManagerNickname = ref('')
  const myPrivateSubId = ref('')
  const myGameSubId = ref('')
  const isMatch = ref(false)


  const isMatchStart = ref(false) // 매치로 시작하는 게임인가
  // 게임 상태 변수
  const firstStart = ref(false)
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


  // 웹소켓 연결 끊김 이벤트
  stompClient.ws.onclose = function (event) {
    console.log('웹소켓 연결 끊김 감지')
  }
  console.log(stompClient)

  // connectHandler()
  const connectHandler = function () {
    stompClient.connect({ 'access-token': userStore.accessToken }, (frame) => {
      console.log("Connect success", gameRoomTitle.value)
      console.log();
      // stompClient.heartbeat.outgoing = 5000;
      // stompClient.heartbeat.incoming = 0;
      console.log(stompClient);
      // 개인 메세지함 구독(기본구독)
      subscriptionPrivate.value = stompClient.subscribe(`/from/chipchippoker/member/${userStore.myNickname}`, (message) => {

        console.log('개인 메세지함 구독 성공')
        const response = JSON.parse(message.body).body
        console.log(response)

        // 개인메세지함 구독 아이디
        myPrivateSubId.value = message.headers.subscription

        switch (response.code) {

          case "MB004":
            console.log('현재 진행 중인 라운드와 일치하지 않습니다.')
            notMatchRound.value = true
            break

          case "MB005":
            console.log('본인의 차례가 아닙니다.')
            notYourTurn.value = true
            break

          case "MB006":
            console.log('배팅이 불가능합니다.')
            cannotBat.value = true
            break

          case "MB008":
            console.log('혼자서는 게임이 불가합니다.')
            alert(response.message)
            break


          case "MS005": // 강퇴(본인)
            console.log(response.message);
            receiveBanMe(response.message)
            break

          case "MS007": // 게임 진행
            // 맨 처음 데이터 저장시에만 0.1초 미루기
            if (firstStart.value === false) {
              firstStart.value = true
              setTimeout(() => {
                roundState.value = response.data.roundState
                currentRound.value = response.data.currentRound
                yourTurn.value = response.data.yourTurn
                gameMemberInfos.value = response.data.gameMemberInfos

              }, 100)
            } else if(roundState.value === false) {
              // 새로운 라운드 저장할때는 2초 미루기
              setTimeout(() => {
                roundState.value = response.data.roundState
                currentRound.value = response.data.currentRound
                yourTurn.value = response.data.yourTurn
                gameMemberInfos.value = response.data.gameMemberInfos
                console.log('라운드 저장 2초 미룸');

              }, 2000)
            } else{
              // 배팅은 1초 미루기
              setTimeout(() => {
                roundState.value = response.data.roundState
                currentRound.value = response.data.currentRound
                yourTurn.value = response.data.yourTurn
                gameMemberInfos.value = response.data.gameMemberInfos
                console.log('배팅 저장1초 미룸');
              }, 1000)

            }
            break
          case "MS008": // 라운드 종료
            receiveRoundFinish(response.data)
            break
          case "MS009": // 경쟁 게임 종료
            receiveGameFinishRank(response.data)
            break
          case "MS010": // 친선 게임 종료
            receiveGameFinishFriend(response.data)
            break
          case "MS012": // 친구요청 받음
            isAlarmArrive.value = true
            break

          case "ME003": // 방장이 아닌사람이 start 한다면
            console.log("님은 방장이 아님");
            break
        }
      })
    })
  }

  // 구독 핸들러
  const subscribeHandler = (gameRoomTitle) => {
    // 토픽 구독 및 수신
    subscriptionGame.value = stompClient.subscribe(`/from/chipchippoker/checkConnect/${gameRoomTitle}`, (message) => {
      console.log('방 구독하기');
      console.log("subscribe success")
      // 내 구독 아이디 저장 

      myGameSubId.value = message.headers.subscription
      console.log(message.headers);
      const response = JSON.parse(message.body).body
      console.log(response)

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
          console.log(response)
          receiveBanYou(response.data)
          break
        case "MS006": // 게임 준비 완료
          receiveReady(response.data)
          break
        case "MS008": // 라운드 종료
          receiveRoundFinish(response.data)
          break
        case "MS009": // 경쟁 게임 종료
          receiveGameFinishRank(response.data)
          break
        case "MS010": // 친선 게임 종료
          receiveGameFinishFriend(response.data)
          break
        case "MB002": // 모두 준비상태가 아님
          console.log('모두 준비 상태가 아닙니다.');
          alert(response.message)
          break
        case "MB003": // 방장이 아님

        case "MS016": // 게임방 시작
          receiveStartGame(response)
          break
        case "MS011": // 매칭
          console.log(response.message)
          receiveMatching(response.data)
        case "MS013": // 관전자 입장
          console.log(response.message);
          receiveSpectaionRoom(response.data)
          break
        case "MS014": // 관전자 퇴장
          console.log(response.message);
          receiveSpectaionExit(response.data)
          break
      }
    })
  }

  // 게임 매칭 SEND
  const sendMatching = function (title, countOfPeople) {
    gameRoomTitle.value = title
    totalCountOfPeople.value = countOfPeople
    console.log(title, countOfPeople)
    stompClient.send(`/to/game/matching/${title}`, JSON.stringify({ countOfPeople }), { 'access-token': userStore.accessToken })
  }

  // 게임 매칭 RECEIVE
  const receiveMatching = function (data) {
    // 다 모이면 플레이 페이지로 이동
    console.log(data);
    memberInfos.value = data.memberInfos
    console.log(memberInfos.value)
    console.log(data.memberInfos.length, totalCountOfPeople.value);
    // 매치 인원이 다 모이면
    if (data.memberInfos.length === totalCountOfPeople.value) {
      matchStore.isSearching = false
      console.log('매치 성공!!')
      isMatchStart.value = true   // 매치로 인한 게임 시작
      roomStore.startGame(matchStore.title) // 게임 시작 send
    } else {
      console.log('매칭 중')
      matchStore.isSearching = true
    }
  }

  // 방 생성 SEND
  const sendCreateRoom = function (title, countOfPeople) {
    console.log(title, countOfPeople)
    stompClient.send(`/to/game/create/${title}`, JSON.stringify({ countOfPeople }), { 'access-token': userStore.accessToken })
  }

  // 방 생성 RECEIVE
  const receiveCreateRoom = function (data) {
    countOfPeople.value = data.countOfPeople
    memberInfos.value = data.memberInfos
    roomManagerNickname.value = data.roomManagerNickname
    gameRoomTitle.value = roomStore.title
    router.push({
      name: 'wait',
      params: { roomId: roomStore.roomId },
    })
  }

  // 게임방 입장 SEND
  const sendJoinRoom = function (gameRoomTitle) {
    stompClient.send(`/to/game/enter/${gameRoomTitle}`, JSON.stringify({}), { 'access-token': userStore.accessToken })
  }

  // 게임방 입장 RECEIVE
  const receiveJoinRoom = function (data) {
    console.log('★★★★★★★★★★★★★★★★★★★★★');
    console.log('입장시 데이터', data);
    console.log('★★★★★★★★★★★★★★★★★★★★★');
    countOfPeople.value = data.countOfPeople
    memberInfos.value = data.memberInfos
    roomManagerNickname.value = data.roomManagerNickname
    console.log('저장된 매니저 닉네임', roomManagerNickname.value);
    gameRoomTitle.value = roomStore.title
    router.push({
      name: 'wait',
      params: { roomId: roomStore.roomId },
    })
  }

  // 게임방 나가기 SEND
  const sendExitRoom = function (gameRoomTitle) {
    stompClient.send(`/to/game/exit/${gameRoomTitle}`, JSON.stringify({}), { 'access-token': userStore.accessToken })
  }

  // 손 봐야함
  // 게임방 나가기 RECEIVE
  const receiveExitRoom = function (data) {
    countOfPeople.value = data.countOfPeople
    roomManagerNickname.value = data.roomManagerNickname
    memberInfos.value = data.memberInfos
    subscriptionGame.value.unsubscribe(myGameSubId.value)
  }

  // 게임 준비 SEND
  const sendReady = function (gameRoomTitle, isReady) {
    stompClient.send(`/to/game/ready/${gameRoomTitle}`, JSON.stringify({ isReady }), { 'access-token': userStore.accessToken })
  }

  // 게임 준비 RECEIVE
  const receiveReady = function (data) {
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
    console.log('receiveStartGame', body)
    router.push({
      name: 'play',
      params: { roomId: roomStore.roomId },
    })
    firstStart.value = false
  }

  // 배팅
  const bet = function (gameRoomTitle, action, bettingCoin) {
    console.log('배팅하기!!!!!!!!!!!!!!!!!!!!!!!!!');
    const message = {
      currentRound: currentRound.value,
      action: action,
      bettingCoin: bettingCoin
    }
    stompClient.send(`/to/game/betting/${gameRoomTitle}`, JSON.stringify(message), { 'access-token': userStore.accessToken })
  }

  // 강퇴 send
  const sendBan = function (gameRoomTitle, nickname) {
    stompClient.send(`/to/game/ban/${gameRoomTitle}`, JSON.stringify({ nickname }), { 'access-token': userStore.accessToken })
  }

  // 강퇴 타인
  const receiveBanYou = function (data) {
    countOfPeople.value = data.countOfPeople
    memberInfos.value = data.memberInfos
    roomManagerNickname.value = data.roomManagerNickname
  }
  // 강퇴 본인
  const receiveBanMe = function (message) {
    // 게임 관련 데이터 초기화 시켜주기
    resetGameStore()
    openviduStore.leaveSession()
    router.push({ name: 'main' })
    alert(message)
  }

  // 라운드 종료
  const receiveRoundFinish = function (data) {
    console.log('라운드 종료');
    roundState.value = data?.roundState
    currentRound.value = data?.currentRound
    yourTurn.value = data?.yourTurn
    gameMemberInfos.value = data?.gameMemberInfos
    winnerNickname.value = data?.winnerNickname
    console.log("roundState", roundState.value);
    console.log("currentRound", currentRound.value);
    console.log("yourTurn", yourTurn.value);
    console.log("gameMemberInfos", gameMemberInfos.value);
  }

  // 경쟁 게임 종료
  const receiveGameFinishRank = function (data) {
    memberEndGameInfos.value = data.memberEndGameInfos
  }
  // 친선 게임 종료
  const receiveGameFinishFriend = function (data) {
   memberEndGameInfos.value = data.memberEndGameInfos
  }

  // 친구신청 Send
  const sendFriendRequest = function (nickname) {
    stompClient.send("/to/friend/request", JSON.stringify({ "nickname": nickname }), { 'access-token': userStore.accessToken })
  }
  // 방 나가기 시 초기화 함수
  const resetGameStore = function () {
    winnerNickname.value = ''
    subscriptionGame.value = undefined
    roomInfo.value = {}
    totalCountOfPeople.value = 0
    myOrder.value = 0
    memberInfos.value = []
    myPrivateSubId.value = ''
    gameRoomTitle.value = ''
    roomManagerNickname.value = ''
    countOfPeople.value = 0
    myGameSubId.value = ''
    isMatch.value = false
    roundState.value = false
    currentRound.value = 0
    yourTurn.value = null
    gameMemberInfos.value = []
    bettingCoin.value = 0
    subscriptionSpectation.value = undefined
    mySpectateSubId.value = []
    watchersNickname.value = []
    memberEndGameInfos.value = []
  }



  //---------------------------------------------------관전--------------------------------------------------------
  // 관전 구독 정보
  const subscriptionSpectation = ref(undefined)

  // 관전 정보
  const mySpectateSubId = ref('')
  const watchersNickname = ref([])



  // 관전 구독 핸들러
  const spectateHandler = (gameRoomTitle) => {
    // 토픽 구독 및 수신
    subscriptionSpectation.value = stompClient.subscribe(`/from/chipchippoker/spectation/checkConnect/${gameRoomTitle}`, (message) => {
      console.log('관전 구독하기');
      // 내 구독 아이디 저장 

      mySpectateSubId.value = message.headers.subscription
      console.log(message.headers);
      const response = JSON.parse(message.body).body
      console.log(response)

      switch (response.code) {
        case "MS013": // 관전자 입장
          console.log(response.message);
          receiveSpectaionRoom(response.data)
          break
        case "MS014": // 관전자 퇴장
          console.log(response.message);
          receiveSpectaionExit(response.data)
          break
      }
    })
  }

  // 관전 SEND
  const sendSpectationRoom = function (gameRoomTitle, gameState) {
    stompClient.send(`/to/spectation/enter/${gameRoomTitle}`, JSON.stringify({ gameState }), { 'access-token': userStore.accessToken })
  }

  // 관전 RECEIVE
  const receiveSpectaionRoom = function (data) {
    watchersNickname.value = data.spectatorList
    memberInfos.value = data.memberInfos
  }

  // 관전 나가기 SEND
  const sendSpectationExit = function (gameRoomTitle) {
    console.log(gameRoomTitle);
    console.log(userStore.accessToken);
    stompClient.send(`/to/spectation/exit/${gameRoomTitle}`, JSON.stringify({}), { 'access-token': userStore.accessToken })
  }

  // 관전 나가기 RECEIVE
  const receiveSpectaionExit = function (data) {
    watchersNickname.value = data.spectatorList
  }
  //---------------------------------------------------관전--------------------------------------------------------


  return {
    // State
    rooms: ref([]),
    stompClient,
    roundState,
    currentRound,
    yourTurn,
    gameMemberInfos,
    winnerNickname,
    memberInfos,
    memberEndGameInfos,
    roomManagerNickname,
    resetGameStore,

    // 구독 정보
    subscriptionGame, myGameSubId,

    // 게임 상태 변수
    firstStart,

    // 관전 정보
    spectateHandler, sendSpectationRoom, receiveSpectaionRoom, watchersNickname, sendSpectationExit, receiveSpectaionExit,

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
    isAlarmArrive,
    // 배팅 시 오류 메세지
    notMatchRound,
    notYourTurn,
    cannotBat,
    indexing,
    myOrder,
    connectHandler
  }

}, { persist: true }) 
