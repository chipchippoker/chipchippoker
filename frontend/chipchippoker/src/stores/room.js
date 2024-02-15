import { ref, computed, reactive } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from './user'
import { useGameStore } from './game'
import { compileScript } from 'vue/compiler-sfc'
import { useOpenviduStore } from './openvidu'

export const useRoomStore = defineStore('room', () => {
  const userStore = useUserStore()
  const openviduStore = useOpenviduStore()
  const ROOM_API = `${userStore.BASE_API_URL}/rooms`
  const SPECTATE_API = `${userStore.BASE_API_URL}/spectate`

  const router = useRouter()

  const roomType = ref('친선')
  const gameType = ref('친선전')
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

  // 관전자 여부, 게임 중 여부
  const isWatcher = ref(false)
  const roomState = ref(null)

  // 웹소켓
  const gameStore = useGameStore()
  
  // 방 생성
  const createRoom = function(payload){
    // 방 생성 API 호출
    axios({
      method: 'post',
      url: `${ROOM_API}`,
      headers: { 'access-token': userStore.accessToken },
      data: payload
    })
    // 방 생성 API 응답 & 방 구독 SEND
    .then(response => {
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
        alert('이미 등록된 방 제목입니다.')
      }
    })
  }

  // 방 리스트
  const getRoomList = function(payload){
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

  // 방 입장
  const enterRoom = function(payload) {
    // 방 입장 API 호출
    axios({
      method: 'post',
      url: `${ROOM_API}/enter`,
      headers: { 'access-token': userStore.accessToken },
      data: payload
    })
    // 방 입장 API 응답 & 방 구독 SEND
    .then(response => {
      const res = response.data
      if (res.code === "성공") {
        roomId.value = res.data.roomId
        title.value = res.data.title
        totalParticipantCnt.value = res.data.totalParticipantCnt
        gameStore.subscribeHandler(title.value)
      } 
      return res.code
    })
    // 방 입장 SEND
    .then((code) => {
      if (code === "성공") {
        gameStore.sendJoinRoom(title.value)
      }
    })
    .catch(err => {
      console.log(err)
      if (err.response.data.code === 'FB001') {
        alert(err.response.data.message)
      } else if (err.response.data.code === 'FB002') {
        alert(err.response.data.message)
      } else if (err.response.data.code === 'FB003') {
        alert(err.response.data.message)
      } else if (err.response.data.code === 'FB000') {
        alert(err.response.data.message)
      } else if (err.response.data.code === 'FB010') {
        alert(err.response.data.message)
      }
      window.location.reload();
    })
  }


  // 방 나가기
  const leaveRoom = function() {
    axios({
      method: 'post',
      url: `${ROOM_API}/leave`,
      headers: { 'access-token': userStore.accessToken },
      data: {
        title: title.value
      }
    })
    .then(res => {
      gameStore.sendExitRoom(title.value)
      roomId.value = ''
      title.value = ''
      totalParticipantCnt.value = 0
      isRoom.value = false
    })
    .then(()=>{
      openviduStore.leaveSession()
      gameStore.resetGameStore()
      router.push({name:'main'})
      // 새로고침 횟수를 삭제합니다.
      localStorage.removeItem('refreshCount')
    })
    .catch(err => {
      console.log(err)
      openviduStore.leaveSession()
      gameStore.resetGameStore()
      router.push({name:'main'})
    })
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
      if (response.data.code === '성공') {
        gameStore.sendBan(title.value, payload.nickname)
      } else if (response.data.code === 'FB005') {
        alert(res.message)
      }
    })
    .catch(err => console.log(err))
  } 

  // 관전 입장
  const enterWatch = function (payload) {
    axios({
      method: 'post',
      url: `${SPECTATE_API}/enter`,
      headers: { 'access-token': userStore.accessToken },
      data: payload
    })
    .then(res => {
      if (res.data.code === '성공') {
        roomId.value = res.data.data.roomId
        title.value = res.data.data.title
        roomState.value = res.data.data.state
        gameStore.spectateHandler(title.value)
      } 
      return res.data
    })
    .then(res => {
      gameStore.sendSpectationRoom(res.data.title, res.data.state)
      if (roomState.value === '대기') {
        router.push({
          name:'wait',
          params: { roomId: res.data.roomId },
        })
      } else {
        router.push({
          name:'play',
          params: { roomId: res.data.roomId },
        })
      }
    })
    .catch(err => {
      console.log(err)
      if (err.response.data.code === 'FB001') {
        alert(err.response.data.message)
      } else if (err.response.data.code === 'FB011') {
        alert(err.response.data.message)
      }
    })
  }


  // 관전 나가기
  const leaveWatcher = function () {
    axios({
      method: 'post',
      url: `${SPECTATE_API}/leave`,
      headers: { 'access-token': userStore.accessToken },
    })
    .then(res => {
      gameStore.sendSpectationExit(title.value)
      roomId.value = ''
      title.value = ''
      roomState.value = ''
      isWatcher.value = false
    })
    .then(res => {
      router.push({name:'main'})
      openviduStore.leaveSession()
      gameStore.resetGameStore()
      // 새로고침 횟수를 삭제합니다.
      localStorage.removeItem('refreshCount')
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
    enterRoom, isWatcher, roomState,
    // 방 나가기
    leaveRoom,
    // 게임 시작하기
    startGame,
    // 사용자 강제 퇴장
    forceMemberOut,
    // 관전
    enterWatch, leaveWatcher,

  }
},{persist:true})
