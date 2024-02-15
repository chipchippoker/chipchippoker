<template>
  <div class="container bg-lightblue rounded-4 w-100 h-100 d-flex fs-5">
    <div class="row">

      <!-- 1. 타이머 -->
      <div class="row col-4 text-center align-items-center justify-content-center">
        <!-- <h1>{{ timer }}</h1> -->
        <!-- <div class="timer">
          <div class="mask"></div>
        </div> -->
      </div>

      <!-- 2. 배팅 할 코인 -->
      <div class="col-4 d-flex flex-column justify-content-evenly align-items-center">

        <!-- 베팅 코인 input -->
        <div class="d-flex align-items-center">
          <button @click="minus1">
            <font-awesome-icon :icon="['fas', 'minus']" />
          </button>
          <input v-model="bettingCoin" class="text-center" style="width: 40px; height: 40px; border-radius: 5px;" type="number">
          <button @click="plus1">
            <font-awesome-icon :icon="['fas', 'plus']" />
          </button>
        </div>

        <!-- 옵션 -->
        <div class="d-flex justify-content-evenly w-100">
          <button @click="plus1" class="rounded-circle bg-modal-yello d-flex justify-content-center align-items-center"
            style="width: 40px; height: 40px">1</button>
          <button @click="plus3" class="rounded-circle bg-lightyellow d-flex justify-content-center align-items-center"
            style="width: 40px; height: 40px">3</button>
          <button @click="plus5" class="rounded-circle bg-yellow d-flex justify-content-center align-items-center"
            style="width: 40px; height: 40px">5</button>
        </div>

      </div>

      <!-- 3. 배팅 콜 올인 다이 -->
      <div class="row col-4 p-2 justify-content-center align-items-center py-3 pe-3">
        <div class="col-4">
          <button @click="bet" class="btn-1 btn-bet">배팅</button>
        </div>
        <div class="col-2"></div>
        <div class="col-4">
          <button @click="call" class="btn-1 btn-call">콜</button>
        </div>

        <div class="col-4">
          <button @click="allIn" class="btn-1 btn-allin">올인</button>
        </div>
        <div class="col-2"></div>
        <div class="col-4">
          <button @click="die" class="btn-1 btn-die">다이</button>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useGameStore } from '@/stores/game';
import { useRoomStore } from '@/stores/room';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore()
const gameStore = useGameStore()
const roomStore = useRoomStore()
const bettingCoin = ref(0)
const myGameInfo = ref({})  // 내 게임 정보
const maxBettingCoin = ref(200)
const minBettingCoin = ref(0)

// 타이머
const timer = ref(20)
const reduceTime = ref()

// 배팅을 했는지 안했는지
const isBetting = ref(false)

// 웹소켓 메시지 수신 시 최대 배팅 코인 업데이트
watch(() => gameStore.gameMemberInfos, () => {
  getGameInfo()
  calculateMaxBettingCoin()
  calculateMinBettingCoin()
})

// 베팅 코인 조절 함수
const plus1 = function () {
  if (bettingCoin.value < maxBettingCoin.value) {
    bettingCoin.value += 1
  }
}
const minus1 = function () {
  if (bettingCoin.value > 0) {
    bettingCoin.value -= 1
  }
}
const plus3 = function () {
  if (bettingCoin.value + 3 <= maxBettingCoin.value) {
    bettingCoin.value += 3
  }
}
const plus5 = function () {
  if (bettingCoin.value + 5 <= maxBettingCoin.value) {
    bettingCoin.value += 5
  }
}

// 올인
const allIn = function () {
  bettingCoin.value = maxBettingCoin.value
  if (betValidation()) {
    gameStore.bet(roomStore.title, "BET", bettingCoin.value)
    bettingCoin.value = 0
  }
}

// 베팅
const bet = function () {
  if (betValidation()) {
    gameStore.bet(roomStore.title, "BET", bettingCoin.value)
    bettingCoin.value = 0
    isBetting.value = true
  }
}

// 콜
const call = function () {
  bettingCoin.value = minBettingCoin.value
  if (betValidation()) {
    gameStore.bet(roomStore.title, "BET", bettingCoin.value)
    bettingCoin.value = 0
  }
}

// 다이
const die = function () {
  if (gameStore.yourTurn === userStore.myNickname) {
    bettingCoin.value = 0
    isBetting.value = true
    gameStore.bet(roomStore.title, "DIE", bettingCoin.value)

  } else {
    alert("본인 차례가 아닙니다.")
  }
}

// 내려고 하는 배팅 코인 감지
watch(() => bettingCoin.value, (newValue, oldValue)=>{
  gameStore.willBettingCoin = bettingCoin.value
})


// 타이머 실행함수
// const timerSetting = function(){
//       // 1초마다 한번씩 호출되는 함수
//   reduceTime.value = setInterval(()=>{
//       // 만약 timer의 시간이 있다면 1초씩 감소
//     if (timer.value > 0){
//       timer.value -= 1
//       console.log('남은 시간',timer.value)
//       // 만약 타이머가 0초이고 현재 턴이 나의 턴이라면 다이를 보냄
//     } else if (gameStore.yourTurn === userStore.myNickname ){
//       die()
//       // 타이머가 0초이고 나의 턴이 아닐때는 멈추기
//     } else{
//       console.log("멈추기")
//       clearInterval(reduceTime.value)
//     }
//   },1000)
// }

// watch(()=>gameStore.yourTurn,()=>{
//   timer.value = 20
//   isBetting.value = false
//   // timerSetting()
// })

gameStore.bettingCoin = bettingCoin.value

// 내 게임 정보 추출하기
const getGameInfo = function(){
  gameStore.gameMemberInfos.forEach(info => {
    if (info.nickname === userStore.myNickname) {
      myGameInfo.value = info
    }
  })
}

// 최대 배팅 코인 구하기
const calculateMaxBettingCoin = function(){
  let maxCoin = 200
  gameStore.gameMemberInfos.forEach(info => {
    // 살아있는 사람 중에서
    if (info.bettingCoin + info.haveCoin !== 0 && info.bettingCoin + info.haveCoin < maxCoin) {
      maxCoin = info.bettingCoin + info.haveCoin
    }
  })
  maxBettingCoin.value = maxCoin - myGameInfo.value.bettingCoin 
}

// 최소 배팅 코인 구하기
const calculateMinBettingCoin = function(){
  let minCoin = 0 
  gameStore.gameMemberInfos.forEach(info => {
    if (info.bettingCoin + info.haveCoin !== 0 && info.bettingCoin > minCoin) {
      minCoin = info.bettingCoin
    }
  })
  minBettingCoin.value = minCoin - myGameInfo.value.bettingCoin
}


// 베팅 Validation ===================================================
const betValidation = function(){
  // 0 미만의 코인을 베팅하려고 하거나
  if (bettingCoin.value < 0)
  {
    alert("코인을 베팅해주세요.")
    return false
  }
  // 보유 코인보다 많은 금액을 베팅하려고 할 때
  else if (bettingCoin.value > myGameInfo.value.haveCoin)
  {
    alert("현재 자신이 가지고 있는 코인보다 많이 베팅할 수 없습니다.")
    return false
  }
  // 현재 필드에 나와있는 최대 베팅 금액보다 적은 금액을 베팅하려고 할 때 -> 첫 턴일 때 생각해봐야 함
  else if (bettingCoin.value + myGameInfo.value.bettingCoin < minBettingCoin.value)
  {
    alert("현재 최대 베팅 코인보다 적게 베팅할 수 없습니다.")
    return false
  }
  // 나의 턴이 아닐 때 controller 건드리는 경우
  else if (gameStore.yourTurn !== userStore.myNickname)
  {
    alert("나의 턴이 아닐땐 배팅할 수 없습니다.")
    return false
  }
  return true
}

// ===================================================================
// 타이머

</script>

<style lang="scss" scoped>

input[type=number]::-webkit-inner-spin-button {
  -webkit-appearance: none;
}

.box1 {
  width: 50px;
  height: 50px;
  margin: .4rem;
  background: #febf00;
  cursor: pointer;
}

.box1.vibration {
  animation: vibration .1s infinite;
}

.box2 {
  width: 50px;
  height: 50px;
  margin: .4rem;
  background: #febf00;
  cursor: pointer;
}

.box2.vibration {
  animation: vibration .1s infinite;
}

@keyframes vibration {
  from {
    transform: rotate(10deg);
  }

  to {
    transform: rotate(-10deg);
  }
}

 // 타이머
 .timer {
    background: -webkit-linear-gradient(left, skyBlue 50%, #eee 50%);
    /* Foreground color, Background colour */
    border-radius: 100%;
    height: 100px;
    /* Height and width */
    position: relative;
    width: 100px;
    /* Height and width */
    animation-name: time;
    animation-duration: 20s;
    animation-timing-function: linear;
    animation-iteration-count: infinite;
}
.mask {
    border-radius: 100% 0 0 100% / 50% 0 0 50%;
    height: 100%;
    left: 0;
    position: absolute;
    top: 0;
    width: 50%;
   
    animation-name: mask;
    animation-duration: 20s;
    animation-timing-function: linear;
    animation-iteration-count: infinite;
    /* Animation time and number of steps (halved) */
    -webkit-transform-origin: 100% 50%;
}
@-webkit-keyframes time {
    100% {
        -webkit-transform: rotate(360deg);
    }
}
@-webkit-keyframes mask {
    0% {
        background: #eee;
        /* Background colour */
        -webkit-transform: rotate(0deg);
    }
    50% {
        background: #eee;
        /* Background colour */
        -webkit-transform: rotate(-180deg);
    }
    50.01% {
        background: skyBlue;
        /* Foreground colour */
        -webkit-transform: rotate(0deg);
    }
    100% {
        background: skyBlue;
        /* Foreground colour */
        -webkit-transform: rotate(-180deg);
    }
}
</style>