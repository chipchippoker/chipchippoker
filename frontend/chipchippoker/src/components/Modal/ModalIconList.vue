<template>
    <div class="text-center bg-modal-yellow">
        <div class="d-flex flex-wrap">
            <div v-for="number in numbers" :key="number" class="text-white grid-item d-flex justify-content-center">
                
                <ModalIconItemVue
                @click="selectIcon(number)"
                :number = number
                />

                
            </div>
        </div>
    </div>
</template>

<script setup>
import ModalIconItemVue from "./ModalIconItem.vue"
import { useUserStore } from '@/stores/user';
import { ref } from "vue";

const userStore = useUserStore()

const numbers =  ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20']
const selectIcon = function(number){
    userStore.myIcon = number
}

</script>

<style scoped>
@import '@/assets/color.css';
@import '@/assets/size.css';
.grid-item {
  display: inline-block;
  width: 20%; /* 4열로 정렬하기 위해 25%로 설정 */
  box-sizing: border-box;
}

</style>