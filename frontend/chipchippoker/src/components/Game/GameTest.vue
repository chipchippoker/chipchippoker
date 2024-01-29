<template>
  <div style="width:500px; color: white" class="d-flex flex-column" >
    afkbhaf
    <h1>게임 테스트</h1>
    <input type="text" v-model="gameRoomTitle" placeholder="방 제목">

    <!-- 인원 수 선택 체크박스 -->
    <label>
      <input type="radio" v-model="countOfPeople" value=2> 2인
    </label>
    <label>
      <input type="radio" v-model="countOfPeople" value=3> 3인
    </label>
    <label>
      <input type="radio" v-model="countOfPeople" value=4> 4인
    </label>

    <button @click="onCreatRoom">방 생성</button>
    <button @click="enterRoom(gameRoomTitle)">방 입장</button>
    <p v-if="errorText" class="error-message">{{ errorText }}</p>
    <button @click="enterRoom(gameRoomTitle)">방 입장</button>
    <input v-model="nickname" type="text" name="" id="" placeholder="강퇴할사람닉네임">
    <button @click="gameStore.kickUser(gameRoomTitle,nickname)">강퇴</button>
    <button @click="sendReady">게임 준비</button>
    <button @click="sendStartGame">게임 시작</button>

    <div>
      <input v-model="bettingCoin" type="number" name="" id="">
      <button @click="bet">배팅하기</button>
      <button @click="die">폴드하기</button>
    </div>

    <div>
      <h1>receive</h1>
      <div><h4>코드</h4>{{ gameStore.receiveMessage?.code }} </div>
      <div><h4>메세지</h4>{{ gameStore.receiveMessage?.message }} </div>
      <div><h4>데이터</h4>{{ gameStore.receiveMessage?.data }}</div>  
      <div><h4>턴</h4>{{ gameStore.receiveMessage?.data?.yourTurn }}</div>  
    </div>


  </div>
</template>

<script setup>
  import { ref } from 'vue'
  import { useGameStore } from '@/stores/game'
  import { useUserStore } from '@/stores/user'

  const gameStore = useGameStore()
  const gameRoomTitle = ref('')
  const countOfPeople = ref(undefined)
  const errorText = ref('')
  const bettingCoin = ref(0)
  // 방생성 버튼 클릭시 호출되는 함수
  const onCreatRoom = function(){
    if (!validateConditions()) {
      return
    }
    console.log('방 생성 요청');
    // 게임방 생성
    gameStore.subscribeHandler(gameRoomTitle.value)
    gameStore.sendCreateRoom(gameRoomTitle.value, countOfPeople.value)
  }

  // 방 제목 유효성 검사
  const validateConditions = function(){
    // 방 제목 길이 확인
    if (gameRoomTitle.value.length < 1 || gameRoomTitle.value.length > 20) {
      errorText.value = '방 제목은 1~20자 이어야 합니다.'
      return false
    }

    // 인원 수 선택 확인
    if (countOfPeople === undefined) {
      errorText.value = '인원 수를 선택하세요.'
      return false
    }

    // 모든 조건을 만족하면 true 반환
    errorText.value = ''
    return true
  }

  const enterRoom = function(gameRoomTitle){
  gameStore.subscribeHandler(gameRoomTitle)
  gameStore.sendJoinRoom(gameRoomTitle)
}
// 준비
const sendReady = function(){
  console.log("Ready!!!!!1")
  gameStore.sendReady(gameRoomTitle.value)
}
// 시작
const sendStartGame = function(){
  console.log("StartGame!!!!!!!!!!!1")
  gameStore.sendStartGame(gameRoomTitle.value)
}

// 베팅
const bet = function(){
  gameStore.bet(gameRoomTitle.value, "BET", bettingCoin.value)
}
const die = function(){
  gameStore.bet(gameRoomTitle.value, "DIE", bettingCoin.value)
}
</script>

<style scoped>

</style>