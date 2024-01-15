import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'

export const useUserStore = defineStore('user', () => {
   
  const route = useRoute()
  const router = useRouter()
  const isNickDuplicated = ref(null)
  const isIdDuplicated = ref(null)
  const userIcon = ref(null)
 
  const accessToken = ref(null)
  const refreshToken = ref(null)
  const kakaoAccessToken = ref(null)
  const USER_API = 'http://localhost/api/members'
  const KAKAO_API_KEY = import.meta.env.VITE_KAKAO_API_KEY
  const REDIRECT_URI = 'http://localhost:5173/login'
  const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`

  // 일반 로그인
  const generalLogIn = function (payload) {
    axios({
      method: 'post',
      url: `${USER_API}/login/general/`,
      data: payload
    })
      .then(res => {
        console.log('일반 로그인 성공!!')
        accessToken.value = res.generalLoginResponse.access-token
        refreshToken.value = res.generalLoginResponse.refresh-token
        userIcon.value = res.generalLoginResponse.icon
      })
      .catch(err => console.log(err))
  }

  
  // 간편 로그인
  const simpleLogIn = function () {
    console.log('카카오 인가코드 받기')
    Kakao.Auth.login({
      redirectUri: REDIRECT_URI,
    })
  }

  const simpleLogInRequest = function (authorizationCode) {
    console.log(authorizationCode);
    // 인가코드로 로그인 요청
    axios({
      method: 'post',
      url: `${USER_API}/login/simple/`,
      data: {
        'authorizationCode': authorizationCode
      }
    })
    .then(res => {
      if (res.code === 200) {
        console.log("카카오 로그인 성공!!")
        accessToken.value = res.simpleLoginResponse.access-token
        refreshToken.value = res.simpleLoginResponse.refresh-token
        userIcon.value = res.simpleLoginResponse.icon
      } else if (res.code === 201) {
        console.log("카카오 로그인 성공2!!")
        kakaoAccessToken.value = res.kakao-access-token
        axios({
          method: 'post',
          url: `${USER_API}/login/simple/nickname/`,
          headers: {
            "kakao-access-token": kakaoAccessToken.value
          }
        })
        .then(res => {
          accessToken.value = res.simpleLoginResponse.access-token
          refreshToken.value = res.simpleLoginResponse.refresh-token
          userIcon.value = res.simpleLoginResponse.icon
        })
      }
    })
    .catch(err => {
      console.log('카카오 로그인 실패');
  })
  }

  
  // 카카오 회원가입
  const  kakaoSignUp = function(nickName){
    const payload = {
      'nickname':nickName
    }
    axios({
      method:'post',
      url:'${USER_API}/login/simple/nickname',
      data:'payload'
    })
    .then((res)=>{
      accessToken.value = res.data.access-token
      refreshToken.value = res.data.refresh-token
    })
    .catch((err)=>{
      console.log(err)
    })

  }



  // 닉네임 중복확인
  const checkNickName = function (nickName){
    const payload = {
      'nickname':nickName
    }
    axios({
      method:'post',
      url: `${USER_API}/duplication/nickname`,
      data:payload
    })
    .then((res)=>{
      isNickDuplicated.value = res.data.isDuplicated
    })
    .catch((err)=>{
      console.log(err)
    })
  }

  // 아이디 중복확인
  const checkMemberId = function (memberId){
    const payload = {
      'memberId':memberId
    }
    axios({
      method:'post',
      url: `${USER_API}/duplication/id`,
      data:payload
    })
    .then((res)=>{
      isIdDuplicated.value = res.data.isDuplicated
    })
    .catch((err)=>{
      console.log(err)
    })
  }



  // 아이디 유효성 검사
  const validateMemberId = function (memberId) {
    if (memberId === null) {
      return true
    }

    // 아이디가 공백이면 유효하지 않음
    if (memberId === '') {
      return false
    }

    // 아이디가 영어, 숫자만으로 이루어졌는지 확인
    const regExp = /^[a-zA-Z0-9]+$/
    if (!regExp.test(memberId)) {
      return false
    }

    // 아이디 길이가 6~12자 사이인지 확인
    const length = memberId.length
    if (length < 6 || length > 12) {
      return false
    }
    
    // 아이디가 유효함
    console.log('아이디 유효성 검사 통과')
    return true
  }


  // 회원가입
  const signUp = function (payload) {
    const { memberId, password1, password2, nickName } = payload
    axios({
      method: 'post',
      url: `${USER_API}/signup/`,
      data: {
        memberId,
        password1,
        password2,
        nickName,
      }
    })
      .then((res) => {
        console.log('회원가입 성공!')
        router.push({ name: 'logIn' })
      })
      .catch(err => console.log(err))
  }



  // 아이디 유효성 검사
  const validateId = function (memberId) {
  if (memberId === null) {
    return true
  }
  if (memberId === '') {
    console.log('아이디 공백')
    return false
  }
  const regExp = /^[a-zA-Z0-9]+$/
  if (!regExp.test(memberId)) {
    console.log('아이디 영숫')
    return false
  }
  const length = memberId.length
  if (length < 6 || length > 12) {
    console.log('아이디 길이')
    return false
  }
  console.log('아이디 유효성 검사 통과')
  return true
  }


  // 비밀번호 유효성 검사
  const validatePassword = function (password) {
  if (password === null) {
    return true
  }
  if (password === '') {
    console.log('비번 공백')
    return false
  }
  const containsEnglish = /[a-zA-Z]/.test(password);
  const containsNumber = /\d/.test(password);
  const containsSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/.test(password);

  if (!(containsEnglish && containsNumber && containsSpecialChar)) {
    console.log('비밀번호는 영어, 숫자, 특수문자를 모두 포함해야 합니다.');
    return false;
  }
  const length = password.length
  if (length < 8 || length > 30) {
    console.log('비번 길이')
    return false
  }
  console.log('비밀번호 유효성 검사 통과')
  return true
  }


  // 닉네임 유효성 검사
  const validateNickName = function (nickName) {
    if (nickName === null) {
      return true
    }

    if (nickName.value === '') {
      console.log('닉넴 공백')
      return false
    }
    const regExp = /^[가-힣a-zA-Z0-9_]+$/
    if (!regExp.test(nickName)) {
      console.log('닉넴 한영숫특')
      return false
    }
    const length = nickName.length
    if (length < 4 || length > 16) {
      console.log('닉넴 길이')
      return false
    }
    console.log('닉네임 유효성 검사 통과')
    return true 
  }






  //  아이콘 변수, 아이콘 가져오기 함수
  const myIcon = ref('1')
  const getIconUrl = function(number){
    return new URL(`/src/assets/profile_icons/icon${number}.jpg`,import.meta.url).href;
  }


  return {checkNickName,accessToken, refreshToken, kakaoAccessToken, kakaoSignUp, generalLogIn, simpleLogIn, signUp, checkMemberId,
    isNickDuplicated, isIdDuplicated, validateId, validatePassword, validateNickName,generalLogIn, simpleLogIn, simpleLogInRequest, validateMemberId,getIconUrl, myIcon}
},{persist:true})
