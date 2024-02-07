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
  const totalParticipantCnt = ref(0)

  const isSearching = ref(false)    // 경쟁전 매칭 중
  const isMatch = ref(false)        // 
  const isNotExistRoom = ref(false)  // 친선전 남는 방이 없음

  // 경쟁전 빠른 시작
  const matchCompete = function(payload) {
    console.log(payload);
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
      isSearching.value = true
      roomId.value = res.data.roomId
      roomStore.roomId = res.data.roomId
      title.value = res.data.title
      totalParticipantCnt.value = res.data.totalParticipantCnt
      gameStore.subscribeHandler(title.value)
    })
    .then(()=>{
      gameStore.sendMatching(title.value, totalParticipantCnt.value)
    })
    .catch(err => console.log(err))
  } 


  // 친선전 빠른 시작
  const matchFriend = async function(payload) {
    try {
      const response = await axios({
        method: 'post',
        url: `${MATCH_API}/friendly`,
        headers: { 'access-token': userStore.accessToken },
        data: payload
      })
      const res = response.data
      if (Object.keys(res.data).length === 0) {
        console.log('생성된 친선방 X')
        isNotExistRoom.value = true
        return false
      } else {
        console.log('친선전 매치 성공')
        roomId.value = res.data.roomId
        title.value = res.data.title
        roomStore.roomId = res.data.roomId
        totalParticipantCnt.value = res.data.totalParticipantCnt
        isNotExistRoom.value = false
        return true
      }
    } catch (err) {
      return console.log(err)
    }
  }  


  // 빠른 게임 찾기 중단
  const stopFindGame = function () {
    axios({
      method: 'post',
      url: `${MATCH_API}/quit`,
      headers: { 'access-token': userStore.accessToken }
    })
    .then(response => {
      console.log('게임 찾기 중단')
      const res = response.data
      console.log(res);
      gameStore.subscriptionGame.unsubscribe(gameStore.myGameSubId)
    })
    .catch(err => console.log(err))
  }

  return {
    // 경쟁전 빠른 시작
    matchCompete, roomId, title, totalParticipantCnt, isMatch, isSearching,
    // 친선전 빠른 시작
    matchFriend, isNotExistRoom,
    // 빠른 게임 찾기 중단
    stopFindGame
  }
},{persist:true})
