<template>
    <div>
        <MainRankItemItem v-for="item in friendStore.friendRankList" :key="item?.nickname"
        :item="item"/>
            
    </div>
</template>

<script setup>
import MainRankItemItem from './MainRankItem.vue';
import { useFriendStore } from '@/stores/friend';
const friendStore = useFriendStore()
</script>

<style lang="scss" scoped>

</style>