import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

export const useUserStore = defineStore('counter', () => {
  
  const USER_API = 'http://localhost/api/members'
  
  const accessToken = ref(null)
  const refreshToken = ref(null)
  const kakaoAccessToken = ref(null)  
  const isNickDuplicated = ref(null)
  const isIdDuplicated = ref(null)
  const checkNickName = function (nickName){
  const userIcon = ref(null)
  
  
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



}
  return {checkNickName,accessToken, refreshToken, kakaoAccessToken, kakaoSignUp, generalLogIn, simpleLogIn, signUp, checkMemberId,
    isNickDuplicated, isIdDuplicated, validateId, validatePassword, validateNickName }
},{persist:true})
