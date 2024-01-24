import { ref, watch } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import { faL } from '@fortawesome/free-solid-svg-icons'

export const useSoundStore = defineStore('sound', () => {
    // 설정 음향 사운드
    const bgmSoundRange = ref(100)
    const effectSoundRange = ref(100)
    const isBgmPlay = ref(false)

    //   음향 객체
    var bgm = new Audio('src/assets/bgm/bgm.mp3');
    var alarm = new Audio('src/assets/bgm/alarm.mp3');
    var cardshuffle = new Audio('src/assets/bgm/cardshuffle.mp3');
    var chipsound = new Audio('src/assets/bgm/chipsound.mp3');
    var lose = new Audio('src/assets/bgm/lose.mp3');
    var win = new Audio('src/assets/bgm/win.mp3');
    var hover = new Audio('src/assets/bgm/hover.mp3');


    // 음향 실시간 적용
    watch([bgmSoundRange,effectSoundRange], () => {
    bgm.volume = bgmSoundRange.value/100
    alarm.volume = effectSoundRange.value/100
    cardshuffle.volume = effectSoundRange.value/100
    chipsound.volume = effectSoundRange.value/100
    lose.volume = effectSoundRange.value/100
    win.volume = effectSoundRange.value/100
    hover.volume = effectSoundRange.value/100
    });


    const bgmOn = function(){
      bgm.pause()
      bgm.play()
      isBgmPlay.value = true
    }
    const bgmOff = function(){
    bgm.pause()
    isBgmPlay.value = false

    }
    // 음향 플레이 함수
    const alarmSound = function(){

    alarm.play()
    }
    const cardshuffleSound = function(){
    cardshuffle.play()
    }
    const chipsoundSound = function(){
    chipsound.play()
    }
    const loseSound = function(){
    lose.play()
    }
    const winSound = function(){
    win.play()
    }
    const hoverSound = function(){
    hover.play()
    }




  return {
    bgmSoundRange,effectSoundRange, isBgmPlay,
    bgmOn, bgmOff, 
    alarmSound, cardshuffleSound ,chipsoundSound ,loseSound ,winSound, hoverSound
  }
},{persist:true})
