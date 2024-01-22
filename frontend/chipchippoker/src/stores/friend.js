import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'

export const useFriendStore = defineStore('friend', () => {
  const FRIEND_API = 'http://localhost/api/friends'
  const RANK_API = 'http://localhost/api/ranks'
  
  // 친구 API에서 사용되는 변수
  const isContained = ref(true)
  const searchedFriend = ref(null)
  const friendList = ref(
    [
      {
        "icon": 1,
        "tier": "rare",
        "isKakaoFriend": true,
        "nickname": "윤예빈",
        "isOnline": true
      },
      {
        "icon": 2,
        "tier": "silver",
        "isKakaoFriend": false,
        "nickname": "인디언포커를하고내인생이",
        "isOnline": false
      },
      {
        "icon": 3,
        "tier": "silver",
        "isKakaoFriend": true,
        "nickname": "10김ㄴ거ㅏㅠㅈㅁㄱ",
        "isOnline": false
      },

    ]
  )
  const alarmList = ref([
    {
      nickname:'윤예빈',
      status: "대기" 
    },
    {
      nickname:'김대원',
      status: "대기" 
    },
    {
      nickname:'crazyhero',
      status: "대기" 
    },
    {
      nickname:'ㅡㅛㅜ믇',
      status: "대기" 
    },
    {
      nickname:'인디언포커를하고내인생이즐거워졌',
      status: "대기" 
    },
    {
      nickname:'제발요',
      status: "대기" 
    },
    {
      nickname:'친구하자',
      status: "대기" 
    },
    {
      nickname:'헤헤...',
      status: "대기" 
    }
  ])
  // 랭킹 aPI에서 사용되는 변수
  const allRankList = ref([
    {
      "rank": 1,
      "tier": "dia",
      "icon": 19,
      "nickname": "10기_임세환",
      "point": 425637
    },
    {
      "rank": 2,
      "tier": "dia",
      "icon": 12,
      "nickname": "10기_윤예빈",
      "point": 124234
    },
    {
      "rank": 3,
      "tier": "gold",
      "icon": 12,
      "nickname": "10기_선수연",
      "point": 12435
    },
    {
      "rank": 4,
      "tier": "gold",
      "icon": 16,
      "nickname": "10기_권순준",
      "point": 12355
    },
    {
      "rank": 5,
      "tier": "gold",
      "icon": 10,
      "nickname": "10기_김대원",
      "point": 12314
    },
    {
      "rank": 6,
      "tier": "gold",
      "icon": 20,
      "nickname": "10기_최현기",
      "point": 12300
    },
    {
      "rank": 7,
      "tier": "silver",
      "icon": 5,
      "nickname": "10기_사싸피",
      "point": 10000
    },
    {
      "rank": 8,
      "tier": "silver",
      "icon": 1,
      "nickname": "10기_오싸피",
      "point": 9292
    },
    {
      "rank": 9,
      "tier": "silver",
      "icon": 2,
      "nickname": "10기_육싸피",
      "point": 9000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    },
    {
      "rank": 10,
      "tier": "silver",
      "icon": 3,
      "nickname": "10기_칠싸피",
      "point": 8000
    }

  ])
  const friendRankList = ref([
    {
      "rank": 1,
      "tier": "dia",
      "icon": 19,
      "nickname": "10기_임세환",
      "point": 425637
    },
    {
      "rank": 2,
      "tier": "dia",
      "icon": 12,
      "nickname": "10기_윤예빈",
      "point": 124234
    },
    {
      "rank": 3,
      "tier": "gold",
      "icon": 12,
      "nickname": "10기_선수연",
      "point": 12435
    },
    {
      "rank": 4,
      "tier": "gold",
      "icon": 16,
      "nickname": "10기_권순준",
      "point": 12355
    },
    {
      "rank": 5,
      "tier": "gold",
      "icon": 10,
      "nickname": "10기_김대원",
      "point": 12314
    },
    {
      "rank": 6,
      "tier": "gold",
      "icon": 20,
      "nickname": "10기_최현기",
      "point": 12300
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

  // 친구 찾기 
  const findFriend = function(nickname){
    axios({
      method: 'get',
      url: `${FRIEND_API}/search/${nickname}`,
    })
    .then(res => {
      searchedFriend.value = res.friendSearchResponse
    })
    .catch(err => console.log(err))
  }

  // 친구 목록 조회
  const getFriendList = function(nickname){
    axios({
      method: 'get',
      url: `${FRIEND_API}/list/search/nickname=${nickname}`,
    })
    .then(res => {
      friendList.value = res.friendListResponse.friendList
    })
    .catch(err => console.log(err))
  }

  // 친구 신청 알람 리스트
  const RequestAlarm = function(nickname){
    axios({
      method: 'get',
      url: `${FRIEND_API}/request/list`,
      data: {'nickname':nickname}
    })
    .then(res => {
    })
    .catch(err => console.log(err))
  }

  // 친구 신청
  const friendRequest = function(nickname){
    axios({
      method: 'post',
      url: `${FRIEND_API}/request`,
      data: {'nickname':nickname}
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
      data: {'nickname':nickname}
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
      data: {'nickname':nickname}
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
    })
    .then(res => {
      allRankList.value = res.totalRankResponse.totalRankList
      // 내가 전체 랭킹에 존재하는지 판단하는 함수
      isContained.value = allRankList.value.filter((rank) => rank.nickname === myNickName).length > 0
    })
    .catch(err => console.log(err))
  }

  // 친구 랭킹 리스트
  const getFriendRankList = function(){
    axios({
      method: 'get',
      url: `${RANK_API}/friends`,
    })
    .then(res => {
      friendRankList.value = res.totalRankResponse.friendRankList
    })
    .catch(err => console.log(err))
  }

  // 내 랭킹 조회
  const getMyRankList = function(){
    axios({
      method: 'get',
      url: `${RANK_API}/myself`,
    })
    .then(res => {
      myRank.value = res.myselfResponse
    })
    .catch(err => console.log(err))
  }

  //  티어 가져오기 함수
  const getTierIconUrl = function(tier){
    return new URL(`/src/assets/tier_icons/${tier}.png`,import.meta.url).href;
  }
  
  return {findFriend, searchedFriend, friendList, getFriendList, allRankList, getAllRankList, friendRankList,getFriendRankList, getMyRankList, myRank,friendRequest, acceptFriendRequest, rejectFriendRequest, RequestAlarm, alarmList, getTierIconUrl, isContained}
},{persist:true})
