<template>
  <video class="cam" ref="videoEl" autoplay />
</template>

<script>
  export default {
    name: 'OvVideo',
  }
</script>

<script setup>
import { ref, onMounted } from "vue";

const props = defineProps({
  streamManager: Object,
});

const videoEl = ref(null);

// mouted되면 videoEl의 값을 addVideoElement에 추가함.
onMounted(() => {
  props.streamManager.addVideoElement(videoEl.value);
});
</script>

<style scoped>
.cam {
    height: 300px;
    background-color: black;
    border-radius: 10px;
}
</style>