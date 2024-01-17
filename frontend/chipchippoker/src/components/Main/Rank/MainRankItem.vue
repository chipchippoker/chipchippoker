<template>
    <!-- 각각의 사용자 -->
    <div class="d-flex  my-2 bg-light rounded-2 p-1 overflow-hidden">
        <div class="d-flex gap-1 align-items-center">
            
            <div style="width:30px;">{{ item?.number }}등</div>
            <img class="x-small-icon"  :src='userStore.getIconUrl(1)'>
        
            <div style="width:100px; height:20px;">{{ item?.name }}</div>
            <div style="width:100px; height:20px;">{{ item?.point }} pt</div>
                
            
        </div>
    </div>

    
</template>

<script setup>
import { useUserStore } from '@/stores/user';
const userStore = useUserStore()
defineProps({item:Object})
</script>

<style scoped>

</style>