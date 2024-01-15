<template>
    <div>
        <h3 class="text-white">전체랭크리스트</h3>
        <MainRankAllItem v-for="item in items" :key="item.id"/>
            
    </div>
</template>

<script setup>
import MainRankAllItem from './MainRankAllItem.vue';
const items = [1,2,3]
</script>

<style lang="scss" scoped>

</style>