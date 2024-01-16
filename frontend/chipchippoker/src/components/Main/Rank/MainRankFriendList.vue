<template>
    <div>
        <MainRankItemItem v-for="item in items" :key="item.id"
        :item="item"/>
            
    </div>
</template>

<script setup>
import MainRankItemItem from './MainRankItem.vue';
let id = 1
// 예시 probs 데이터
const items = [
    {'name':'ㅁㄶ,ㅓㅠ',
    'number':id++,
    'point':1234
    },
    {'name':'ㅈㄷㅁ룧ㄹㄷ',
    'number':id++,
    'point':1212
    },
    {'name':'ㅁㄷ러ㅗㅠ',
    'number':id++,
    'point':123
    },
    {'name':'ㅂㅈ도',
    'number':id++,
    'point':123
    },
    {'name':'ㅁㄴ류ㅓㅏㄹㄴㅁ',
    'number':id++,
    'point':12
    },]
</script>

<style lang="scss" scoped>

</style>