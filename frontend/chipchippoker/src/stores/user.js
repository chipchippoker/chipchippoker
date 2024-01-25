import { ref, watch } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRouter } from 'vue-router'
 
const USER_API = 'https://i10a804.p.ssafy.io/api/auth'
const MEMBERS_API = 'https://i10a804.p.ssafy.io/api/members'
const KAKAO_JAVASCRIPT_KEY = import.meta.env.VITE_KAKAO_JAVASCRIPT_KEY
const REDIRECT_URI = 'http://localhost:5173/login'

export const useUserStore = defineStore('user', () => {
  // State
  const router = useRouter()
  const accessToken = ref(null)
  const refreshToken = ref(null)
  const authorizationCode = ref(null)
  const kakaoAccessToken = ref(null)
  const myNickname = ref('')
  const myIcon = ref('1')
  const profileInfo = ref({})
  let headers = {'access-token': accessToken.value}
  let authHeaders = {'access-token': accessToken.value}
  let refreshHeaders = {'refresh-token': refreshToken.value}
  let kakaoHeaders = {'kakao-access-token': kakaoAccessToken.value}

  watch(accessToken, (newAccessToken) => {
    headers['access-token'] = newAccessToken;
    authHeaders['access-token'] = newAccessToken;
  });
  
  watch(refreshToken, (newRefreshToken) => {
    refreshHeaders['refresh-token'] = newRefreshToken;
  });
  
  watch(kakaoAccessToken, (newKakaoAccessToken) => {
    kakaoHeaders['kakao-access-token'] = newKakaoAccessToken;
  });

  // 회원가입
  const signUp = function (payload) {
    console.log('회원가입 요청');
    axios({
      method: 'post',
      url: `${USER_API}/signup`,
      data: payload
    })
    .then((response) => {
        console.log('회원가입 성공!')
        const res = response.data
        if (res.code === 200) {
          accessToken.value = res.data.accessToken
          refreshToken.value = res.data.refreshToken
          myIcon.value = res.data.icon
          myNickname.value = res.data.nickname
          router.push({ name: 'main' })
        }
      })
      .catch(err => console.log(err))
    }
    
  // 일반 로그인
  const generalLogIn = function (payload) {
    console.log('일반 로그인 요청');
    axios({
      method: 'post',
      url: `${USER_API}/login`,
      data: payload
    })
      .then(response => {
        const res = response.data
        console.log('일반 로그인 성공!!')
        console.log(res.data);
        accessToken.value = res.data.accessToken
        refreshToken.value = res.data.refreshToken
        myIcon.value = res.data.icon
        myNickname.value = res.data.nickname
        router.push({ name: 'main' })
      })
      .catch(err => console.log(err))
  }

  // 카카오 인가코드 받기
  const getKakaoCode = function () {
    console.log('카카오 인가코드 받기')
    Kakao.init(KAKAO_JAVASCRIPT_KEY)
    Kakao.Auth.authorize({
      redirectUri: REDIRECT_URI,
    })
  }

  // 간편 로그인
  const simpleLogInRequest = function (authorizationCode) {
    console.log('간편 로그인 요청');
    // 인가코드로 로그인 요청
    axios({
      method: 'post',
      url: `${USER_API}/authorization`,
      data: {authorizationCode}
    })
    .then(response => {
      const res = response.data
      console.log(res.data);
      if (res.data.code === 200) {
        console.log("카카오 로그인 성공!!")
        accessToken.value = res.data.accessToken
        refreshToken.value = res.data.refreshToken
        myIcon.value = res.data.icon
        myNickname.value = res.data.nickname
        router.push({ name: 'main' })

      } else if (res.data.code === 202) {
        console.log("카카오 로그인 성공, 닉네임 설정!!")
        console.log(res.data);
        kakaoAccessToken.value = res.data.kakaoAccessToken
        router.push({ name: 'kakaosignup' })
      }
    })
    .catch(err => console.log(err))
  }

  // 카카오 회원가입
  const kakaoSignUp = function(payload){
    console.log("카카오 회원가입 요청")
    console.log(kakaoAccessToken.value)
    axios({
      method: 'post',
      url: `${USER_API}/social-signup`,
      data: payload,
      headers: kakaoHeaders
    })
    .then((response)=>{
      const res = response.data
      console.log('카카오 회원가입 성공');
      console.log(res.data);
      accessToken.value = res.data.accessToken
      refreshToken.value = res.data.refreshToken
      myIcon.value = res.data.icon
      myNickname.value = res.data.nickname
      router.push({ name: 'main' })
    })
    .catch(err => console.log(err))
  }

  // 로그아웃
  const logOut = function () {
    console.log('로그아웃 요청!');
    axios({
      method: 'post',
      url: `${USER_API}/logout`,
      headers: authHeaders
    })  
    .then(res => {
      console.log('로그아웃 성공!!')
      accessToken.value = ''
      refreshToken.value = ''
      myIcon.value = ''
      myNickname.value = ''
      router.push({ name: 'login'})
    })  
    .catch(err => console.log(err))
  }  

  // 카카오 연동
  const kakaoConnect = function () {
    console.log('카카오 연동 요청!');
    axios({
      method: 'post',
      url: `${USER_API}/social`,
      data: { authorizationCode },
      headers: authHeaders
    })
    .then(res => {
      console.log('카카오 연동 성공!');
    })
    .catch(err => console.log(err))
  }

  // 닉네임 중복확인
  const checkNickname = function (nickname){
    console.log("닉네임 중복 확인 요청")
    axios({
      method: 'post',
      url: `${USER_API}/duplication/nickname`,
      data: { nickname }
    })
    .then((res)=>{
      console.log('닉네임 중복 확인 성공');
      return res.data.data
    })
    .catch(err => console.log(err))
  }

  // 아이디 중복확인
  const checkMemberId = function (memberId){
    console.log("아이디 중복 확인 요청")
    axios({
      method: 'post',
      url: `${USER_API}/duplication/id`,
      data: { memberId }
    })
    .then((res)=>{
      console.log('아이디 중복 확인 성공')
      return res.data.data
    })
    .catch(err => console.log(err))
  }

  // 회원탈퇴
  const signOut = function () {
    console.log('회원탈퇴 요청!')
    axios({
      method: 'delete',
      url: `${USER_API}/members`,
      headers: authHeaders
    })
    .then(res => {
      console.log('회원탈퇴 성공')
    })
    .catch(err => console.log(err))
  }
  
  // 아이디 유효성 검사
  const validateId = function (memberId) {
  if (memberId === null) {
    return true
  }
  if (memberId === '') {
    
    return false
  }
  const regExp = /^[a-zA-Z0-9]+$/
  if (!regExp.test(memberId)) {
    
    return false
  }
  const length = memberId.length
  if (length < 6 || length > 12) {

    return false
  }
  return true
  }

  // 비밀번호 유효성 검사
  const validatePassword = function (password) {
  if (password === null) {
    return true
  }
  if (password === '') {

    return false
  }
  const containsEnglish = /[a-zA-Z]/.test(password);
  const containsNumber = /\d/.test(password);
  const containsSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/.test(password);

  if (!(containsEnglish && containsNumber && containsSpecialChar)) {

    return false;
  }
  const length = password.length
  if (length < 8 || length > 30) {

    return false
  }
  return true
  }

  // 닉네임 유효성 검사
  const validateNickname = function (nickname) {
    if (nickname === null) {
      return true
    }

    if (nickname.value === '') {
  
      return false
    }
    const regExp = /^[가-힣a-zA-Z0-9_]+$/
    if (!regExp.test(nickname)) {
  
      return false
    }
    const length = nickname.length
    if (length < 4 || length > 16) {
  
      return false
    }

    return true 
  }

  //  아이콘 변수, 아이콘 가져오기 함수  
  const getIconUrl = function(number){
    return new URL(`/src/assets/profile_icons/icon${number}.jpg`,import.meta.url).href;
  }

  //  아이콘 변수, 아이콘 가져오기 함수  
  const getTierIconUrl = function(number){
    return new URL(`/src/assets/tier_icons/icon${number}.jpg`,import.meta.url).href;
  }
  
  // 프로필 정보 요청
  const getProfileInfo = function(nickname) {
    axios({
      method:'get',
      url: `${MEMBERS_API}/profile/${nickname}`,
      headers: {
        "access-token": accessToken.value
      }
    })
    .then(res => {
      console.log("프로필 정보 요청 결과",res);
      profileInfo.value = res.data.data
      console.log("profileInfo",profileInfo.value)
    })
  }


  return {
    // 로그인, 로그아웃, 회원가입, 회원탈퇴, 카카오 연동
    generalLogIn, getKakaoCode, simpleLogInRequest, kakaoSignUp,
    logOut, signUp, signOut, checkMemberId, checkNickname, validateId, validatePassword, validateNickname, kakaoConnect, 
    accessToken, refreshToken, authorizationCode, kakaoAccessToken,

    // 프로필 아이콘, 프로필 정보 받아오기
    getIconUrl, getTierIconUrl, getProfileInfo, 
    myIcon, myNickname, profileInfo
    }
},{persist:true})