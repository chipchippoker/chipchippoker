import { ref } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from './user'

export const useMatchStore = defineStore('match', () => {
  const userStore = useUserStore()
  const MATCH_API = `${userStore.BASE_API_URL}/matching`
  const router = useRouter()
  
  const roomId = ref(null)
  const title = ref(null)
  const totalParticipantCnt = ref(null)
  const isMatch = ref(null)

  // 경쟁전 빠른 시작
  const matchCompete = function(payload) {
    axios({
      method: 'post',
      url: `${MATCH_API}/competition`,
      headers: { 'access-token': userStore.accessToken },
      data: payload
    })
    .then(res => {
      console.log('경쟁전 매치 성공')
      roomId.value = res.data.roomId
      title.value = res.data.title
      totalParticipantCnt.value = res.data.totalParticipantCnt
      isMatch.value = true
      return res.data
    })
    .catch(err => console.log(err))
  } 


  // 친선전 빠른 시작
  const matchFriend = function(payload) {
    axios({
      method: 'post',
      url: `${MATCH_API}/friendly`,
      headers: { 'access-token': userStore.accessToken },
      data: payload
    })
    .then(res => {
      if (Object.keys(res.data).length === 0) {
        console.log('생성된 친선방 X')
        isMatch.value = false
      } else {
        console.log('친선전 매치 성공')
        roomId.value = res.data.roomId
        title.value = res.data.title
        totalParticipantCnt.value = res.data.totalParticipantCnt
        isMatch.value = true
      }
      return res.data
    })
    .catch(err => console.log(err))
  }  


  // 빠른 게임 찾기 중단
  const stopFindGame = function () {
    axios({
      method: 'post',
      url: `${MATCH_API}/quit`,
      headers: { 'access-token': userStore.accessToken }
    })
    .then(res => {
      console.log('게임 찾기 중단')
      return res.data
    })
    .catch(err => console.log(err))   
  }

  return {
    // 경쟁전 빠른 시작
    matchCompete, roomId, title, totalParticipantCnt, isMatch,
    // 친선전 빠른 시작
    matchFriend,
    // 빠른 게임 찾기 중단
    stopFindGame
  }
},{persist:true})
