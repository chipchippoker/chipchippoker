import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from './user'
import { faL } from '@fortawesome/free-solid-svg-icons'

export const useMatchStore = defineStore('match', () => {
  const MATCH_API = 'http://i10a804.p.ssafy.io:8082/api/matching'

  const userStore = useUserStore()
  const router = useRouter()
  
  const roomId = ref(null)
  const title = ref(null)
  const totalParticipantsCnt = ref(null)
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
      totalParticipantsCnt.value = res.data.totalParticipantsCnt
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
        totalParticipantsCnt.value = res.data.totalParticipantsCnt
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
    matchCompete, roomId, title, totalParticipantsCnt, isMatch,
    // 친선전 빠른 시작
    matchFriend,
    // 빠른 게임 찾기 중단
    stopFindGame
  }
},{persist:true})
