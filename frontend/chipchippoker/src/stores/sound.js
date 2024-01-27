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
    var bgm = new Audio('src/assets/bgm/bgm.mp3')
    
    // 음향 실시간 적용
    watch([bgmSoundRange,effectSoundRange], () => {
    bgm.volume = bgmSoundRange.value/100
    })

    const bgmOn = function(){
      // bgm.pause()
      // bgm.play()
      isBgmPlay.value = true
    }
    const bgmOff = function(){
    bgm.pause()
    isBgmPlay.value = false

    }
    // 음향 플레이 함수
    const alarmSound = function(){
      var alarm = new Audio('src/assets/bgm/alarm.mp3');
      alarm.volume = effectSoundRange.value/100
      alarm.play()
    }
    const cardshuffleSound = function(){
      var cardshuffle = new Audio('src/assets/bgm/cardshuffle.mp3');
      cardshuffle.volume = effectSoundRange.value/100
      cardshuffle.play()
    }
    const chipsoundSound = function(){
      var chipsound = new Audio('src/assets/bgm/chipsound.mp3');
      chipsound.volume = effectSoundRange.value/100
      chipsound.play()
    }
    const loseSound = function(){
      var lose = new Audio('src/assets/bgm/lose.mp3');
      lose.volume = effectSoundRange.value/100
      lose.play()
    }
    const winSound = function(){
      var win = new Audio('src/assets/bgm/win.mp3');
      win.volume = effectSoundRange.value/100
      win.play()
    }
    const hoverSound = function(){
      var hover = new Audio('src/assets/bgm/hover.mp3');
      hover.volume = effectSoundRange.value/100
      hover.play()
    }




  return {
    bgmSoundRange,effectSoundRange, isBgmPlay,
    bgmOn, bgmOff, 
    alarmSound, cardshuffleSound ,chipsoundSound ,loseSound ,winSound, hoverSound
  }
},{persist:true})
