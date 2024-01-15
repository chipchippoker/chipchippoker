import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

export const useUserStore = defineStore('counter', () => {
  
  const USER_API = 'http://localhost/api/members'
  
  const accessToken = ref(null)
  const refreshToken = ref(null)
  const kakaoAccessToken = ref(null)
  const userIcon = ref(null)
  

  // 카카오 회원가입 함수
  const  kakaoSignUp = function(nickName){
    const payload = {
      'nickname':nickName
    }
    axios({
      method:'post',
      url:`${USER_API}/login/simple/nickname`,
      data:payload
    })
    .then((res)=>{
      accessToken.value = res.data.access-token
      refreshToken.value = res.data.refresh-token
      userIcon.value = res.data.icon
    })
    .catch((err)=>{
      console.log(err)
    })
    
  }

  return {accessToken, refreshToken, kakaoAccessToken,userIcon, kakaoSignUp}
},{persist:true})
