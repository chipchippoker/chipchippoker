<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="bg-gradation-blue">
  <h1 class="text-3xl font-bold underline">
    Hello world!
  </h1>

  <RouterView />
</div>
</template>

<style scoped>
 @import '@/assets/color.css';
</style>