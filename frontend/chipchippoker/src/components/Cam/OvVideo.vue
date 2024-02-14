<template>
  <video class="cam" ref="videoEl" autoplay :style="videoStyle" />
</template>

<script>

export default {
  name: 'OvVideo',
}
</script>

<script setup>
import * as faceAPI from 'face-api.js'
import { ref, onMounted, onUnmounted } from "vue";
import { useRoute } from 'vue-router';
import { useGameStore } from '@/stores/game';

const gameStore = useGameStore()
const route = useRoute()
const props = defineProps({
  streamManager: Object,
});

const videoEl = ref(null);

const videoStyle = ref({ width: "400px", height: "300px" });
const emit = defineEmits(['sendEmotion'])

if (route.name === 'play') {
  videoStyle.value = { width: "280px", height: "210px" }
}

const startEmotionDetection = setInterval(async () => {
  try {

  const detections = await faceAPI.detectSingleFace(videoEl.value,
    new faceAPI.TinyFaceDetectorOptions())
    .withFaceLandmarks().withFaceExpressions()
    // 인식 될때만 올리기
  if (detections){
    const emotion = {
      'angry': detections.expressions.angry * 100,
      'disgusted': detections.expressions.disgusted * 100,
      'fearful': detections.expressions.fearful * 100,
      'happy': detections.expressions.happy,
      'neutral': detections.expressions.neutral,
      'sad': detections.expressions.sad,
      'surprised': detections.expressions.surprised * 100
    }
    // console.log(emotion)
    // 가장 큰 감정 추출
    const maxEmotion = Object.keys(emotion).reduce((maxEmotion, key) => {
      return emotion[key] > emotion[maxEmotion] ? key : maxEmotion;
    }, Object.keys(emotion)[0]);
    emit('sendEmotion', maxEmotion,emotion)
  }
}
catch{
  // console.log('감정인식 안됨')
}
}, 1000)


// mouted되면 videoEl의 값을 addVideoElement에 추가함.
onMounted(async () => {
  props.streamManager.addVideoElement(videoEl.value);
  const models = await Promise.all([
    faceAPI.nets.tinyFaceDetector.loadFromUri('/models'),
    faceAPI.nets.faceExpressionNet.loadFromUri('/models'),
    faceAPI.nets.faceLandmark68Net.loadFromUri('/models'),
  ])

  // 모델 로딩 후 처리
  // console.log('Models loaded:', models)

  startEmotionDetection()
})

// 나갈때 중지
onUnmounted(() => {
  clearInterval(startEmotionDetection);
});

</script>

<style scoped>
.cam {
  border-radius: 30px;
}
</style>