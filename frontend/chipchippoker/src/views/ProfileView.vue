<template>
  <div class="container m-0">
    <!-- 옵션, 로고, 아이콘 -->
    <div class="d-flex justify-content-between">
      <div class="">
        <div class="">
          <font-awesome-icon icon="bell" shake size="lg" style="color: #ffffff;" />
        </div>
        <div class="">
          <font-awesome-icon icon="gear" style="color: #ffffff;" />
        </div>
        <div class="">
          <font-awesome-icon icon="book" style="color: #ffffff;" />
        </div>
      </div>
      <div class=""><img class="small-logo" src="/src/assets/Logo.png" alt=""></div>
      <div class="">
        <font-awesome-icon icon="user" style="color: #ffffff;" />
      </div>
    </div>
    <h1>프로필페이지</h1>
    <div>
      <ProfileMyinfo />
    </div>
  </div>
</template>

<script setup>
import ProfileMyinfo from '@/views/ProfileMyInfo.vue'
</script>

<style scoped>
  @import "@/assets/color.css";
  @import "@/assets/size.css";
</style>