import { ref } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from './user'
import { useGameStore } from './game'

export const useFriendStore = defineStore('friend', () => {
  const userStore = useUserStore()
  const gameStore = useGameStore()
  const FRIEND_API = `${userStore.BASE_API_URL}/friends`
  const RANK_API = `${userStore.BASE_API_URL}/ranks`
  
  // 친구 API에서 사용되는 변수
  const searchedPerson = ref({})
  const friendList = ref([])
  const alarmList = ref([])
  const isSent = ref(false)
  const isSearched = ref(false)
  // 랭킹 aPI에서 사용되는 변수
  const isContainedinAll = ref(null)
  const isContainedinFriend = ref(null)
  const allRankList = ref([])
  const friendRankList = ref([])
  const myRank = ref([])
  // 친구 찾기 
  const findFriendinAll = function(nickname){
    console.log(userStore.accessToken);
    axios({
      method: 'get',
      url: `${FRIEND_API}/search`,
      params: {nickname},
      headers: {"access-token": userStore.accessToken}
    })
    .then(res => {
      console.log("res.data.data => ",res.data.data)
      searchedPerson.value = res.data.data
    })
    .catch(err => console.log(err))
  }

  // 친구 목록 조회 -> 닉네임이 공란이면 전체 친구목록, 닉네임이 공란이 아니면 
  const getFriendList = function(nickname){
    axios({
      method: 'get',
      url: `${FRIEND_API}/list/search`,
      params: {nickname},
      headers: {"access-token": userStore.accessToken}
    })
    .then(res => {
      // console.log("res.data.data => ",res.data.data)
      friendList.value = res.data.data
    })
    .catch(err => console.log(err))
  }

  // 친구 신청 알람 리스트
  const RequestAlarm = function(){
    axios({
      method: 'get',
      url: `${FRIEND_API}/request/list`,
      headers: {"access-token": userStore.accessToken}
    })
    .then(res => {
      // console.log(userStore.myNickname,"res => ",res)
      // console.log(userStore.myNickname,"res.data.data => ",res.data.data)
      alarmList.value = res.data.data
    })
    .catch(err => console.log(err))
  }

  // 친구 신청
  const friendRequest = function(nickname){
    axios({
      method: 'post',
      url: `${FRIEND_API}/request`,
      data: {nickname},
      headers: {"access-token": userStore.accessToken}
    })
    .then(res => {
      gameStore.sendFriendRequest(nickname)
      console.log("res.data => ",res.data)
    })
    .catch(err => console.log(err))
  }

  // 친구 신청 수락
  const acceptFriendRequest = function(nickname){
    console.log(nickname)
    axios({
      method: 'post',
      url: `${FRIEND_API}/request/accept`,
      data: {nickname},
      headers: {"access-token": userStore.accessToken}
    })
    .then(res => {
      console.log("res.data => ",res.data)
    })
    .catch(err => console.log(err))
    
  }

  // 친구 신청 거절
  const rejectFriendRequest = function(nickname){
    axios({
      method: 'delete',
      url: `${FRIEND_API}/request/reject`,
      data: {nickname},
      headers: {"access-token": userStore.accessToken}
    })
    .then(res => {
      console.log("res.data => ",res.data)
    })
    .catch(err => console.log(err))
    
  }
  
  // 랭킹 -------------------------------------------------------------------------------------
  
  
  // 전체 랭킹 리스트
  const getAllRankList = function(){

    axios({
      method: 'get',
      url: `${RANK_API}/totals`,
      headers:{"access-token": userStore.accessToken}
    })
    
    .then(res => {
      // console.log(res);
      allRankList.value = res.data.data
      // 내가 전체 랭킹에 존재하는지 판단하는 함수
      isContainedinAll.value = allRankList.value.filter((rank) => rank.nickname === userStore.myNickname).length > 0
      // console.log("res.data.data => ",res.data.data)
      // console.log('isContainedinAll =>', isContainedinAll.value);
    })
    .catch(err => console.log(err))
  }

  // 친구 랭킹 리스트
  const getFriendRankList = function(){
    axios({
      method: 'get',
      url: `${RANK_API}/friends`,
      headers: {"access-token": userStore.accessToken}
    })
    .then(res => {
      friendRankList.value = res.data.data
      // 내가 친구 랭킹에 존재하는지 판단하는 함수
      isContainedinFriend.value = friendRankList.value.filter((rank) => rank.nickname === userStore.myNickname).length > 0
      // console.log("res.data.data => ",res.data.data)
      // console.log('isContainedinFriend', isContainedinFriend.value);
    })
    .catch(err => console.log(err))
  }

  // 내 랭킹 조회
  const getMyRankList = function(){
    axios({
      method: 'get',
      url: `${RANK_API}/myself`,
      headers: {"access-token": userStore.accessToken}
    })
    .then(res => {
      myRank.value = res.data.data
    })
    .catch(err => console.log(err))
  }

  //  티어 가져오기 함수
  const getTierIconUrl = function(tier){
    return new URL(`/src/assets/tier_icons/${tier}.svg`,import.meta.url).href;
  }
  

  return {
    // 친구
    searchedPerson, friendList, alarmList, isContainedinAll, isContainedinFriend,isSent,isSearched,
    findFriendinAll, getFriendList, RequestAlarm, friendRequest, acceptFriendRequest, rejectFriendRequest,

    // 랭킹
    allRankList, friendRankList, myRank,
    getAllRankList, getFriendRankList, getMyRankList
    // 티어 아이콘 가져오기
    ,getTierIconUrl
    }
},{persist:true})
