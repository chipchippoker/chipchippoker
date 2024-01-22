<template>
    <div>
      <img class="small-icon m-3" :class="{'selected':number === userStore.myIcon }" :src='userStore.getIconUrl(number)' :alt="number">
      
    </div>
  </template>
  
  <script setup>
  import { useUserStore } from '@/stores/user';
  const props = defineProps({
    number: String
  });
  const userStore = useUserStore()

  </script>
  
  <style scoped>

  .selected {
    border: 6px solid greenyellow;
    }

  </style>
  