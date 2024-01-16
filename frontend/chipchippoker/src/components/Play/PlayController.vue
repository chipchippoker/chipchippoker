<template>
  <div class="bg-lightblue rounded-4 w-100 h-100 d-flex fs-5">
    <!-- 배팅 할 코인 -->
    <div class="w-75 d-flex flex-column justify-content-evenly align-items-center">
      <div class="d-flex align-items-center">
        <font-awesome-icon :icon="['fas', 'minus']" />
        <div class="bg-white d-flex justify-content-center align-items-center mx-4" style="width: 40px; height: 40px">4</div>
        <font-awesome-icon :icon="['fas', 'plus']" />
      </div>
      <div class="d-flex justify-content-evenly w-100">
        <div class="rounded-circle bg-modal-yello d-flex justify-content-center align-items-center" style="width: 40px; height: 40px">1</div>
        <div class="rounded-circle bg-lightyellow d-flex justify-content-center align-items-center" style="width: 40px; height: 40px">3</div>
        <div class="rounded-circle bg-yellow d-flex justify-content-center align-items-center" style="width: 40px; height: 40px">5</div>
        <div class="rounded-circle bg-weightyellow d-flex justify-content-center align-items-center" style="width: 40px; height: 40px">ALL</div>
      </div>
    </div>
    <!-- 배팅 다이 -->
    <div class="d-flex flex-column justify-content-evenly align-items-center w-25">
      <div class="bg-darkblue text-white fw-bold rounded-pill d-flex justify-content-center align-items-center border border-2 border-white" style="width: 60px; height: 40px">배팅</div>
      <div class="bg-danger bg-opacity-75 text-white fw-bold rounded-pill d-flex justify-content-center align-items-center border border-2 border-white" style="width: 60px; height: 40px">다이</div>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped>

</style>