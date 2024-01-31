<template>
    <div class="text-white">
        <!-- 카드 뒤집기 -->
        <p >카드 클릭시 뒤집기 효과</p>
      <div class="flip-card" @click="flip">
        <div class="flip-card-inner">
            <div class="flip-card-front">
                <img :src="getCardUrl(1, 2)" alt="dog">
            </div>
            <div class="flip-card-back">
                <img :src="getBackCardUrl()" alt="카드">
            </div>
        </div>
    </div>

        
        <div>
            <p >input에 따른 코인 추가</p>
            <input v-model="coinNum" type="number" name="" id="">
        </div>


        <div class="coin-list flex-wrap" style="width: 160px;">
            
            <img style="width: 50px;" v-for="index in 10" :key="index" class="coin-overlap" :src="getCoinUrl(1)" alt="">

            
            <img style="width: 50px;" class="coin-overlap opacity-75" v-for="index in coinNum" :key="index"
                :src="getCoinUrl(1)" alt="">
        </div>


        <!-- <div class="position-relative">
            <div @click="move">
                <img class="position-absolute card-back1 move1" :src="getBackCardUrl()" alt="카드">
                <img class="position-absolute card-back2 move2" :src="getBackCardUrl()" alt="카드">
                <img class="position-absolute card-back3 move3" :src="getBackCardUrl()" alt="카드">
                <img class="position-absolute card-back4 move4" :src="getBackCardUrl()" alt="카드">
            </div>
        </div> -->

        <p>코인 모으기</p>
        <div @click="joinCoin" class="position-relative" style="width: 200px; height: 200px;">
            <div id="coin1" class="coin-list coin-unmove1 flex-wrap position-absolute" style="width: 160px; ">
                <div v-for="index in 10" :key="index">
                    <img  style="width: 50px;"  class="coin-overlap" :src="getCoinUrl(1)" alt="">
                </div>
            </div>
            <div id="coin2" class="coin-list coin-unmove2 flex-wrap position-absolute" style="width: 160px;;">
                <div v-for="index in 10" :key="index">
                    <img  style="width: 50px;"  class="coin-overlap" :src="getCoinUrl(2)" alt="">
                </div>
            </div>
            <div id="coin3" class="coin-list coin-unmove3 flex-wrap position-absolute" style="width: 160px;;">
                <div v-for="index in 10" :key="index">
                    <img  style="width: 50px;"  class="coin-overlap" :src="getCoinUrl(3)" alt="">
                </div>
            </div>
            <div id="coin4" class="coin-list coin-unmove4 flex-wrap position-absolute" style="width: 160px;;">
                <div v-for="index in 10" :key="index">
                    <img  style="width: 50px;" class="coin-overlap" :src="getCoinUrl(4)" alt="">
                </div>
            </div>
        </div>


        <P >부르르 진동</P>
        <div id="box" class="box" @click="vibration()">Click</div>

        
        <P >코인 촤라락</P>
        <div @click="coinSlide()">
            <img style="width: 50px;" v-for="index in 10" :key="index" :id="`coinslide${index}`" class="coin-overlap" :src="getCoinUrl(1)" alt="">
        </div>
        
    </div>  
</template>
  
<script setup>
import { ref } from 'vue';

//  카드 뒤집기 
const flip = () => {
    const flipCard = document.querySelector('.flip-card');
    flipCard.classList.toggle('flipped');
};

// 앞장 카드
const getCardUrl = function (setnum, cardnum) {
    return new URL(`/src/assets/cards/set${setnum}/card${cardnum}.png`, import.meta.url).href;
};
//   뒷장 카드
const getBackCardUrl = function () {
    return new URL(`/src/assets/cards/back.png`, import.meta.url).href;
};

//   코인
const coinNum = ref(0)
const getCoinUrl = function (num) {
    return new URL(`/src/assets/coins/coin${num}.png`, import.meta.url).href;
};


//   카드 나눠주기
const move = function () {
    const backCard1 = document.querySelector('.card-back1');
    backCard1.classList.toggle('move1');
    const backCard2 = document.querySelector('.card-back2');
    backCard2.classList.toggle('move2');
    const backCard3 = document.querySelector('.card-back3');
    backCard3.classList.toggle('move3');
    const backCard4 = document.querySelector('.card-back4');
    backCard4.classList.toggle('move4');
}

const joinCoin = function(){
    const coin1 = document.querySelector('#coin1');
    coin1.classList.toggle('coin-move1');
    const coin2 = document.querySelector('#coin2');
    coin2.classList.toggle('coin-move2');
    const coin3 = document.querySelector('#coin3');
    coin3.classList.toggle('coin-move3');
    const coin4 = document.querySelector('#coin4');
    coin4.classList.toggle('coin-move4');
}


// 부르르
const vibration = () => {
    const box = document.querySelector('#box')
    box.classList.add("vibration");

  setTimeout(function() {
    box.classList.remove("vibration");
  }, 400);
}

// 코인 정렬
const coinSlide = function(){
    for (let i = 1; i < 11; i++) {
        setTimeout(()=>{
            const coin  = document.querySelector(`#coinslide${i}`)
            coin.classList.add('coin-slide')
        } ,100 * (i + 1))
    }
}

</script>
  
<style scoped>
@import url("https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css");


.coin-slide{
    transform: translateY(50px);
    transition: 0.2s;
}



.box {
  width: 50px;
  height:50px;
  background: #febf00;
  cursor: pointer;
}
.box.vibration {
  animation: vibration .1s infinite;
}

@keyframes vibration {
  from {
    transform: rotate(10deg);
  }
  to {
    transform: rotate(-10deg);
  }
}


.flip-card {
    width: 160px;
    height: 224px;
    perspective: 1000px;
}

.flip-card-inner {
    position: relative;
    width: 100%;
    height: 100%;
    text-align: center;
    transition: transform 0.6s;
    transform-style: preserve-3d;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
}

.flip-card.flipped .flip-card-inner {
    transform: rotateY(180deg);
}

.flip-card-front,
.flip-card-back {
    position: absolute;
    width: 100%;
    height: 100%;
    -webkit-backface-visibility: hidden;
    backface-visibility: hidden;
}

.flip-card-front {
    color: black;
}

.flip-card-back {
    color: white;
    transform: rotateY(180deg);
}

/* 코인 */
.coin-list {
    display: flex;

}

.coin-overlap {
    margin-left: -25px;
    
}

.move1 {
    transform: translate(200px, 200px);
    transition: 1s;
}

.move2 {
    transform: translate(-200px, 200px);
    transition: 1s;
}

.move3 {
    transform: translate(200px, -200px);
    transition: 1s;
}

.move4 {
    transform: translate(-200px, -200px);
    transition: 1s;
}

/* 코인 */
.coin-unmove1 {
    transform: translate(0, 0);
    transition: 1s;
}

.coin-unmove2 {
    transform: translate(0, 0);
    transition: 1s;
}

.coin-unmove3 {
    transform: translate(0, 0);
    transition: 1s;
}

.coin-unmove4 {
    transform: translate(0, 0);
    transition: 1s;
}
.coin-move1 {
    transform: translate(50%, 50%);
    transition: 1s;
}

.coin-move2 {
    transform: translate(-50%, 50%);
    transition: 1s;
}

.coin-move3 {
    transform: translate(50%, -50%);
    transition: 1s;
}

.coin-move4 {
    transform: translate(-50%, -50%);
    transition: 1s;
}

</style>
  