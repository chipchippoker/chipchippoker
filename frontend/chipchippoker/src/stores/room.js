import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from './user'
import { useGameStore } from '@/stores/game'

const ROOM_API = 'https://i10a804.p.ssafy.io/api/rooms'

export const useRoomStore = defineStore('room', () => {
  const allRoomList = ref([
    {
      "isPrivate": true,
      "state": "대기",
      "title": "테스트 방1",
      "totalParticipantCnt": 2,
      "currentParticipantsCnt": 2,
      "currentSpectatorsCnt": 0
  },
  {
      "isPrivate": false,
      "state": "대기",
      "title": "테스트 방2",
      "totalParticipantCnt": 2,
      "currentParticipantsCnt": 3,
      "currentSpectatorsCnt": 0
  },
  
  {
      "isPrivate": true,
      "state": "진행",
      "title": "테스트 방3",
      "totalParticipantCnt": 2,
      "currentParticipantsCnt": 0,
      "currentSpectatorsCnt": 1
  },
  {
      "isPrivate": true,
      "state": "대기",
      "title": "테스트 방4",
      "totalParticipantCnt": 2,
      "currentParticipantsCnt": 2,
      "currentSpectatorsCnt": 1
  },
  {
      "isPrivate": true,
      "state": "진행",
      "title": "테스트 방5",
      "totalParticipantCnt": 3,
      "currentParticipantsCnt": 3,
      "currentSpectatorsCnt": 0
  },
  {
      "isPrivate": false,
      "state": "진행",
      "title": "테스트 방6",
      "totalParticipantCnt": 3,
      "currentParticipantsCnt": 0,
      "currentSpectatorsCnt": 1
  },
  {
      "isPrivate": false,
      "state": "대기",
      "title": "테스트 방7",
      "totalParticipantCnt": 3,
      "currentParticipantsCnt": 3,
      "currentSpectatorsCnt": 0
  },
  {
      "isPrivate": false,
      "state": "대기",
      "title": "테스트 방8",
      "totalParticipantCnt": 4,
      "currentParticipantsCnt": 4,
      "currentSpectatorsCnt": 4
  },
  ])

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
  
  const router = useRouter()
  const userStore = useUserStore()

  // 방 생성 변수
  const roomManagerNickname = ref('')
  const roomId = ref('')
  const title = ref('')
  const totalParticipantCnt = ref('')

  // 웹소켓
  const gameStore = useGameStore()

  // 방 생성
  const createRoom = function(payload){
    console.log('방 생성 요청!!');
    console.log(payload);
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
      console.log(res.data);
      roomManagerNickname.value = res.data.roomManagerNickname
      roomId.value = res.data.roomId
      title.value = res.data.title
      totalParticipantCnt.value = res.data.totalParticipantCnt
      console.log(title.value);
      gameStore.subscribeHandler(title.value)
      return res.data
    })
    // 방 생성 SEND
    .then(data => {
      gameStore.sendCreateRoom(title.value, totalParticipantCnt.value)
    })
    .catch(err => console.log(err))
  }

  // 방 리스트
  const getRoomList = function(payload){
    axios({
      method: 'get',
      url: `${ROOM_API}`,
      params: {
        type: payload.type,
        title: payload.title,
        isTwo: payload.isTwo,
        isThree: payload.isThree,
        isFour: payload.isFour,
        isEmpty: payload.isEmpty,
        page: payload.page,
        size: payload.size,
      }
    })
    .then(res => {
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
    // 공개방 입장 API 응당 & 방 구독 SEND
    .then(res => {
      console.log('방 입장 성공')
      roomId.value = res.data.roomId
      title.value = res.data.title
      totalParticipantCnt.value = res.data.totalParticipantCnt
      gameStore.subscribeHandler(title.value)
    })
    // 방 입장 SEND
    .then(() => {
      gameStore.sendJoinRoom(title.value)
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
    .then(res => {
      console.log('방 입장 성공')
      roomId.value = res.data.roomId
      title.value = res.data.title
      totalParticipantCnt.value = res.data.totalParticipantCnt
      gameStore.subscribeHandler(title.value)
    })
    .then(() => {
      // 방 입장 SEND
      gameStore.sendJoinRoom(title.value)
    })
    .catch(err => console.log(err))
  }

  // 대기방 나가기
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
      console.log('방 나가기 성공')
      return res.data
    })
    .then(() => {
      // 메인 페이지로 이동
      router.push({
        name:'main'
      })
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
    .then(res => {
      console.log('게임 시작 성공')
      return res.data
    })
    .then(() => {
      // 플레이 페이지로 이동
      router.push({
        name:'wait',
        params: { roomId: roomId.value.toString() + 'p' },
        state: {
          title: title.value,
          totalParticipantCnt: totalParticipantCnt.value
        }
      })
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
    .then(res => {
      console.log('강제 퇴장 성공')
      return res.data
    })
    .catch(err => console.log(err))
  } 

  return {
    // 방 목록
    getRoomList,
    allRoomList, pageData, isLast, isfirst, totalPages, totalElements, nowElements, nowPage, isEmpty, pageArray,
    // 방 생성
    createRoom, roomManagerNickname, roomId, title, totalParticipantCnt,
    // 방 입장
    enterRoomPublic, enterRoomPrivate,
    // 방 나가기
    leaveRoom,
    // 게임 시작하기
    startGame,
    // 사용자 강제 퇴장
    forceMemberOut,
  }
},{persist:true})
