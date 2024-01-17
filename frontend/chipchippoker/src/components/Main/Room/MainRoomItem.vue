<template>
    <div class="bg-light p-2 rounded-2 m-1">
        <div class="d-flex justify-content-between">
            <div v-if="isprivate" class="x-little-text">비공개</div>
            <div v-else class="x-little-text">공개</div>
            <div class="d-flex gap-1">
                <div class="btn-lightred x-little-text rounded-1 px-1">입장</div>
                <div class="btn-gray x-little-text rounded-1 px-1">관전
                    <font-awesome-icon :icon="['fas', 'caret-right']" style="color: #8f8f8f;" />
                </div>
            </div>
        </div>
        
        <div>
            <div><strong>{{ name }}</strong></div>
        </div>
        <div class="x-little-text">
            참여 인원 : {{  }}/4 관전 인원 : {{  }}/4
        </div>
    </div>
</template>

<script setup>
import { ref } from "vue";
const isprivate = ref(false)
const name = ref('방제')
</script>

<style scoped>

</style>