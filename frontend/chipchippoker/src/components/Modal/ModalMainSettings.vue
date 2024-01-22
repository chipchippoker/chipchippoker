<template>
    <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered">
        <div class="modal-content" style="background-color: #fff0c0;">
          <div class="modal-header border-0">
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div  class="d-flex flex-column gap-3">
                <div class="d-flex flex-column">
                    <h4 class="fw-bold">음향</h4>
                    <div class="container">
                        <label for="bgmRange" class="form-label">BGM</label>
                        <input v-model="bgmRange" type="range" id="bgmRange" class="form-range">

                        {{ bgmRange }}                            
                    </div>
                    <div class="container">
                        <label for="soundRange" class="form-label">효과음</label>
                        <input v-model="soundRange" type="range" id="soundRange" class="form-range" >
                        {{soundRange}}                        
                    </div>
                </div>
                <div class="d-flex flex-column gap-2">
                    <h4 class="fw-bold">카메라</h4>
                    <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked>
                    <label class="form-check-label" for="flexSwitchCheckChecked">관전 카메라 허용</label>
                    </div>
                </div>
                <div>
                    <h4 class="fw-bold">문의하기
                        <a href="https://docs.google.com/forms/d/e/1FAIpQLSdmmaeY3EmByEDTqNVndKTz2GdtdbLpFgZK2nkJ8rG-IBM3Yg/viewform?usp=sf_link">
                            <font-awesome-icon :icon="['fas', 'envelope-open']" style="color: #ffffff;" />
                        </a>
                    </h4>
                </div>
            </div>

          </div>
        </div>
      </div>
    
</template>

<script setup>
import { ref } from 'vue';
const bgmRange = ref(0)
const soundRange = ref(0)
</script>

<style scoped>


</style>