<template>

  <!-- 배팅 보드 가운데 위치시키기 -->
  <div id="bettingField" class="container position-absolute top-50 start-50 translate-middle battingstyle p-0 z-0 card-winner" style="width: calc(100% - 620px); height: 90%;">
    <video
      id="video"
      v-if="gameStart"
      style="z-index: 9999;
      position: fixed;
      width: 100vw;
      height: 100vh;"
      src="/src/assets/icons/chipchippoker.mp4" alt="비디오 없다" autoplay>
    </video>
    <img v-for="index in gameStore.memberInfos.length" :key="index" src="/src/assets/cards/set0/card0.svg" alt=""
    :id="`card-deck${index}`"
    style="width: 100px; z-index: 2000; position: absolute;">
    <div class="row position-relative w-100 h-100 d-flex justify-content-center align-items-center">


      <!-- 모인 코인 -->
      <div class="position-absolute d-flex justify-content-center align-items-center">
        <div class="flex-wrap" style="width: 100px; z-index: 1999;">
          <div class="justify-content-center text-white text-center fw-bold">
            <p class="mb-1">총 배팅 코인: {{ totalBettingCoin() }}</p>
            <div id="total-coin" class="d-flex justify-content-center flex-wrap" style="width: 100px;">
              <span>
                <img style="width: 30px;" v-for="index in gameStore?.totalBettingCoin[0]" :key="index"
                  class="list-overlap-small" :src="getCoinUrl(1)" alt="">
              </span>
              <span v-if="gameStore?.gameMemberInfos?.length > 1">
                <img style="width: 30px;" v-for="index in gameStore?.totalBettingCoin[1]" :key="index"
                class="list-overlap-small" :src="getCoinUrl(2)" alt="">
              </span>
              <span v-if="gameStore?.gameMemberInfos?.length > 2">
                <img style="width: 30px;" v-for="index in gameStore?.totalBettingCoin[2]" :key="index"
                class="list-overlap-small" :src="getCoinUrl(3)" alt="">
              </span>
              <span v-if="gameStore?.gameMemberInfos?.length > 3">
                <img style="width: 30px;" v-for="index in gameStore?.totalBettingCoin[3]" :key="index"
                class="list-overlap-small" :src="getCoinUrl(4)" alt="">
              </span>
              <span>
                <img style="width: 30px;" v-for="index in gameStore?.willBettingCoin" :key="index"
                  class="list-overlap-small opacity-50" :src="getCoinUrl(getMyIndex())" alt="">
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- 위쪽 -->
      <div class="row col-12 justify-content-between">

        <!-- 1번 플레이어 -->
        <div class="row col-6">
          <!-- 카드 -->
          <div class="col-5" id="player1">
            <!-- 뒤집을 카드 -->
            <div class="flip-card" id="flip-card1">
              <div class="flip-card-inner">
                  <div class="flip-card-front">
                    <img id="card" :src="getCardUrl(0,0)" alt="뒷면카드">
                  </div>
                  <div class="flip-card-back">
                    <img id="card" :src="getCardUrl(gameStore?.gameMemberInfos[0]?.cardInfo?.cardSet, gameStore?.gameMemberInfos[0]?.cardInfo?.cardNumber)" alt="앞장">
                  </div>
              </div>
            </div>
          </div>
          <!-- 코인 -->
          <div class="col-6 d-flex flex-column justify-content-between align-items-center">
            <!-- 보유 코인 -->
            <div class="text-white text-center mb-4"> 
              <p class="mb-1">보유코인 : {{ gameStore?.gameMemberInfos[0]?.haveCoin }}</p>
              <div v-if="gameStore?.gameMemberInfos[0]?.nickname === userStore.myNickname" class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[0]?.haveCoin - gameStore?.willBettingCoin" :key="index"
                :class="{ 'list-overlap-middle': gameStore?.gameMemberInfos[0]?.haveCoin > 30 && gameStore?.gameMemberInfos[0]?.haveCoin <60, 
                'list-overlap-small': gameStore?.gameMemberInfos[0]?.haveCoin <= 30,
                'list-overlap-much':gameStore?.gameMemberInfos[0]?.haveCoin >=60 }" :src="getCoinUrl(1)" alt="">
                <!-- 내려고 하는 코인 -->
                <img style="width: 30px;" v-for="index in gameStore?.willBettingCoin" :key="index"
                  class="list-overlap-small opacity-50" :src="getCoinUrl(1)" alt="">
              </div>
              <div v-else class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[0]?.haveCoin" :key="index"
                :class="{ 'list-overlap-middle': gameStore?.gameMemberInfos[0]?.haveCoin > 30 && gameStore?.gameMemberInfos[0]?.haveCoin <60, 
                'list-overlap-small': gameStore?.gameMemberInfos[0]?.haveCoin <= 30,
                'list-overlap-much':gameStore?.gameMemberInfos[0]?.haveCoin >=60 }" :src="getCoinUrl(1)" alt="">
              </div>
            </div>  
          </div>
          <!-- 공백 -->
          <div class="col-1">

          </div>
        </div>

        <!-- 2번 플레이어 -->
        <div class="row col-6" v-if="gameStore.memberInfos && gameStore.memberInfos.length > 1">
          <!-- 공백 -->
          <div class="col-2">

          </div>
          <!-- 코인 -->
          <div class="col-5 d-flex flex-column justify-content-between align-items-center">
            <!-- 보유 코인 -->
            <div class="text-white text-center mb-4">
              <p class="mb-1">보유코인: {{ gameStore?.gameMemberInfos[1]?.haveCoin }}</p>
              <div v-if="gameStore?.gameMemberInfos[1]?.nickname === userStore.myNickname" class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[1]?.haveCoin - gameStore?.willBettingCoin" :key="index"
                :class="{ 'list-overlap-middle': gameStore?.gameMemberInfos[1]?.haveCoin > 30 && gameStore?.gameMemberInfos[1]?.haveCoin <60, 
                'list-overlap-small': gameStore?.gameMemberInfos[1]?.haveCoin <= 30,
                'list-overlap-much':gameStore?.gameMemberInfos[1]?.haveCoin >=60 }" :src="getCoinUrl(2)" alt="">
                <img style="width: 30px;" v-for="index in gameStore?.willBettingCoin" :key="index"
                  class="list-overlap-small opacity-50" :src="getCoinUrl(2)" alt="">
              </div>
              <div v-else class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[1]?.haveCoin" :key="index"
                :class="{ 'list-overlap-middle': gameStore?.gameMemberInfos[1]?.haveCoin > 30 && gameStore?.gameMemberInfos[1]?.haveCoin <60, 
                'list-overlap-small': gameStore?.gameMemberInfos[1]?.haveCoin <= 30,
                'list-overlap-much':gameStore?.gameMemberInfos[1]?.haveCoin >=60 }" :src="getCoinUrl(2)" alt="">
              </div>
            </div>
          </div>
          <!-- 카드 -->
          <div class="col-5">
            <!-- 뒤집을 카드 -->
            <div class="flip-card" id="flip-card2">
              <div class="flip-card-inner">
                  <div class="flip-card-front">
                    <img id="card" :src="getCardUrl(0,0)" alt="뒷면카드">
                  </div>
                  <div class="flip-card-back">
                    <img id="card" :src="getCardUrl(gameStore?.gameMemberInfos[1]?.cardInfo?.cardSet, gameStore?.gameMemberInfos[1]?.cardInfo?.cardNumber)" alt="앞장">
                  </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 아래쪽 -->
      <div class="row col-12 justify-content-between">

        <!-- 3번 플레이어 -->
        <div class="row col-6" v-if="gameStore.memberInfos && gameStore.memberInfos.length > 2">
          <!-- 카드 -->
          <div class="col-5">
            <!-- 뒤집을 카드 -->
            <div class="flip-card" id="flip-card3">
              <div class="flip-card-inner">
                  <div class="flip-card-front">
                    <img id="card" :src="getCardUrl(0,0)" alt="뒷면카드">
                  </div>
                  <div class="flip-card-back">
                    <img id="card" :src="getCardUrl(gameStore?.gameMemberInfos[2]?.cardInfo?.cardSet, gameStore?.gameMemberInfos[2]?.cardInfo?.cardNumber)" alt="앞장">
                  </div>
              </div>
            </div>
          </div>
          <!-- 코인 -->
          <div class="col-6 d-flex flex-column justify-content-between align-items-center">
            <!-- 보유코인 -->
            <div  v-if="gameStore?.gameMemberInfos[2]?.nickname === userStore.myNickname" class="text-white text-center">
              <p class="mb-1">보유코인: {{ gameStore?.gameMemberInfos[2]?.haveCoin }}</p>
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[2]?.haveCoin-gameStore?.willBettingCoin" :key="index"
                  :class="{ 'list-overlap-middle': gameStore?.gameMemberInfos[2]?.haveCoin > 30 && gameStore?.gameMemberInfos[2]?.haveCoin <60, 
                  'list-overlap-small': gameStore?.gameMemberInfos[2]?.haveCoin <= 30,
                  'list-overlap-much':gameStore?.gameMemberInfos[2]?.haveCoin >=60 }" :src="getCoinUrl(3)" alt="">
                <img style="width: 30px;" v-for="index in gameStore?.willBettingCoin" :key="index"
                class="list-overlap-small opacity-50" :src="getCoinUrl(3)" alt="">
              </div>
            </div>
            <div v-else class="text-white text-center">
              <p class="mb-1">보유코인: {{ gameStore?.gameMemberInfos[2]?.haveCoin }}</p>
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[2]?.haveCoin" :key="index"
                  :class="{ 'list-overlap-middle': gameStore?.gameMemberInfos[2]?.haveCoin > 30 && gameStore?.gameMemberInfos[2]?.haveCoin <60, 
                  'list-overlap-small': gameStore?.gameMemberInfos[2]?.haveCoin <= 30,
                  'list-overlap-much':gameStore?.gameMemberInfos[2]?.haveCoin >=60 }" :src="getCoinUrl(3)" alt="">
              </div>
            </div>
          </div>
          <!-- 공백 -->
          <div class="col-1">

          </div>
        </div>

        <!-- 4번 플레이어 -->
        <!-- 코인 -->
        <div class="row col-6" v-if="gameStore.memberInfos && gameStore.memberInfos.length > 3">
          <!-- 공백 -->
          <div class="col-2">

          </div>
          <!-- 코인 -->
          <div class="col-5 d-flex flex-column justify-content-between align-items-center">
            <!-- 보유 코인 -->
            <div v-if="gameStore?.gameMemberInfos[3]?.nickname === userStore.myNickname"  class="text-white text-center">
              <p class="mb-1">보유코인: {{ gameStore?.gameMemberInfos[3]?.haveCoin }}</p>
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[3]?.haveCoin - gameStore?.willBettingCoin" :key="index"
                  :class="{ 'list-overlap-middle': gameStore?.gameMemberInfos[3]?.haveCoin > 30 && gameStore?.gameMemberInfos[3]?.haveCoin <60, 
                  'list-overlap-small': gameStore?.gameMemberInfos[3]?.haveCoin <= 30,
                  'list-overlap-much':gameStore?.gameMemberInfos[3]?.haveCoin >=60 }" :src="getCoinUrl(4)" alt="">
                <img style="width: 30px;" v-for="index in gameStore?.willBettingCoin" :key="index"
                class="list-overlap-small opacity-75" :src="getCoinUrl(4)" alt="">
              </div>
            </div>
            <div v-else class="text-white text-center">
              <p class="mb-1">보유코인: {{ gameStore?.gameMemberInfos[3]?.haveCoin }}</p>
              <div class="d-flex justify-content-center flex-wrap" style="width: 100px;">
                <img style="width: 30px;" v-for="index in gameStore?.gameMemberInfos[3]?.haveCoin" :key="index"
                  :class="{ 'list-overlap-middle': gameStore?.gameMemberInfos[3]?.haveCoin > 30 && gameStore?.gameMemberInfos[3]?.haveCoin <60, 
                  'list-overlap-small': gameStore?.gameMemberInfos[3]?.haveCoin <= 30,
                  'list-overlap-much':gameStore?.gameMemberInfos[3]?.haveCoin >=60 }" :src="getCoinUrl(4)" alt="">
              </div>
            </div>
          </div>

          <!-- 카드 -->
          <div class="col-5">
            <!-- 뒤집을 카드 -->
            <div class="flip-card" id="flip-card4">
              <div class="flip-card-inner">
                  <div class="flip-card-front">
                    <img id="card" :src="getCardUrl(0,0)" alt="뒷면카드">
                  </div>
                  <div class="flip-card-back">
                    <img id="card" :src="getCardUrl(gameStore?.gameMemberInfos[3]?.cardInfo?.cardSet, gameStore?.gameMemberInfos[3]?.cardInfo?.cardNumber)" alt="앞장">
                  </div>
              </div>
            </div> 
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { watch, computed, ref, onBeforeUnmount, onMounted } from 'vue';
import { useGameStore } from '@/stores/game';
import { useRoomStore } from '@/stores/room';
import { useUserStore } from '@/stores/user';
import { useSoundStore } from '@/stores/sound';

const userStore = useUserStore()
const gameStore = useGameStore()
const roomStore = useRoomStore()
const soundStore = useSoundStore()

const gameStart = ref(false)
const setTimeList = ref([])

const nextYourTurn = computed(() => gameStore.nextYourTurn) // 턴 변화
const nextRoundState = computed(() => gameStore.nextRoundState); // 현재 라운드 상태 (ex. 진행중)
const bettingEvent = computed(() => gameStore.bettingEvent) // 배팅 이벤트 감지
const willBettingCoin = computed(() => gameStore.willBettingCoin) // 내려고 하는 배팅 코인 감지

const cardPosition = [["-10%", "20%"], ["-10%", "50%"], ["80%", "20%"], ["80%", "50%"]]

// 내 인덱스 구하기
const getMyIndex = function () {
  const myindex = ref(0)
  gameStore?.gameMemberInfos?.forEach((info, index) => {
    // console.log(info, index);
    if (info.nickname === userStore.myNickname) {
      myindex.value = index + 1
    }
  })
  return myindex.value
}

// 총 배팅 코인 구하기
const totalBettingCoin = function(){
  let coinCnt = 0
  gameStore.totalBettingCoin.forEach(coin =>{
    coinCnt += coin
  })
  return coinCnt
}

// 배팅코인 초기화
const updateTotalBettingCoin = function(){
  gameStore?.nextGameMemberInfos?.forEach((info,index) =>{
    gameStore.totalBettingCoin[index] = info.bettingCoin
  })
}

// 코인 가져오기
const getCoinUrl = function (num) {
  return new URL(`/src/assets/coins/coin${num}.svg`, import.meta.url).href;
};

// 카드 가져오기
const getCardUrl = function (setnum, cardnum) {
  return new URL(`/src/assets/cards/set${setnum}/card${cardnum}.svg`, import.meta.url).href;
};

// 데이터 저장하기
async function updateData () {
  // console.log('데이터 새로 저장');
  gameStore.roundState = gameStore.nextRoundState
  gameStore.yourTurn = gameStore.nextYourTurn
  gameStore.currentRound = gameStore.nextCurrentRound
  gameStore.gameMemberInfos = gameStore.nextGameMemberInfos
  gameStore.penaltyInfos = gameStore.nextPenaltyInfos
}

// 배팅코인과 카드데이터만 받아옴
async function updateEndData () {
  // console.log('마지막데이터 새로 저장');
  gameStore.roundState = gameStore.nextRoundState
  gameStore.currentRound = gameStore.nextCurrentRound
  gameStore.yourTurn = gameStore.nextYourTurn
  
  gameStore.nextGameMemberInfos.forEach((info, index) => {
    gameStore.gameMemberInfos[index].bettingCoin = info.bettingCoin
    gameStore.gameMemberInfos[index].cardInfo = info.cardInfo
  })
  gameStore.penaltyInfos = gameStore.nextPenaltyInfos
}

// 비디오 요소에 스타일을 적용합니다.
// const videoElement = document.createElement('video')
// videoElement.src = "/src/assets/icons/chipchippoker.mp4"
// videoElement.autoplay = true
// videoElement.style.zIndex = "2000"
// videoElement.style.position = "fixed"
// videoElement.style.width = "100vw"
// videoElement.style.height = "100vh"
// const container = document.querySelector('.container')
// container.appendChild(videoElement)

// videoElement.addEventListener('ended', () => {
//   gameStart.value = false
//   videoElement.remove()
  // console.log(gameStart.value)
  // console.log('비디오 재생 완료!')
//   startRoundAnimation()
// })

// 애니메이션 진행 상태 토글
async function toggleAnimationState () {
  console.log(gameStore.isAnimationRunning, !gameStore.isAnimationRunning);
  gameStore.isAnimationRunning = !gameStore.isAnimationRunning
}

// 라운드 시작 콜백함수
async function startRoundAnimation () {
    // -1. 애니메이션 상태 토글
    await toggleAnimationState()
    // 0. 코인 보여주기
    await gameStore.gameMemberInfos.forEach((info, index) => {
        const totalCoinId = document.getElementById('total-coin')
        totalCoinId.classList.remove(`coin-devide-move${index+1}`)
        totalCoinId.classList.remove('fade-out')
    })

    // 1. 화면 가운데 카드 생성 애니메이션 (createCard)
    // 2. 각 플레이어에게 카드 분배 애니메이션 (moveCard 카드 이동)
    // 3. 분배 완료 후 카드 사라짐 (FadeoutCard)
    await createCard()
    await moveCard()
    // await removeCard()
    await flipCard()
    await toggleAnimationState()
}

// 게임 시작 애니메이션
async function startGameAnimation () {
  // 0. 애니메이션 상태 토글
  await toggleAnimationState()
  // 1. 플레이페이지 진입하면 텍스트 애니메이션 (3초 정도)
  gameStart.value = true
  // console.log('게임 비디오 시작', gameStart.value)

  const time = setTimeout(()=>{
    gameStart.value = false
    // console.log('비디오 끌거임');
    // // const videoElement = document.getElementById('video')
    // videoElement.remove()
    startRoundAnimation()
  },5000)
  setTimeList.value.push(time)
  await toggleAnimationState()
}

// 라운드 변경 이벤트
watch(() => nextRoundState.value, (newValue, oldValue) => {
  if ( newValue === 0){
    // console.log('게임 종료했음')
  } else{
    // console.log('라운드 변경')
    if (newValue === true && oldValue === false) {
      if (gameStore.nextCurrentRound === 1) {
        // console.log('게임시작')
        // 게임 시작
        startGameAnimation()
        updateData()

      } else {
        // 라운드 시작
        // console.log('라운드시작')

        startRoundAnimation()
        updateData()
        updateTotalBettingCoin()

      }
    }
    else if (newValue === false && oldValue === true) {
      // 라운드 종료
      console.log('라운드종료')
      updateEndData()
      updateTotalBettingCoin()
      // setTimeList.value.forEach(time => {
      //   clearTimeout(time)
      // })
      endRoundAnimation()

    }
  }
  })

// 턴 변경 이벤트
watch(() =>[ nextYourTurn.value, nextRoundState.value], (newValue, oldValue) => {
  // console.log('턴 변경')
  console.log(newValue, oldValue);
  // updateData()
})

// 배팅 이벤트
watch(() => bettingEvent.value, (newValue, oldValue) => {
  // console.log('배팅 관련 무언가 변화', newValue, oldValue);
  if (newValue === true && oldValue === false) {
    console.log('배팅 이벤트 발생')
    updateData()
    gameStore.bettingEvent = false
    // 코인 배팅 (코인 움직이기)
    bettingCoin()

  }
})

// 라운드 종료 콜백함수
async function endRoundAnimation () {
  // 0. 애니메이션 상태 토글
  await toggleAnimationState()
  // 1. 내 카드 뒤집기
  await filpMyCard()
  // 2. 모든 카드 뒤로 뒤집기
  await flipCardBack()
  // 3. 가운데 모든 카드 띄우기 (카드 스타일 주기)
  await joinCard()
  // 4. 카드 페이드아웃
  await fadeOutCard()
  // 5. 카드 없애기
  await removeCardEnd()
  // 6. 코인 회수
  await joinCoin()
  // 7. 데이터 업데이트
  await updateData()
  // 8. 애니메이션 상태 토글
  await toggleAnimationState()
}

// 카드 생성 (가운데)
async function createCard() {
  // console.log("카드 생성");

  for (let i = 1; i < gameStore.memberInfos.length+1; i++) {
    const cardElement = document.getElementById(`card-deck${i}`)
    cardElement.classList.remove(`card-devide-move${i}`)
    cardElement.classList.remove(`fade-out`)
  }
  return new Promise(resolve => {
    const time = setTimeout(() => {
      // console.log('카드 생성 애니메이션 완료');
      resolve() // 생성 완료
    }, 1000)
    setTimeList.value.push(time)
  })
}

// 카드 분배
async function moveCard() {
  // console.log("카드 분배")
  for (let i = 1; i < gameStore.memberInfos.length+1; i++) {
    const cardElement = document.getElementById(`card-deck${i}`)
    cardElement.classList.add(`card-devide-move${i}`)
    cardElement.classList.add(`fade-out`)
  }
  return new Promise(resolve => {
    soundStore.cardshuffleSound()
    const time = setTimeout(() => {
      // console.log('카드 분배 애니메이션 완료');
      resolve(); // 생성 완료
    }, 1000)
    setTimeList.value.push(time)
  })
}

// 카드 페이드아웃
async function fadeOutCard() {
  // console.log("카드 페이드아웃")
  for (let i = 1; i < gameStore.memberInfos.length+1; i++) {
    const cardElement = document.getElementById(`card-deck${i}`)
    cardElement.classList.add(`fade-out`)
  }
  return new Promise(resolve => {
    const time = setTimeout(() => {
      // console.log('카드 페이드아웃 애니메이션 완료')
      resolve(); // 생성 완료
    }, 2000)
    setTimeList.value.push(time)
  })
}

// 카드 제거
async function removeCard() {
  return new Promise(resolve => {
    const time = setTimeout(() => {
      for (let i = 1; i < gameStore.memberInfos.length+1; i++) {
        const cardElement = document.getElementById(`card-deck${i}`)
        cardElement.remove()
      }
      // console.log('카드 제거 애니메이션 완료')
      resolve(); // 생성 완료
    }, 1000)
    setTimeList.value.push(time)
  })
}
async function removeCardEnd() {
  return new Promise(resolve => {
    const time = setTimeout(() => {
      for (let i = 1; i < gameStore.memberInfos.length+1; i++) {
        const cardElement = document.getElementById(`end-card${i}`)
        cardElement.remove()
      }
      // console.log('카드 제거 애니메이션 완료')
      resolve(); // 생성 완료
    }, 1000)
    setTimeList.value.push(time)
  })
}

// 내 카드 뒤집기
async function filpMyCard() {
  if (!roomStore.isWatcher) {
    const myIndex = getMyIndex()
    const cardElement = document.getElementById(`flip-card${myIndex}`)
    cardElement.classList.add('flipped')
  
    return new Promise(resolve => {
      const time = setTimeout(() => {
        // console.log('카드 모으기 애니메이션 완료');
        resolve(); // 생성 완료
      }, 1000)
      setTimeList.value.push(time)
    })
  }
}

// 내 카드 빼고 뒤집기
async function flipCard() {
  const myIndex = getMyIndex()
  for (let i = 1; i < gameStore.memberInfos.length+1; i++) {
    // 내 카드는 넘긴다.
    if(i === myIndex && !roomStore.isWatcher){
      continue
    }
    const cardElement = document.getElementById(`flip-card${i}`)
    cardElement.classList.add('flipped')
  }
  return new Promise(resolve => {
    const time = setTimeout(() => {
      // console.log('카드 모으기 애니메이션 완료')
      resolve(); // 생성 완료
    }, 1000)
    setTimeList.value.push(time)
  })
}

// 모든 카드 뒤로 뒤집기
async function flipCardBack() {
  for (let i = 1; i < gameStore.memberInfos.length+1; i++) {
    const cardElement = document.getElementById(`flip-card${i}`)
    cardElement.classList.remove('flipped')
    }
  return new Promise(resolve => {
    const time = setTimeout(() => {
      // console.log('카드 모으기 애니메이션 완료');
      resolve(); // 생성 완료
    }, 1000)
    setTimeList.value.push(time)
  })
}

// 카드 모으기 (라운드 승패 판단)
async function joinCard () {
  // console.log('카드 모으기')

  const container = document.getElementById('bettingField');

  gameStore.gameMemberInfos.forEach((data, index) => {
    const divTag = document.createElement('div')
    divTag.id = `end-card${index+1}`
    divTag.style.width = "130px"
    divTag.style.height = "230px"
    divTag.style.zIndex = "10000"
    divTag.style.position = "absolute"
    divTag.style.margin = '20px'
    divTag.classList.add('text-center')
    divTag.style.top = cardPosition[index][0]
    divTag.style.left = cardPosition[index][1]
    
    if (gameStore.winnerNickname === data.nickname) {
      const winnerTag = document.createElement('h3')
      winnerTag.style.color = 'blue'
      winnerTag.style.fontWeight = 'bold'
      winnerTag.style.zIndex = '10001'
      winnerTag.style.backgroundColor = 'white'
      winnerTag.style.borderRadius = '10px'

      winnerTag.innerText = '승'
      divTag.appendChild(winnerTag)
    } else {
      const LoserTag = document.createElement('h3')
      LoserTag.style.color = 'red'
      LoserTag.style.fontWeight = 'bold'
      LoserTag.style.zIndex = '10001'
      LoserTag.style.backgroundColor = 'white'
      LoserTag.style.borderRadius = '10px'
      LoserTag.innerText = "패"
      
      // 패널티 부여
      gameStore?.penaltyInfos?.forEach(info => {
        if (info.nickname === data.nickname) {
          LoserTag.innerText = `패 (패널티:-${info.penaltyCoin})`
        }
      })
      divTag.appendChild(LoserTag)
    }
    const cardElement = document.createElement('img')
    cardElement.src = getCardUrl(data.cardInfo.cardSet, data.cardInfo.cardNumber)
    container.appendChild(divTag);
    divTag.appendChild(cardElement);
  });
  return new Promise(resolve => {
    const time = setTimeout(() => {
      // console.log('카드 모으기 애니메이션 완료');
      resolve(); // 생성 완료
    }, 1000)
    setTimeList.value.push(time)
  });
}

// 코인 배팅
async function bettingCoin () {
}

// 코인 이동 (승자에게)
async function joinCoin(){
  // console.log("승자에게 코인 이동")
  gameStore.gameMemberInfos.forEach((info, index) => {
    const totalCoinId = document.getElementById('total-coin')
    // 승자 
    if (info.nickname === gameStore.winnerNickname) {
      totalCoinId.classList.add(`coin-devide-move${index+1}`)
      totalCoinId.classList.add('fade-out')
    }
  })
  return new Promise(resolve => {
    const time = setTimeout(() => {
      // console.log('승자에게 코인 이동 완료');
      resolve(); // 생성 완료
      
    }, 1000)
    setTimeList.value.push(time)
  })
}

  // 애니메이션 제거
  onBeforeUnmount(() => {
    // setTimeList.value.forEach(time => {
    //   clearTimeout(time)
    // })
    gameStore.isAnimationRunning = false
  })

  
  // onMounted(() => {
  // })

</script>

<style lang="scss" scoped>
.container {
    display: flex;
    justify-content: center; /* 수평 가운데 정렬 */
    align-items: center; /* 수직 가운데 정렬 */
}

#card {
  width: 100px;
}

@keyframes glowing {
  0% {
    border-color: #FF5733; /* 불꽃 효과의 시작 색상 */
    box-shadow: 0 0 10px #FF5733; /* 불꽃 효과의 시작 그림자 */
  }
  50% {
    border-color: #FFC300; /* 불꽃 효과의 중간 색상 */
    box-shadow: 0 0 20px #FFC300; /* 불꽃 효과의 중간 그림자 */
  }
  100% {
    border-color: #FF5733; /* 불꽃 효과의 끝 색상 */
    box-shadow: 0 0 10px #FF5733; /* 불꽃 효과의 끝 그림자 */
  }
}

.card-winner {
   animation: glowing 2s infinite alternate !important; /* 애니메이션 적용: 2초 간격으로 불꽃이 번갈아가며 반복됨 */

}

.winner {
  background-color: white;
  color: blue;
  font-weight: bold;
  font-size: 30px;
  border: 2px solid black;
  border-radius: 10px;
  z-index: 10001;
}

.loser {
  background-color: white;
  color: red;
  font-weight: bold;
  font-size: 30px;
  border: 2px solid black;
  border-radius: 10px;
  z-index: 10001;
}

.end-round {
  width: 100px;
  z-index: 1999;
  position: absolute
}
</style>