import { ref } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from './user'
import { useGameStore } from './game'
import { useRoomStore } from './room'
import { MagicString } from 'vue/compiler-sfc'


export const useMatchStore = defineStore('match', () => {
  const userStore = useUserStore()
  const gameStore = useGameStore()
  const roomStore = useRoomStore()
  const MATCH_API = `${userStore.BASE_API_URL}/matching`
  const router = useRouter()
  
  const roomId = ref(null)
  const title = ref(null)
  const totalParticipantCnt = ref(null)
  const isMatch = ref(false)

  // 경쟁전 빠른 시작
  const matchCompete = function(payload) {
    axios({
      method: 'post',
      url: `${MATCH_API}/competition`,
      headers: { 'access-token': userStore.accessToken },
      data: payload
    })
    .then(response => {
      console.log('경쟁전 매치 성공')
      const res = response.data
      console.log(res);
      roomId.value = res.data.roomId
      roomStore.roomId = res.data.roomId
      title.value = res.data.title
      totalParticipantCnt.value = res.data.totalParticipantCnt
      isMatch.value = true
      // 첫 입장(방 생성)
      console.log(res.data.isFirstParticipant);
      gameStore.subscribeHandler(title.value)
      if (res.data.isFirstParticipant) {
        return true
      } else {
        return false
      }
    })
    .then((isFirstParticipant)=>{
      console.log(isFirstParticipant);
      gameStore.sendMatching(title.value, totalParticipantCnt.value, isFirstParticipant)
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
        roomStore.roomId = res.data.roomId
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
