<template>
    <div class="bg-light p-3 rounded-2 m-2">
        <div class="d-flex justify-content-between">
            <div v-if="isprivate">비공개</div>
            <div v-else>공개</div>
            <div class="d-flex">
                <div class="btn-lightred">입장</div>
                <div class="btn-gray">관전</div>
            </div>
        </div>
        
        <div>
            <h3><strong>{{ name }}</strong></h3>
        </div>
        <div>
            참여 인원 : {{  }}/4 관전 인원 : {{  }}/4
        </div>
    </div>
</template>

<script setup>
import { ref } from "vue";
const isprivate = ref(false)
const name = ref('방제')
</script>

<style lang="scss" scoped>

</style>