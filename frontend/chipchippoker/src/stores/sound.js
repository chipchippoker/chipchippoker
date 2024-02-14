import { ref, watch } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'

export const useSoundStore = defineStore('sound', () => {
    // 설정 음향 사운드
    const bgmSoundRange = ref(100)
    const effectSoundRange = ref(100)
    const isBgmPlay = ref(false)

    //   음향 객체
    const bgm = new Audio('src/assets/bgm/bgm.mp3')
    
    // 음향 실시간 적용
    watch([bgmSoundRange,effectSoundRange], () => {
    bgm.volume = bgmSoundRange.value/100
    })

    const bgmOn = function(){
      // bgm.pause()
      isBgmPlay.value = true
      bgm.play()
    }
    const bgmOff = function(){
    bgm.pause()
    isBgmPlay.value = false

    }
    
    // 음향 플레이 함수
    const alarm = new Audio('src/assets/bgm/alarm.mp3');
    const alarmSound = function(){
      alarm.volume = effectSoundRange.value/100
      alarm.currentTime = 0;
      alarm.play()
    }

    // 카드 섞기
    const cardshuffle = new Audio('src/assets/bgm/cardshuffle.mp3');
    const cardshuffleSound = function(){
      cardshuffle.currentTime = 0;
      cardshuffle.volume = effectSoundRange.value/100
      cardshuffle.play()
    }
    // 코인 사운드
    const chipsound = new Audio('src/assets/bgm/chipsound.mp3');
    const chipsoundSound = function(){
      chipsound.currentTime = 0 
      chipsound.volume = effectSoundRange.value/100
      chipsound.play()
    }
    
    // 패배 사운드
    const lose = new Audio('src/assets/bgm/lose.mp3');
    const loseSound = function(){
      lose.currentTime = 0
      lose.volume = effectSoundRange.value/100
      lose.play()
    }
    
    // 승리 사운드
    const win = new Audio('src/assets/bgm/win.mp3');
    const winSound = function(){
      win.currentTime = 0;
      win.volume = effectSoundRange.value/100
      win.play()
    }
    
    // 호버 사운드
    const hover = new Audio('src/assets/bgm/hover.mp3');
    const hoverSound = function(){
      // var hover = new Audio('src/assets/bgm/hover.mp3');
      hover.currentTime = 0;
      hover.volume = effectSoundRange.value/100
      hover.play()
    }

  return {
    bgmSoundRange,effectSoundRange, isBgmPlay,
    bgmOn, bgmOff, 
    alarmSound, cardshuffleSound ,chipsoundSound ,loseSound ,winSound, hoverSound
  }
},{persist:true})
