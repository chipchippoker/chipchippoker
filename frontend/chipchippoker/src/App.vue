<script setup>
import ModalGuide from './components/Modal/ModalGuide.vue';
import ModalNotificationList from './components/Modal/ModalNotificationList.vue';
import ModalMainSettings from './components/Modal/ModalMainSettings.vue';
import { RouterLink, RouterView } from 'vue-router'
import { useRoute, useRouter } from 'vue-router';
import { ref, computed, onMounted } from 'vue';
import { useUserStore } from '@/stores/user'
import { useFriendStore } from './stores/friend';
import { useGameStore } from './stores/game';
import { useSoundStore } from './stores/sound';

// window.addEventListener("unload", (event) => {
//     localStorage.removeItem('game')
//     localStorage.removeItem('room')
//     localStorage.removeItem('openvidu')
//   })
  
const soundStore = useSoundStore()
const userStore = useUserStore()
const friendStore = useFriendStore()
const gameStore = useGameStore()
const route = useRoute()
const router = useRouter()

const showIcon = computed(() => {
  return !['wait', 'play'].includes(route.name);
});

const goProfile = function(){
  userStore.profileNickname = userStore.myNickname
  if (route.fullPath === '/profile') {
    router.go(0)
  }
  router.push({name:'profile'})
}

const windowWidth = window.innerWidth;
const windowHeight = window.innerHeight;
function getRandomValue(max) {
  return Math.floor(Math.random() * max);
}

const stars = ref([])

for (let i = 0; i < 100; i++) {
  const x = getRandomValue(windowWidth);
  const y = getRandomValue(windowHeight);

  const style = ['style1', 'style2', 'style3'];
  const opacity = ['opacity1', 'opacity1', 'opacity1', 'opacity2', 'opacity3'];
  const twinkle = [
    'twinkle1',
    'twinkle1',
    'twinkle1',
    'twinkle2',
    'twinkle2',
    'twinkle3',
    'twinkle4'
  ];

  const _s = getRandomValue(3);
  const _o = getRandomValue(5);
  const _t = getRandomValue(7);

  const className = 'star ' + style[_s] + ' ' + opacity[_o] + ' ' + twinkle[_t];

  stars.value.push({ x, y, className });
}

  const checkAlarm = function(){
    friendStore.RequestAlarm()
    gameStore.isAlarmArrive = false
  }


  const soundCallback = async function () {
    await soundStore.hoverSound()
  }

  onMounted(() => {
   
  })

</script>

<template>
  <div class="maple bg-gradation-blue" @click="soundCallback()">
    <div class="position-relative container p-0 w-100" >   
      <!-- 별들 -->
      <!-- <div v-for="star in stars" :key="star" :class="star.className" :style="{ top: star.y + 'px', left: star.x + 'px' }"></div> -->

      <!-- 옵션, 로고, 아이콘  -->
      <div v-if="userStore.accessToken !== null" class="d-flex justify-content-between align-items-center position-absolute m-3" style="width: 95%;">

        <div class="d-flex flex-row justify-content-between" style="width: 10%;">
          <!-- 알림 모달 아이콘 -->
          <div class="mx-3 z-3 btn-transparency position-relative">
            <font-awesome-icon type="button" @click="checkAlarm()"  icon="bell" :class="{'fa-shake':gameStore.isAlarmArrive==true}"  size="lg" data-bs-toggle="modal" data-bs-target="#alarmModal" style="color: #ffffff;" />
            <div v-if="gameStore.isAlarmArrive" class="alarm-on"></div>
          </div>
          <!-- 설정 모달 아이콘 -->
          <div class="mx-3 z-3 btn-transparency">
            <font-awesome-icon type="button" icon="gear" size="lg" data-bs-toggle="modal" data-bs-target="#settingModal" style="color: #ffffff;" />
          </div>
          <!-- 가이드북 모달 아이콘 -->
          <div class="ms-3 z-3 btn-transparency">
            <font-awesome-icon type="button" icon="book" size="lg" data-bs-toggle="modal" data-bs-target="#guideModal" style="color: #ffffff;" />
          </div>
        </div>
        <!-- 유저 아이콘 -->
        <img v-if="showIcon" type="button" class="z-3" @click="goProfile()" :src='userStore.getIconUrl(userStore.myIcon)' style="width: 60px; height: 60px; border-radius: 50%;">
      </div>

    
      <div id="app" class="d-flex justify-content-center ">
        <RouterView />
      </div>

      <!-- 알림 Modal -->
      <div data-bs-backdrop="static" class="modal fade" id="alarmModal" tabindex="-1" aria-labelledby="alarmModalLabel" aria-hidden="true">
        <ModalNotificationList/>
        
      </div>
      <!-- 세팅 Modal -->
      <div data-bs-backdrop="static" class="modal fade" id="settingModal" tabindex="-1" aria-labelledby="settingModalLabel" aria-hidden="true">
        <ModalMainSettings/>
      </div>
      <!-- 가이드북 Modal -->
      <div data-bs-backdrop="static" class="modal fade " id="guideModal" tabindex="-1" aria-labelledby="guideModalLabel" aria-hidden="true">
        <ModalGuide/>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
@import "@/assets/css/color.css";
@import "@/assets/css/size.css";
@import "@/assets/css/animation.css";
@import "@/assets/css/button.scss";

html, body {
  margin: 0;
  padding: 0;
  overflow: hidden; /* 스크롤 없애기 */
}

#app {
  height: 100vh; /* 화면 높이 100%로 설정 */
}

@font-face {
  font-family : 'maple';
  src : url('@/assets/fonts/MaplestoryLight.ttf')  format('truetype');
}
.maple {
  font-family: 'maple'
}

.alarm-on {
  position: absolute;
  top: 0;
  right: 0;
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background-color: red;
}

/* 별 만들기 */
.star {
  border-radius: 50%;
  background-color: white;
  position: absolute;
  overflow: hidden;
  animation-direction: alternate;
  animation-iteration-count: infinite;
}

.style1 {
  width: 1px;
  height: 1px;
}

.style2 {
  width: 2px;
  height: 2px;
}

.style3 {
  width: 3px;
  height: 3px;
}

.opacity1 {
  opacity: 1;
}

.opacity2 {
  opacity: 0.5;
}

.opacity3 {
  opacity: 0.1;
}

.twinkle1 {
  animation-duration: 0.5s;
  animation-name: twinkling;
}

.twinkle2 {
  animation-duration: 1s;
  animation-name: twinkling;
}

.twinkle3 {
  animation-duration: 1.5s;
  animation-name: twinkling;
}

.twinkle4 {
  animation-duration: 2s;
  animation-name: twinklingWithNoBoxShadow;
}

@keyframes twinkling {
  0% {
    box-shadow: 0 0 10px 0px rgba(255, 255, 255, 0.1);
  }

  50% {
    box-shadow: 0 0 10px 2px rgba(255, 255, 255, 0.4);
  }

  100% {
    box-shadow: 0 0 10px 0px rgba(255, 255, 255, 0.1);
  }
}

@keyframes twinklingWithNoBoxShadow {
  0% {
    background-color: #ffffff;
    box-shadow: 0 0 10px 0px rgba(255, 255, 255, 1);
  }
  20% {
    background-color: #ffc4c4;
    box-shadow: 0 0 10px 0px rgba(255, 196, 196, 1);
  }
  80% {
    background-color: #c4cfff;
    box-shadow: 0 0 10px 0px rgba(196, 207, 255, 1);
  }
  100% {
    background-color: #ffffff;
    box-shadow: 0 0 10px 0px rgba(255, 255, 255, 0.2);
  }
}

// 마우스 커서 커스터마이징

</style>