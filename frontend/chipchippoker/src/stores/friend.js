import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'

export const useFriendStore = defineStore('friend', () => {
  const FRIEND_API = 'http://localhost/api/friends'
  const searchedFriend = ref(null)
  let id = 1
  const friendList = ref(
    [{'name':'윤예빈',
    'number':id++,
    'point':12387,
    'isOnline':true
    },
    {'name':'ㄴㅁ이로',
    'number':id++,
    'point':2376,
    'isOnline':false
    },
    {'name':'ㅁㄶ,ㅓㅠ',
    'number':id++,
    'point':1234,
    'isOnline':true
    },
    {'name':'ㅈㄷㅁ룧ㄹㄷ',
    'number':id++,
    'point':1212,
    'isOnline':false
    },
    {'name':'ㅁㄷ러ㅗㅠ',
    'number':id++,
    'point':123,
    'isOnline':true
    },
    {'name':'ㅂㅈ도',
    'number':id++,
    'point':123,
    'isOnline':false
    },
    {'name':'ㅁㄴ류ㅓㅏㄹㄴㅁ',
    'number':id++,
    'point':12,
    'isOnline':true
    },]
  )


  // 친구 찾기 
  const findFriend = function(nickname){
    axios({
      method: 'get',
      url: `${FRIEND_API}/search/${nickname}`,
    })
    .then(res => {
      searchedFriend.value = res.data
    })
    .catch(err => console.log(err))
  }


  // 친구 목록 조회
  const getfriendList = function(nickname){
    axios({
      method: 'get',
      url: `${FRIEND_API}/list/search/nickname=${nickname}`,
    })
    .then(res => {
      friendList.value = res.data
    })
    .catch(err => console.log(err))
    
  }


  return {findFriend, searchedFriend, friendList, getfriendList}
},{persist:true})
