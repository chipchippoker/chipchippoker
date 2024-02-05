import { ref, computed, reactive } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from './user'
import { useGameStore } from './game'
import { compileScript } from 'vue/compiler-sfc'


export const useRoomStore = defineStore('room', () => {
  const userStore = useUserStore()
  const ROOM_API = `${userStore.BASE_API_URL}/rooms`
  const SPECTATE_API = `${userStore.BASE_API_URL}/spectate`

  const router = useRouter()

  const roomType = ref('친선')
  const allRoomList = ref([])

  const pageData = ref(
    {
      "pageNumber": 0, // 현재 내가 요청한 페이지
      "pageSize": 8, // 한 페이지당 보여줄 컨텐츠의 개수 (우리는 8개로 고정)
      "sort": {
          "sorted": false,
          "empty": true,
          "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
  }
  )  // 페이지 데이터
  const isLast = ref(true) // 마지막 페이지인가?
  const isfirst = ref(true) // 첫번째 페이지인가?
  const totalPages = ref(1)  // 전체 몇 페이지로 나뉘는가?
  const totalElements = ref(8) // 전체 페이지의 컨텐츠의 개수
  const nowElements = ref(8) // 현재 페이지의 컨텐츠의 개수
  const nowPage = ref(0)  // 현재 페이지 번호
  const isEmpty = ref(false) // 데이터가 없는가?
  const pageArray = ref([1]) // 페이지네이션 배열
  const emptyList = ref([]) // 페이지에서 빈 곳들
  emptyList.value = Array(8 - allRoomList.value.length).fill(0)
  
  // 방 생성 변수
  const roomManagerNickname = ref('')
  const roomId = ref('')
  const title = ref('')
  const totalParticipantCnt = ref(0)
  const isRoom = ref(false)

  // 관전자 여부, 게임 중 여부, 관전자 닉네임들
  const isWatcher = ref(false)
  const roomState = ref(null)
  const watchersNickname = ref([])


  // 웹소켓
  const gameStore = useGameStore()
  
  // 방 생성
  const createRoom = function(payload){
    console.log('방 생성 요청!!');
    // 방 생성 API 호출
    axios({
      method: 'post',
      url: `${ROOM_API}`,
      headers: { 'access-token': userStore.accessToken },
      data: payload
    })
    // 방 생성 API 응답 & 방 구독 SEND
    .then(response => {
      console.log('방 생성 성공')
      const res = response.data
      roomManagerNickname.value = res.data.roomManagerNickname
      roomId.value = res.data.roomId
      title.value = res.data.title
      totalParticipantCnt.value = res.data.totalParticipantCnt
      gameStore.subscribeHandler(title.value)
    })
    // 방 생성 SEND
    .then(() => {
      gameStore.sendCreateRoom(title.value, totalParticipantCnt.value)
    })
    .catch(err => {
      console.log(err)
      if (err.response.data.code === 'CF005') {
        isRoom.value = true
        console.log(isRoom.value)
        alert('이미 등록된 방 제목입니다.')
      }
      console.log(err);
    })
  }

  // 방 리스트
  const getRoomList = function(payload){
    console.log('방 목록 요청');
    axios({
      method: 'get',
      url: `${ROOM_API}`,
      headers: { 'access-token': userStore.accessToken },
      params:{
        type:payload.type,
        title:payload.title,
        isTwo:payload.isTwo,
        isThree:payload.isThree,
        isFour:payload.isFour,
        isEmpty:payload.isEmpty,
        page:payload.page,
        size:payload.size,
      }
    })
    .then(response => {
      const res = response.data
      allRoomList.value = res.data.content
      pageData.value = res.data.pageable
      isLast.value = res.data.last
      totalElements.value = res.data.totalElements
      totalPages.value = res.data.totalPages
      isfirst.value = res.data.first
      nowElements.value = res.data.numberOfElements
      nowPage.value = res.data.number
      isEmpty.value = res.data.empty
      pageArray.value = Array.from({ length: res.data.totalPages }, (_, i) => i + 1)
      if (totalPages.value === 0) {
        pageArray.value = Array.from({ length: 1 }, (_, i) => i + 1)
    }
    })
    .catch(err => console.log(err))
  }

  // 공개방 입장
  const enterRoomPublic = function (payload) {
    // 공개방 입장 API 호출
    axios({
      method: 'post',
      url: `${ROOM_API}/enter`,
      headers: { 'access-token': userStore.accessToken },
      data: payload
    })
    // 공개방 입장 API 응답 & 방 구독 SEND
    .then(response => {
      const res = response.data
      console.log(res)
      if (res.code === "성공") {
        console.log('성공 if문 안에 들어옴');
        console.log(res.message)
        roomId.value = res.data.roomId
        title.value = res.data.title
        totalParticipantCnt.value = res.data.totalParticipantCnt
        gameStore.subscribeHandler(title.value)
      } else if (res.code === 'FB001') {
        console.log(res.message)
        alert(res.message)
      } else if (res.code === 'FB002') {
        console.log(res.message)        
        alert(res.message)
      } else if (res.code === 'FB003') {
        console.log(res.message)        
        alert(res.message)
      } else if (res.code === 'FB000') {
        console.log(res.message)
        alert(res.message)
      }
      return res.code
    })
    // 방 입장 SEND
    .then((code) => {
      if (code === "성공") {
        gameStore.sendJoinRoom(title.value)
      }
    })
    .catch(err => console.log(err))
  }

  // 비공개방 입장
  const enterRoomPrivate = function(payload) {
    axios({
      method: 'post',
      url: `${ROOM_API}/enter`,
      headers: { 'access-token': userStore.accessToken },
      data: {
        title: payload.title,
        password: payload.password
      }
    })
    .then(response => {
      const res = response.data
      if (res.code === 200) {
        console.log(res.message)
        roomId.value = res.data.roomId
        title.value = res.data.title
        totalParticipantCnt.value = res.data.totalParticipantCnt
        gameStore.subscribeHandler(title.value)
      } else if (res.code === 'FB001') {
        console.log(res.message)
        alert(res.message)
      } else if (res.code === 'FB002') {
        console.log(res.message)        
        alert(res.message)
      } else if (res.code === 'FB003') {
        console.log(res.message)        
        alert(res.message)
      } else if (res.code === 'FB000') {
        console.log(res.message)
        alert('들어갈 수 없는 방입니다')
      }
      return res.code
    })
    .then((code) => {
      // 방 입장 SEND
      if (code === 200) {
        gameStore.sendJoinRoom(title.value)
      }   
    })
    .catch(err => console.log(err))
  }

  // 대기방 나가기
  const leaveRoom = function() {
    console.log('방 나가기 API 호출하러 옴');
    axios({
      method: 'post',
      url: `${ROOM_API}/leave`,
      headers: { 'access-token': userStore.accessToken },
      data: {
        title: title.value
      }
    })
    .then(res => {
      console.log('방나가기 API 호출');
      console.log(res.data)
      roomId.value = ''
      title.value = ''
      totalParticipantCnt.value = 0
      isRoom.value = false
      gameStore.sendExitRoom(title.value)
    })
    .catch(err => console.log(err))
  }

  // 방에서 게임 시작
  const startGame = function(payload) {
    axios({
      method: 'post',
      url: `${ROOM_API}/play`,
      headers: { 'access-token': userStore.accessToken },
      data: payload
    })
    .then(response => {
      const res = response.data
      console.log(res);
      console.log('게임 시작 성공')
      gameStore.sendStartGame(title.value)
    })
    .catch(err => console.log(err))
  }

  // 사용자 강제 퇴장
  const forceMemberOut = function(payload) {
    axios({
      method: 'post',
      url: `${ROOM_API}/member/out`,
      headers: { 'access-token': userStore.accessToken },
      data: payload
    })
    .then(response => {
      const res = response.data
      console.log(response.message)
      if (res.code === 200) {
        gameStore.sendBan(title.value, userStore.myNickname)
      } else if (res.code === 'FB005') {
        alert(res.message)
      }
    })
    .catch(err => console.log(err))
  } 

  // 관전 대기중 입장
  const enterWatch = function (payload) {
    axios({
      method: 'post',
      url: `${SPECTATE_API}/enter`,
      headers: { 'access-token': userStore.accessToken },
      data: payload
    })
    .then(res => {
      if (res.code === 200) {
        console.log('관전 대기중 입장');
        roomId.value = res.data.roomId
        title.value = res.data.title
        roomState.value = res.data.state
        
      } else if (res.code === 'FB001') {
        console.log(res.message)
        alert(res.message)
      }
    })
    .catch(err => console.log(err))
  }

  // 관전 진행중 입장
  const enterWatchProgress = function (payload) {
    axios({
      method: 'post',
      url: `${SPECTATE_API}/enter`,
      headers: { 'access-token': userStore.accessToken },
      data: payload
    })
    .then(res => {
      if (res.code === 200) {
        console.log('관전 진행중 입장');
        roomId.value = res.data.roomId
        title.value = res.data.title
        roomState.value = res.data.state
        
      } else if (res.code === 'FB001') {
        console.log(res.message)
        alert(res.message)
      }
    })
    .catch(err => console.log(err))
  }

  // 관전 나가기
  const leaveWatcher = function () {
    axios({
      method: 'post',
      url: `${SPECTATE_API}/leave`,
      headers: { 'access-token': userStore.accessToken },
    })
    .then(res => {
      console.log('관전 나가기');
      roomId.value = ''
      title.value = ''
      roomState.value = ''
    })
    .catch(err => console.log(err))
  }

  return {
    // 방 목록
    getRoomList,
    allRoomList, pageData, isLast, isfirst, totalPages, totalElements, nowElements, nowPage, isEmpty, pageArray, roomType, emptyList,
    // 방 생성
    createRoom, roomManagerNickname, roomId, title, totalParticipantCnt, isRoom,
    // 방 입장
    enterRoomPublic, enterRoomPrivate, isWatcher, roomState, watchersNickname,
    // 방 나가기
    leaveRoom,
    // 게임 시작하기
    startGame,
    // 사용자 강제 퇴장
    forceMemberOut,
    // 관전
    enterWatch, enterWatchProgress, leaveWatcher,
  }
},{persist:true})
