<template>
    <div class="player-list d-flex container w-100 p-0">
        <div class="row w-100 h-100">
            <div 
            v-for="player in array.slice(0, 2)" :key="player.id"
            class="col-6 mb-3"
            >
                <WaitPlayerItem :player="player"/>
            </div>
            <div 
            v-for="player in array.slice(2, 4)" :key="player.id"
            class="col-6"
            >
                <WaitPlayerItem  :player="player"/>
            </div>
        </div>
    </div>
</template>

<script setup>
import WaitPlayerItem from '@/components/Wait/WaitPlayerItem.vue'
const array = [1, 2, 3, 4]
</script>

<style scoped>
.player-list {
}
</style>