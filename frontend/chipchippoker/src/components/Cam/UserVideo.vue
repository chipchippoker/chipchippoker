<template>
  <div v-if="streamManager">
    <OvVideo :stream-manager="streamManager"/>
    <!-- {{ clientData }}이게 내 현재 이름임 -->
    <div class="member-id"><p>{{ clientData }}</p></div>
  </div>
</template>

<script>
  export default {
    name: 'UserVideo',
  }
</script>

<script setup>
  // import { ref, onMounted, computed } from 'vue';
  import { computed } from 'vue';
  import OvVideo from '@/components/Cam/OvVideo.vue';

  const props = defineProps({
    streamManager: Object,
  })

  // clientData는 computed로 진행됨
  const clientData = computed(() => {
    const { clientData } = getConnectionData();
    return clientData;
  });

  function getConnectionData() {
    const { connection } = props.streamManager.stream;
    return JSON.parse(connection.data);
  }
</script>

<style scoped>
.member-id {
  color: white;
}

.box-cam {
  width: 90%;
  height: 300px;
}
</style>