<template>
  <video class="cam" ref="videoEl" autoplay @click="clickVideo" :style="videoStyle"/>
</template>

<script>

export default {
  name: 'OvVideo',
}
</script>

<script setup>
import * as faceAPI from 'face-api.js'
import { ref, onMounted, watch } from "vue";
import { useRoute } from 'vue-router';
import { useGameStore } from '@/stores/game';

const gameStore = useGameStore()
const route = useRoute()
const props = defineProps({
  streamManager: Object,
});

const videoEl = ref(null);

const videoStyle = ref({ width: "400px", height: "300px" });

if (route.name === 'play') {
  videoStyle.value = { width: "280px", height: "210px" }
}

// mouted되면 videoEl의 값을 addVideoElement에 추가함.
onMounted(() => {
  props.streamManager.addVideoElement(videoEl.value);
})

const emit = defineEmits(['sendEmotion'])
const clickVideo = function() {
  Promise.all([
    faceAPI.nets.tinyFaceDetector.loadFromUri('/models'),
    faceAPI.nets.faceExpressionNet.loadFromUri('/models'),
    faceAPI.nets.faceLandmark68Net.loadFromUri('/models'),
    ]).then(
    setInterval(async () => {
      const detections = await faceAPI.detectSingleFace(videoEl.value,
        new faceAPI.TinyFaceDetectorOptions())
        .withFaceLandmarks().withFaceExpressions()
      
      const emotion = {
        'angry':detections.expressions.angry*100,
        'disgusted':detections.expressions.disgusted*100,
        'fearful':detections.expressions.fearful*100,
        'happy':detections.expressions.happy,
        'neutral':detections.expressions.neutral,
        'sad':detections.expressions.sad,
        'surprised':detections.expressions.surprised*100
      }
      // console.log(emotion)
      // 가장 큰 감정 추출
      const maxEmotion = Object.keys(emotion).reduce((maxEmotion, key) => {
        return emotion[key] > emotion[maxEmotion] ? key : maxEmotion;
      }, Object.keys(emotion)[0]);
      emit('sendEmotion',maxEmotion)
    }, 1000))
}


</script>

<style scoped>
.cam {
  border-radius: 30px;
}

</style>