import { ref } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRouter } from 'vue-router'
 
const KAKAO_JAVASCRIPT_KEY = import.meta.env.VITE_KAKAO_JAVASCRIPT_KEY
const REDIRECT_URI = 'https://i10a804.p.ssafy.io/login'

export const useUserStore = defineStore('user', () => {
  const BASE_API_URL = 'https://i10a804.p.ssafy.io/api'
  const USER_API = `${BASE_API_URL}/auth`
  const MEMBERS_API = `${BASE_API_URL}/members`

  // State
  const router = useRouter()
  const accessToken = ref(null)
  const refreshToken = ref(null)
  const authorizationCode = ref(null)
  const kakaoAccessToken = ref(null)
  const myNickname = ref('')
  const myIcon = ref('1')
  const profileInfo = ref({})

  // 프로필 아이콘 보여주기
  const viewProfileIcon = ref(true)

  // 토큰 재발행 요청
  const renewToken = function () {
    axios({
      method: 'post',
      headers: {
        'access-token': accessToken.value,
        'refresh-token': accessToken.value
      }
    })
    .then(res => {
      console.log(res)
      accessToken.value = res.data.accessToken
      refreshToken.value =res.data.refreshToken
    })
    .catch(err => console.log(err))
  }

  // 회원가입
  const signUp = async function (payload) {
    console.log('회원가입 요청')
    try {
      const response = await axios({
        method: 'post',
        url: `${USER_API}/signup`,
        data: payload
      })
      console.log('회원가입 성공!')
      const res = response.data
      accessToken.value = res.data.accessToken
      refreshToken.value = res.data.refreshToken
      myIcon.value = res.data.icon
      myNickname.value = res.data.nickname
      router.push({ name: 'main' })
      return true
    } catch (err) {
      console.log(err)
      return false
    }
    }
    
  // 일반 로그인
  const generalLogIn = async function (payload) {
    console.log('일반 로그인 요청');
    try {
      const response = await axios({
        method: 'post',
        url: `${USER_API}/login`,
        data: payload
      })
      const res = response.data
      console.log('일반 로그인 성공!!')
      console.log(res.data)
      accessToken.value = res.data.accessToken
      refreshToken.value = res.data.refreshToken
      myIcon.value = res.data.icon
      myNickname.value = res.data.nickname
      router.push({ name: 'main' })
      return true
    } catch (err) {
      console.log(err)
      return false
    }
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
  const simpleLogInRequest = async function (authorizationCode) {
    console.log('간편 로그인 요청');
    // 인가코드로 로그인 요청
    try {
      const response = await axios({
        method: 'post',
        url: `${USER_API}/authorization`,
        data: { authorizationCode }
      })
      const res = response.data
      console.log(res.data)
      if (res.data.code === 200) {
        console.log("카카오 로그인 성공!!")
        accessToken.value = res.data.accessToken
        refreshToken.value = res.data.refreshToken
        myIcon.value = res.data.icon
        myNickname.value = res.data.nickname
        router.push({ name: 'main' })

      } else if (res.data.code === 202) {
        console.log("카카오 로그인 성공, 닉네임 설정!!")
        console.log(res.data)
        kakaoAccessToken.value = res.data.kakaoAccessToken
        router.push({ name: 'kakaosignup' })
      }
      return true
    } catch (err) {
      console.log(err)
      return false
    }
  }

  // 카카오 회원가입
  const kakaoSignUp = async function(payload){
    console.log("카카오 회원가입 요청")
    console.log(payload);
    try {
      const response = await axios({
        method: 'post',
        url: `${MEMBERS_API}/social-signup`,
        data: payload,
        headers: { 'kakao-access-token': kakaoAccessToken.value }
      })
      const res = response.data
      console.log('카카오 회원가입 성공')
      console.log(res.data)
      accessToken.value = res.data.accessToken
      refreshToken.value = res.data.refreshToken
      myIcon.value = res.data.icon
      myNickname.value = res.data.nickname
      router.push({ name: 'main' })
      return true
    } catch (err) {
      console.log(err)
      return false
    }
  }

  // 로그아웃
  const logOut = function () {
    console.log('로그아웃 요청!');
    axios({
      method: 'post',
      url: `${MEMBERS_API}/logout`,
      headers: { 'access-token': accessToken.value }
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
  const kakaoConnect = async function () {
    console.log('카카오 연동 요청!')
    try {
      await axios({
        method: 'post',
        url: `${USER_API}/social`,
        data: { authorizationCode },
        headers: { 'access-token': accessToken.value }
      })
      console.log('카카오 연동 성공!')
      return true
    } catch (err) {
      console.log(err)
      return false
    }
  }

  // 닉네임 중복확인
  const checkNickname = async function (nickname){
    console.log("닉네임 중복 확인 요청")
    try {
      const res = await axios({
        method: 'post',
        url: `${USER_API}/duplication/nickname`,
        data: { nickname }
      })
      console.log('닉네임 중복 확인 성공')
      return res.data.data
    } catch (err) {console.log(err)}  
  }

  // 아이디 중복확인
  const checkMemberId = async function (memberId){
    console.log("아이디 중복 확인 요청")
    try {
      const res = await axios({
        method: 'post',
        url: `${USER_API}/duplication/id`,
        data: { memberId }
      })
      console.log('아이디 중복 확인 성공')
      return res.data.data
    } catch (err) {console.log(err)}
  }
    
  // 회원탈퇴
  const signOut = async function () {
    console.log('회원탈퇴 요청!')
    try {
      await axios({
        method: 'delete',
        url: `${MEMBERS_API}/withdraw`,
        headers: { 'access-token': accessToken.value }
      })
      console.log('회원탈퇴 성공')
      return true
    } catch (err) {
      console.log(err)
      return false
    }
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
    console.log(nickname)
    axios({
      method:'get',
      url: `${MEMBERS_API}/profile/${nickname}`,
      headers: { 'access-token': accessToken.value }

    })
    .then(res => {
      console.log("프로필 정보 요청 결과",res);
      profileInfo.value = res.data.data
      console.log("profileInfo",profileInfo.value)
    })
  }

  return {
    // 베이스 url
    BASE_API_URL,

    // 로그인, 로그아웃, 회원가입, 회원탈퇴, 카카오 연동
    generalLogIn, getKakaoCode, simpleLogInRequest, kakaoSignUp,
    logOut, signUp, signOut, checkMemberId, checkNickname, validateId, validatePassword, validateNickname, kakaoConnect, 
    accessToken, refreshToken, authorizationCode, kakaoAccessToken,

    // 프로필 아이콘, 프로필 정보 받아오기
    getIconUrl, getTierIconUrl, getProfileInfo, 
    myIcon, myNickname, profileInfo, viewProfileIcon,
    }
},{persist:true})