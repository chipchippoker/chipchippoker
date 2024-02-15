<template>
    <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered">
        <div class="modal-content p-3" style="background-color: #fff0c0;">
          <!-- 모달 헤드 ->  close 버튼 -->
          <div class="modal-header border-0 p-1">
          </div>
          <!-- 모달 내용 -->
          <div class="modal-body">
            <div  class="d-flex flex-column gap-3">
                <!-- 음향 -->
                <div class="d-flex flex-column m-2">
                    <div class="d-flex flex-row justify-content-between mb-3">
                        <h4 class="fw-bold ps-2 me-4">음향</h4>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="container">
                        <label for="bgmRange" class="form-label">BGM</label>
                        <input v-model="soundStore.bgmSoundRange" type="range" id="bgmRange" class="form-range">                         
                    </div>
                    <div class="container">
                        <label for="soundRange" class="form-label">효과음</label>
                        <input v-model="soundStore.effectSoundRange" type="range" id="soundRange" class="form-range" >           
                    </div>
                </div>
                <!-- 카메라 -->
                <!-- <div class="d-flex flex-column gap-2">
                    <h4 class="fw-bold">카메라</h4>
                    <div class="form-check form-switch">
                        <input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked>
                        <label class="form-check-label" for="flexSwitchCheckChecked">관전 카메라 허용</label>
                    </div>
                </div> -->
                <!-- 문의 -> 구글폼으로 연결 -->
                <div>
                    <h4 class="fw-bold ps-2">문의하기
                        <a href="https://docs.google.com/forms/d/e/1FAIpQLSdmmaeY3EmByEDTqNVndKTz2GdtdbLpFgZK2nkJ8rG-IBM3Yg/viewform?usp=sf_link"
                        target="_blank">
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
import { useSoundStore } from '@/stores/sound';
const soundStore = useSoundStore()
const bgmRange = ref(0)
const soundRange = ref(0)
</script>

<style scoped>


</style>