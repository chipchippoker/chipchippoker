<template>
  <div id="chat-container" class="d-flex flex-column justify-content-between">

    <!-- 1. 채팅 목록 -->
    <div id="chat-window">
      <ul id="messageList" class="m-0 chat_ul" style="list-style-type: none;">
        <li class="my-2" v-for="(message, index) in messages.value" :key="index">
          <strong>{{message.username}}:</strong> {{message.message}}
        </li>
      </ul>
    </div>

    <!-- 2. 입력창 -->
    <div class="my-2" id="chat-input">
      <input class="form-control" type="text" placeholder="입력" v-model="inputMessage" @keyup.enter="sendMessage(inputMessage)">
      <button id="chat-btn" class="btn btn-outline-secondary" @click="sendMessage(inputMessage)">전송</button>
    </div>
  </div>
</template>

<script setup>
  import { ref, computed, onMounted, watch } from 'vue';
  import { useGameStore } from '@/stores/game';
  import { useRoomStore } from '@/stores/room';
  import { useOpenviduStore } from '@/stores/openvidu';

  const gameStore = useGameStore()
  const roomStore = useRoomStore()
  const openviduStore = useOpenviduStore()
  
  /////////////////////채팅창을 위한 부분
  const inputMessage = ref("")
  const messages = ref([])

  const sendMessage = function(input) {
    openviduStore.sendMessage(input)
    inputMessage.value = ''
  }

  // scroll 함수
  function scrollUl() {
    // 채팅창 form 안의 ul 요소, (ul 요소 안에 채팅 내용들이 li 요소로 입력된다.)
    let chatUl = document.querySelector('.chat_ul');
    chatUl.scrollTop = chatUl.scrollHeight; // 스크롤의 위치를 최하단으로
  }

  messages.value = computed(() => openviduStore.messages)

  // messages 배열이 변경될 때마다 scrollUl 함수를 호출하여 스크롤 갱신
  watch([openviduStore.messages], () => {
    window.setTimeout(scrollUl, 50);
  });



</script>

<style scoped>

#chat-container {
 background-color: #8497c7;
 border-radius: 10px;
 height: 150px;
 box-shadow: inset;
}

#chat-window {
  padding: 5px;
  padding-top: 5px;
  height: 80%;
}

#messageList {
  max-height: 110px;
  margin: 10px;
  padding: 10px;
  padding-top: 20px;
  padding-bottom: 0;
  overflow-y: auto;
}

#chat-input {
  position: relative;
  margin-top: 5px;
}

#chat-btn {
  position: absolute;
  top: 0;
  right: 0;
}

</style>