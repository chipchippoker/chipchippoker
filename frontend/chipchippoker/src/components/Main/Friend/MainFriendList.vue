<template>
    <div>
        <h3>친구목록</h3>
        <MainFriendItem v-for="item in items" :key="item.name"
        :item="item"/>
    </div>
</template>

<script setup>
import MainFriendItem from './MainFriendItem.vue';
let id = 1
// 예시 probs 데이터
const items = [
    {'name':'윤예빈',
    'number':id++,
    'point':12387,
    'isOnline':true
    },
    {'name':'ㄴㅁ이로',
    'number':id++,
    'point':2376,
    'isOnline':false
    },
    {'name':'ㅁㄶ,ㅓㅠ',
    'number':id++,
    'point':1234,
    'isOnline':true
    },
    {'name':'ㅈㄷㅁ룧ㄹㄷ',
    'number':id++,
    'point':1212,
    'isOnline':false
    },
    {'name':'ㅁㄷ러ㅗㅠ',
    'number':id++,
    'point':123,
    'isOnline':true
    },
    {'name':'ㅂㅈ도',
    'number':id++,
    'point':123,
    'isOnline':false
    },
    {'name':'ㅁㄴ류ㅓㅏㄹㄴㅁ',
    'number':id++,
    'point':12,
    'isOnline':true
    },
]
</script>

<style lang="scss" scoped>

</style>