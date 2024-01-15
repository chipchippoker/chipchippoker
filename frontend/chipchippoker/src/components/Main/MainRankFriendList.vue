<template>
    <div>
        <h3 class="text-white">친구랭킹리스트</h3>
        <MainRankFriendItem v-for="item in items" :key="item.id"/>
            
    </div>
</template>

<script setup>
import MainRankFriendItem from './MainRankFriendItem.vue';
const items = [1,2,3]
</script>

<style lang="scss" scoped>

</style>