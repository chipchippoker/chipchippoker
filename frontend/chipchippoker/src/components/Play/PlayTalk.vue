<template>
  <div class="bg-lightblue rounded-4 w-100 h-100 d-flex flex-column justify-content-end fs-5">
    <ul class="overflow-y-auto m-0 chat_ul">
      <li v-for="(message, index) in messages.value" :key="index">
        <strong>{{message.username}}:</strong> {{message.message}}
      </li>
    </ul>
    <div class="input-group">
      <input class="form-control" type="text" placeholder="입력" v-model="inputMessage">
      <button class="btn btn-outline-secondary" @click="sendMessage(inputMessage)">전송</button>
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
    window.setTimeout(scrollUl, 50);
  }

  // scroll 함수
  function scrollUl() {
    // 채팅창 form 안의 ul 요소, (ul 요소 안에 채팅 내용들이 li 요소로 입력된다.)
    let chatUl = document.querySelector('.chat_ul');
    chatUl.scrollTop = chatUl.scrollHeight; // 스크롤의 위치를 최하단으로
  }

  messages.value = computed(() => openviduStore.messages)


</script>

<style scoped>

</style>