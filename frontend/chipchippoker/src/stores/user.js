import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRouter } from 'vue-router'

export const useUserStore = defineStore('user', () => {
  const router = useRouter()
  const accessToken = ref(null)
  const refreshToken = ref(null)
  const userIcon = ref(null)
  const authorizationCode = ref(null)
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

    axios.get(KAKAO_AUTH_URL)
    .then(res => {
      router.push(res.data.auth_code_url)
      // 응답 코드가 302이면
      // if (res.status === 302) {
      //   // 리다이렉트된 URL에서 인가코드 추출
      //   const redirectUrl = res.headers.location;
      //   console.log(redirectUrl);
      //   authorizationCode.value = redirectUrl.split('?')[1].split('&')[0].split('=')[1];
      // }
    })
    .then(res => {
      authorizationCode.value = new URL(window.location.href).searchParams.get('code')
    })
    .then(res => {
      console.log(authorizationCode.value);

    })


    // 인가코드로 로그인 요청
    axios({
      method: 'post',
      url: `${USER_API}/login/simple/`,
      data: {
        'authorizationCode': authorizationCode.value
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

  // 비밀번호 유효성 검사
  const validatePassword = function (password) {
    if (password === null) {
      return true
    }
    
    if (password === '') {
      return false
    }


    // 비밀번호가 영어, 숫자, 특수문자를 모두 포함하는지 확인
    const containsEnglish = /[a-zA-Z]/.test(password);
    const containsNumber = /\d/.test(password);
    const containsSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/.test(password);

    if (!(containsEnglish && containsNumber && containsSpecialChar)) {
      console.log('비밀번호는 영어, 숫자, 특수문자를 모두 포함해야 합니다.')
      return false
    }

    // 비밀번호 길이가 8자 이상, 30자 이하인지 확인
    const length = password.length
    if (length < 8 || length > 30) {
      return false
    }
    
    // 비밀번호가 유효함
    console.log('비밀번호 유효성 검사 통과')
    return true
  }

  // 닉네임 유효성 검사
  const validateNickName = function () {
    // 닉네임이 공백이면 유효하지 않음
    if (nickname === '') {
      return
    }

    // 닉네임이 한글, 영어, 숫자, 특수문자 ( _ )로만 이루어졌는지 확인
    const regExp = /^[가-힣a-zA-Z0-9_]+$/
    if (!regExp.test(nickname.lue)) {
      return
    }

    // 닉네임 길이가 4자 이상, 16자 이하인지 확인
    const length = nickname.length
    if (length < 4 || length > 16) {
      return
    }

    // 닉네임이 유효함
    console.log('닉네임 유효성 검사 통과')
    return true
  }

  return { generalLogIn, simpleLogIn, validateMemberId, validatePassword, validateNickName }
},{persist:true})
