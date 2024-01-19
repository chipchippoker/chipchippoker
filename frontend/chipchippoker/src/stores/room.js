import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from './user'

export const useRoomStore = defineStore('room', () => {
  const ROOM_API = 'http://localhost/api/rooms'
  const allRoomList = ref([
    {
      "isPrivate": true,
      "state": "대기",
      "title": "테스트 방1",
      "totalParticipantsCnt": 2,
      "currentParticipantsCnt": 2,
      "currentSpectatorsCnt": 0
  },
  {
      "isPrivate": false,
      "state": "대기",
      "title": "테스트 방2",
      "totalParticipantsCnt": 2,
      "currentParticipantsCnt": 3,
      "currentSpectatorsCnt": 0
  },
  
  {
      "isPrivate": true,
      "state": "진행",
      "title": "테스트 방3",
      "totalParticipantsCnt": 2,
      "currentParticipantsCnt": 0,
      "currentSpectatorsCnt": 1
  },
  {
      "isPrivate": true,
      "state": "대기",
      "title": "테스트 방4",
      "totalParticipantsCnt": 2,
      "currentParticipantsCnt": 2,
      "currentSpectatorsCnt": 1
  },
  {
      "isPrivate": true,
      "state": "진행",
      "title": "테스트 방5",
      "totalParticipantsCnt": 3,
      "currentParticipantsCnt": 3,
      "currentSpectatorsCnt": 0
  },
  {
      "isPrivate": false,
      "state": "진행",
      "title": "테스트 방6",
      "totalParticipantsCnt": 3,
      "currentParticipantsCnt": 0,
      "currentSpectatorsCnt": 1
  },
  {
      "isPrivate": false,
      "state": "대기",
      "title": "테스트 방7",
      "totalParticipantsCnt": 3,
      "currentParticipantsCnt": 3,
      "currentSpectatorsCnt": 0
  },
  {
      "isPrivate": false,
      "state": "대기",
      "title": "테스트 방8",
      "totalParticipantsCnt": 4,
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

  // 방 생성
  const makeRoom = function(accessToken, payload){
    console.log('방 생성 요청!!');
    axios({
      method: ROOM_API,
      headers: { 'access-token': accessToken },
      data: payload
    })
    .then(res => {
      console.log('방 생성 성공')
      return res.data
    })
    .then(roomInfo => {
      // 방 정보 전달, 대기 페이지로 이동
      router.push({
        name:'wait',
        params: { roomId: roomInfo.roomId },
        state: {
          title: roomInfo.title,
          totalParticipantsCnt: roomInfo.totalParticipantsCnt,
          nickName: userStore.myNickName
        }
      })
    })
    .catch(err => console.log(err))
  }



  // 방 리스트
  const getRoomList = function(payload){
    axios({
      method: 'get',
      url: `${ROOM_API}/`,
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

  return {getRoomList,allRoomList, pageData, isLast, isfirst, totalPages, totalElements, nowElements, nowPage, isEmpty,pageArray}
},{persist:true})
