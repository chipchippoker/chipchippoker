<template>
  <div class="bg-lightblue rounded-4 w-100 h-100 d-flex flex-column fs-5">
    <ul id="messageList" class="overflow-y-auto m-0">
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
  }

  messages.value = computed(() => openviduStore.messages)

  const scrollToBottom = () => {
    const messageList = document.getElementById('messageList');
    if (messageList) {
      messageList.scrollTop = messageList.scrollHeight;
    }
  };

  watch(() => openviduStore.messages, () => {
    scrollToBottom();
  });

</script>

<style scoped>

</style>