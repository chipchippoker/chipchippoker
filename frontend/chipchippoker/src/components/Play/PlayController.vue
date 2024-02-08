<template>
  <div class="bg-lightblue rounded-4 w-100 h-100 d-flex fs-5">
    <!-- 배팅 할 코인 -->
    <div class="w-75 d-flex flex-column justify-content-evenly align-items-center">
      <div class="d-flex align-items-center">
        <button @click="minus1">
          <font-awesome-icon :icon="['fas', 'minus']" />
        </button>
        <!-- 베팅 코인 input -->
        <input v-model="bettingCoin" class="text-center" style="width: 40px; height: 40px" type="number" name="" id="">
        <button @click="plus1">
          <font-awesome-icon :icon="['fas', 'plus']" />
        </button>
      </div>
      <div class="d-flex justify-content-evenly w-100">
        <button @click="plus1" class="rounded-circle bg-modal-yello d-flex justify-content-center align-items-center"
          style="width: 40px; height: 40px">1</button>
        <button @click="plus3" class="rounded-circle bg-lightyellow d-flex justify-content-center align-items-center"
          style="width: 40px; height: 40px">3</button>
        <button @click="plus5" class="rounded-circle bg-yellow d-flex justify-content-center align-items-center"
          style="width: 40px; height: 40px">5</button>
        <button @click="all" class="rounded-circle bg-weightyellow d-flex justify-content-center align-items-center"
          style="width: 40px; height: 40px">ALL</button>
      </div>
    </div>
    <!-- 배팅 다이 -->
    <div class="d-flex flex-column justify-content-evenly align-items-center w-25">
      <button @click="bet">배팅</button>
      <button @click="die">다이</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useGameStore } from '@/stores/game';
import { useRoomStore } from '@/stores/room';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore()
const gameStore = useGameStore()
const roomStore = useRoomStore()
const bettingCoin = ref(0)
const myGameInfo = ref({})  // 내 게임 정보

// 베팅 코인 조절 함수
const plus1 = function () {
  bettingCoin.value += 1
}
const minus1 = function () {
  bettingCoin.value -= 1
}
const plus3 = function () {
  bettingCoin.value += 3
}
const plus5 = function () {
  bettingCoin.value += 5
}
// 올인
const all = function () {
  getGameInfo()
  bettingCoin.value = calculateAllinBetting() - myGameInfo.value.bettingCoin 
  console.log('bettingCoin', bettingCoin.value);
}

// 베팅
const bet = function () {
  if (betValidation()) {
    gameStore.bet(roomStore.title, "BET", bettingCoin.value)
    bettingCoin.value = 0
  }
}
const die = function () {
  if (betValidation()) {
    gameStore.bet(roomStore.title, "DIE", bettingCoin.value)
    bettingCoin.value = 0
  }
}

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
const calculateAllinBetting = function(){
  let maxBettingCoin = 200
  gameStore.gameMemberInfos.forEach(info => {
    // 살아있는 사람 중에서
    if (info.bettingCoin + info.haveCoin !== 0 && info.bettingCoin + info.haveCoin < maxBettingCoin) {
      maxBettingCoin = info.bettingCoin + info.haveCoin
    }
  })
  return maxBettingCoin
}

const calculateMinBetting = function(){
  let minBettingCoin = 0
  gameStore.gameMemberInfos.forEach(info => {
    if (info.bettingCoin + info.haveCoin !== 0 && info.bettingCoin > minBettingCoin) {
      minBettingCoin = info.bettingCoin
    }
  })
  return minBettingCoin
}


// 베팅 Validation ===================================================
const betValidation = function(){
  getGameInfo()
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
  else if (bettingCoin.value + myGameInfo.value.bettingCoin < calculateMinBetting())
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

</script>

<style scoped>
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
}</style>