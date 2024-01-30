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
        <button @click="plus1" class="rounded-circle bg-modal-yello d-flex justify-content-center align-items-center" style="width: 40px; height: 40px">1</button>
        <button @click="plus3" class="rounded-circle bg-lightyellow d-flex justify-content-center align-items-center" style="width: 40px; height: 40px">3</button>
        <button @click="plus5" class="rounded-circle bg-yellow d-flex justify-content-center align-items-center" style="width: 40px; height: 40px">5</button>
        <button @click="all" class="rounded-circle bg-weightyellow d-flex justify-content-center align-items-center" style="width: 40px; height: 40px">ALL</button>
      </div>
    </div>
    <!-- 배팅 다이 -->
    <div class="d-flex flex-column justify-content-evenly align-items-center w-25">
      <button v-if="notMatchRound || notYourTurn || cannotBat" @click="vibration1" id="box1" class="box1 bg-darkblue text-white fw-bold rounded-pill d-flex justify-content-center align-items-center border border-2 border-white" style="width: 60px; height: 40px">배팅</button>
      <button v-else @click="bet" class="bg-darkblue text-white fw-bold rounded-pill d-flex justify-content-center align-items-center border border-2 border-white" style="width: 60px; height: 40px">배팅</button>
      <button v-if="notMatchRound || notYourTurn || cannotBat" @click="vibration2" id="box2" class="box2 bg-danger bg-opacity-75 text-white fw-bold rounded-pill d-flex justify-content-center align-items-center border border-2 border-white" style="width: 60px; height: 40px">다이</button>
      <button v-else @click="die" class="bg-danger bg-opacity-75 text-white fw-bold rounded-pill d-flex justify-content-center align-items-center border border-2 border-white" style="width: 60px; height: 40px">다이</button>
    </div>
  </div>
</template>

<script setup>
  import { ref, computed } from 'vue';
  import { useGameStore } from '@/stores/game';
  import { useRoomStore } from '@/stores/room';
  const gameStore = useGameStore()
  const roomStore = useRoomStore()
  const bettingCoin = ref(0)
  // 베팅 코인 조절 함수
  const plus1 = function(){
    bettingCoin.value += 1
  }
  const minus1 = function(){
    bettingCoin.value -= 1
  }
  const plus3 = function(){
    bettingCoin.value += 3
  }
  const plus5 = function(){
    bettingCoin.value += 5
  }
  const all = function(){
    bettingCoin.value = gameStore?.gameMemberInfos[0]?.havingCoin
  }

  // 베팅
  const bet = function(){
    gameStore.bet(roomStore.title,"BET", bettingCoin.value)
  }
  const die = function(){
    gameStore.bet(roomStore.title,"DIE", bettingCoin.value)
  }

  gameStore.bettingCoin = bettingCoin.value


  // 배팅 오류
  const notMatchRound = ref(false)
  const notYourTurn = ref(false)
  const cannotBat = ref(false)

  notMatchRound.value = computed(() => gameStore.notMatchRound)
  notYourTurn.value = computed(() => gameStore.notYourTurn)
  cannotBat.value = computed(() => gameStore.cannotBat)

  // 부르르
  const vibration1 = () => {
    const box = document.querySelector('#box1')
    box.classList.add("vibration");

    // 각각의 모달 닫기 로직을 작성
    gameStore.notMatchRound = false;
    gameStore.notYourTurn = false;
    gameStore.cannotBat = false;

    setTimeout(function() {
      box.classList.remove("vibration");
    }, 400);
  }  
  const vibration2 = () => {
    const box = document.querySelector('#box2')
    box.classList.add("vibration");

    // 각각의 모달 닫기 로직을 작성
    gameStore.notMatchRound = false;
    gameStore.notYourTurn = false;
    gameStore.cannotBat = false;

    setTimeout(function() {
      box.classList.remove("vibration");
    }, 400);
  }  
</script>

<style scoped>
input[type=number]::-webkit-inner-spin-button {
  -webkit-appearance: none;
}
.box1 {
  width: 50px;
  height:50px;
  margin: .4rem;
  background: #febf00;
  cursor: pointer;
}
.box1.vibration {
  animation: vibration .1s infinite;
}
.box2 {
  width: 50px;
  height:50px;
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
</style>