import { ref, watch } from 'vue'
import { defineStore } from 'pinia'
import bgm from '@/assets/bgm/bgm.mp3'
import alarm from '@/assets/bgm/alarm.mp3'
import cardshuffle from '@/assets/bgm/cardshuffle.mp3'
import chipsound from '@/assets/bgm/chipsound.mp3'
import hover from '@/assets/bgm/hover.mp3'
import lose from '@/assets/bgm/lose.mp3'
import win from '@/assets/bgm/win.mp3'

export const useSoundStore = defineStore('sound', () => {
    // 설정 음향 사운드
    const bgmSoundRange = ref(100)
    const effectSoundRange = ref(100)
    const isBgmPlay = ref(false)

    //   음향 객체
    const bgmAudio = new Audio(bgm)
    
    // 음향 실시간 적용
    watch([bgmSoundRange,effectSoundRange], () => {
      bgmAudio.volume = bgmSoundRange.value/100
    })

    const bgmOn = async function(){
      // bgm.pause()
      isBgmPlay.value = true
      await bgmAudio.play()
    }
    const bgmOff = async function(){
      bgmAudio.pause()
      isBgmPlay.value = false
    }
    
    // 음향 플레이 함수
    const alarmAudio = new Audio(alarm);
    const alarmSound = async function(){
      alarmAudio.volume = effectSoundRange.value/100
      alarmAudio.currentTime = 0;
      await alarmAudio.play()
    }

    // 카드 섞기
    const cardshuffleAudio = new Audio(cardshuffle);
    const cardshuffleSound = async function(){
      cardshuffleAudio.currentTime = 0;
      cardshuffleAudio.volume = effectSoundRange.value/100
      await cardshuffleAudio.play()
    }
    // 코인 사운드
    const chipsoundAudio = new Audio(chipsound);
    const chipsoundSound = async function(){
      chipsoundAudio.currentTime = 0 
      chipsoundAudio.volume = effectSoundRange.value/100
      await chipsoundAudio.play()
    }
    
    // 패배 사운드
    const loseAudio = new Audio(lose);
    const loseSound = async function(){
      loseAudio.currentTime = 0
      loseAudio.volume = effectSoundRange.value/100
      await loseAudio.play()
    }
    
    // 승리 사운드
    const winAudio = new Audio(win);
    const winSound = async function(){
      winAudio.currentTime = 0;
      winAudio.volume = effectSoundRange.value/100
      await winAudio.play()
    }
    
    // 호버 사운드
    const hoverAudio = new Audio(hover);
    const hoverSound = async function(){
      hoverAudio.currentTime = 0;
      hoverAudio.volume = effectSoundRange.value/100
      await hoverAudio.play()
    }

  return {
    bgmSoundRange,effectSoundRange, isBgmPlay,
    bgmOn, bgmOff, 
    alarmSound, cardshuffleSound ,chipsoundSound ,loseSound ,winSound, hoverSound
  }
},{persist:true})
