import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from './user'
import { faL } from '@fortawesome/free-solid-svg-icons'

export const useFriendStore = defineStore('friend', () => {
  const FRIEND_API = 'http://localhost/api/friends'
  const RANK_API = 'http://localhost/api/ranks'
  const userStore = useUserStore()
  // 친구 API에서 사용되는 변수
  const searchedPerson = ref(
    {
      "nickname": "최현기",
      "icon": "asd",
      "isOnline": true,
      "isFriend": false,
      "isSent": false
  }
  )
  
  const friendList = ref([])
  const alarmList = ref([
    {
      "nickname": "10기_윤예빈"
    }
  ])

  // 랭킹 aPI에서 사용되는 변수
  const isContainedinAll = ref(true)
  const isContainedinFriend = ref(false)
  const allRankList = ref(
    [
      {
          "rank": 1,
          "tier": "diamond",
          "icon": "1",
          "nickname": "림림",
          "point": 4100
      },
      {
          "rank": 2,
          "tier": "diamond",
          "icon": "14",
          "nickname": "딤딤",
          "point": 4000
      },
      {
          "rank": 3,
          "tier": "diamond",
          "icon": "13",
          "nickname": "님님",
          "point": 3900
      },
      {
          "rank": 4,
          "tier": "diamond",
          "icon": "1",
          "nickname": "김김",
          "point": 3800
      },
      {
          "rank": 5,
          "tier": "diamond",
          "icon": "10",
          "nickname": "울산",
          "point": 3700
      },
      {
          "rank": 6,
          "tier": "diamond",
          "icon": "9",
          "nickname": "부산",
          "point": 3600
      },
      {
          "rank": 7,
          "tier": "diamond",
          "icon": "4",
          "nickname": "10기_윤예빈",
          "point": 3500
      },
      {
          "rank": 8,
          "tier": "diamond",
          "icon": "6",
          "nickname": "대구",
          "point": 3400
      },
      {
          "rank": 9,
          "tier": "diamond",
          "icon": "1",
          "nickname": "인천",
          "point": 3300
      },
      {
          "rank": 10,
          "tier": "diamond",
          "icon": "3",
          "nickname": "서울8반",
          "point": 3200
      },
      {
          "rank": 11,
          "tier": "diamond",
          "icon": "2",
          "nickname": "서울5반",
          "point": 3100
      },
      {
          "rank": 12,
          "tier": "diamond",
          "icon": "1",
          "nickname": "서울4반",
          "point": 3000
      },
      {
          "rank": 13,
          "tier": "diamond",
          "icon": "1",
          "nickname": "싸피4",
          "point": 2900
      },
      {
          "rank": 14,
          "tier": "diamond",
          "icon": "14",
          "nickname": "싸피3",
          "point": 2800
      },
      {
          "rank": 15,
          "tier": "diamond",
          "icon": "1",
          "nickname": "싸피2",
          "point": 2700
      },
      {
          "rank": 16,
          "tier": "diamond",
          "icon": "8",
          "nickname": "김희주",
          "point": 2600
      },
      {
          "rank": 17,
          "tier": "diamond",
          "icon": "1",
          "nickname": "김명주",
          "point": 2500
      },
      {
          "rank": 18,
          "tier": "diamond",
          "icon": "6",
          "nickname": "윤희서",
          "point": 2400
      },
      {
          "rank": 19,
          "tier": "diamond",
          "icon": "1",
          "nickname": "임주호",
          "point": 2300
      },
      {
          "rank": 20,
          "tier": "diamond",
          "icon": "4",
          "nickname": "선주리",
          "point": 2200
      },
      {
          "rank": 21,
          "tier": "diamond",
          "icon": "1",
          "nickname": "권희주",
          "point": 2100
      },
      {
          "rank": 22,
          "tier": "diamond",
          "icon": "1",
          "nickname": "윤동희",
          "point": 2000
      },
      {
          "rank": 23,
          "tier": "platinum",
          "icon": "1",
          "nickname": "김지수",
          "point": 1900
      },
      {
          "rank": 24,
          "tier": "platinum",
          "icon": "1",
          "nickname": "김현기",
          "point": 1800
      },
      {
          "rank": 25,
          "tier": "platinum",
          "icon": "1",
          "nickname": "대원이",
          "point": 1700
      },
      {
          "rank": 26,
          "tier": "platinum",
          "icon": "1",
          "nickname": "싸피1",
          "point": 1600
      },
      {
          "rank": 27,
          "tier": "gold",
          "icon": "1",
          "nickname": "김대원",
          "point": 1500
      },
      {
          "rank": 28,
          "tier": "gold",
          "icon": "1",
          "nickname": "윤예빈",
          "point": 1400
      },
      {
          "rank": 29,
          "tier": "gold",
          "icon": "12",
          "nickname": "선수연",
          "point": 1300
      },
      {
          "rank": 30,
          "tier": "gold",
          "icon": "15",
          "nickname": "임세환",
          "point": 1200
      }
  ]
  )
  const friendRankList = ref([
    {
        "rank": 1,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "림림",
        "point": 4100
    },
    {
        "rank": 2,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "딤딤",
        "point": 4000
    },
    {
        "rank": 3,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "님님",
        "point": 3900
    },
    {
        "rank": 4,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "김김",
        "point": 3800
    },
    {
        "rank": 5,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "울산",
        "point": 3700
    },
    {
        "rank": 6,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "부산",
        "point": 3600
    },
    {
        "rank": 7,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "대전",
        "point": 3500
    },
    {
        "rank": 8,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "대구",
        "point": 3400
    },
    {
        "rank": 9,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "인천",
        "point": 3300
    },
    {
        "rank": 10,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "서울8반",
        "point": 3200
    },
    {
        "rank": 11,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "10기_윤예빈",
        "point": 3100
    },
    {
        "rank": 12,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "서울4반",
        "point": 3000
    },
    {
        "rank": 13,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "싸피4",
        "point": 2900
    },
    {
        "rank": 14,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "싸피3",
        "point": 2800
    },
    {
        "rank": 15,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "싸피2",
        "point": 2700
    },
    {
        "rank": 16,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "김희주",
        "point": 2600
    },
    {
        "rank": 17,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "김명주",
        "point": 2500
    },
    {
        "rank": 18,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "윤희서",
        "point": 2400
    },
    {
        "rank": 19,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "임주호",
        "point": 2300
    },
    {
        "rank": 20,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "선주리",
        "point": 2200
    },
    {
        "rank": 21,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "권희주",
        "point": 2100
    },
    {
        "rank": 22,
        "tier": "diamond",
        "icon": "asd",
        "nickname": "윤동희",
        "point": 2000
    },
    {
        "rank": 23,
        "tier": "platinum",
        "icon": "asd",
        "nickname": "김지수",
        "point": 1900
    },
    {
        "rank": 24,
        "tier": "platinum",
        "icon": "asd",
        "nickname": "김현기",
        "point": 1800
    },
    {
        "rank": 25,
        "tier": "platinum",
        "icon": "asd",
        "nickname": "대원이",
        "point": 1700
    },
    {
        "rank": 26,
        "tier": "platinum",
        "icon": "asd",
        "nickname": "싸피1",
        "point": 1600
    },
    {
        "rank": 27,
        "tier": "gold",
        "icon": "asd",
        "nickname": "김대원",
        "point": 1500
    },
    {
        "rank": 28,
        "tier": "gold",
        "icon": "asd",
        "nickname": "윤예빈",
        "point": 1400
    },
    {
        "rank": 29,
        "tier": "gold",
        "icon": "asd",
        "nickname": "선수연",
        "point": 1300
    },
    {
        "rank": 30,
        "tier": "gold",
        "icon": "asd",
        "nickname": "임세환",
        "point": 1200
    }
])
  const myRank = ref(
    {
      "rank": 2,
      "tier": "dia",
      "icon": 12,
      "nickname": "10기_윤예빈",
      "point": 124234
    },
  )

  // 전제 db에서 사람 찾기 
  const findFriendinAll = function(nickname){
    axios({
      method: 'get',
      url: `${FRIEND_API}/search`,
      params: {
        'nickname':nickname
      },
      headers: {
        'access-token': userStore.accessToken
      }
    })
    .then(res => {
      searchedPerson.value = res.data
    })
    .catch(err => console.log(err))
  }

  // 친구 목록 조회 -> 닉네임이 공란이면 전체 친구목록, 닉네임이 공란이 아니면 
  const getFriendList = function(nickname){
    axios({
      method: 'get',
      url: `${FRIEND_API}/list/search`,
      params: {
        'nickname':nickname
      },
      headers: {
        'access-token': userStore.accessToken
      }
    })
    .then(res => {
      friendList.value = res.data
    })
    .catch(err => console.log(err))
  }

  // 친구 신청 알람 리스트
  const RequestAlarm = function(nickname){
    axios({
      method: 'get',
      url: `${FRIEND_API}/request/list`,
      data: {'nickname':nickname},
      headers: {
        'access-token': userStore.accessToken
      }
    })
    .then(res => {
      alarmList.value = res.data
    })
    .catch(err => console.log(err))
  }

  // 친구 신청
  const friendRequest = function(nickname){
    axios({
      method: 'post',
      url: `${FRIEND_API}/request`,
      data: {'nickname':nickname},
      headers: {
        'access-token': userStore.accessToken
      }

    })
    .then(res => {
    })
    .catch(err => console.log(err))
    
  }

  // 친구 신청 수락
  const acceptFriendRequest = function(nickname){
    axios({
      method: 'post',
      url: `${FRIEND_API}/request/accept`,
      data: {'nickname':nickname},
      headers: {
        'access-token': userStore.accessToken
      }
    })
    .then(res => {
    })
    .catch(err => console.log(err))
    
  }

  // 친구 신청 거절
  const rejectFriendRequest = function(nickname){
    axios({
      method: 'post',
      url: `${FRIEND_API}/request/reject`,
      data: {'nickname':nickname},
      headers: {
        'access-token': userStore.accessToken
      }
    })
    .then(res => {
    })
    .catch(err => console.log(err))
    
  }
  
  // 랭킹 -------------------------------------------------------------------------------------
  
  
  // 전체 랭킹 리스트
  const getAllRankList = function(){
    axios({
      method: 'get',
      url: `${RANK_API}/totals`,
      headers: {
        'access-token': userStore.accessToken
      }
    })
    .then(res => {
      allRankList.value = res.data
      // 내가 전체 랭킹에 존재하는지 판단하는 함수
      isContainedinAll.value = allRankList.value.filter((rank) => rank.nickname === userStore.myNickname).length > 0
    })
    .catch(err => console.log(err))
  }

  // 친구 랭킹 리스트
  const getFriendRankList = function(){
    axios({
      method: 'get',
      url: `${RANK_API}/friends`,
      headers: {
        'access-token': userStore.accessToken
      }
    })
    .then(res => {
      friendRankList.value = res.data
      // 내가 친구 랭킹에 존재하는지 판단하는 함수
      isContainedinFriend.value = friendRankList.value.filter((rank) => rank.nickname === userStore.myNickname).length > 0
    })
    .catch(err => console.log(err))
  }

  // 내 랭킹 조회
  const getMyRankList = function(){
    axios({
      method: 'get',
      url: `${RANK_API}/myself`,
      headers: {
        'access-token': userStore.accessToken
      }
    })
    .then(res => {
      myRank.value = res.data
    })
    .catch(err => console.log(err))
  }

  //  티어 가져오기 함수
  const getTierIconUrl = function(tier){
    return new URL(`/src/assets/tier_icons/${tier}.png`,import.meta.url).href;
  }
  
  return {
    // 친구
    searchedPerson, friendList, alarmList, isContainedinAll, isContainedinFriend,
    findFriendinAll, getFriendList, RequestAlarm, friendRequest, acceptFriendRequest, rejectFriendRequest,
    // 랭킹
    allRankList, friendRankList, myRank,
    getAllRankList, getFriendRankList, getMyRankList
    // 티어 아이콘 가져오기
    ,getTierIconUrl
    }
},{persist:true})
