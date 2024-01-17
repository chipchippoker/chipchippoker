<template>
    <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered">
        <div class="modal-content" style="background-color: #ffde76;">
          <div class="modal-header border-0">
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div>
                <div>
                    <h3>음향</h3>
                    <div class="d-flex">
                        <label for="bgmRange" class="form-label">BGM</label>
                        <input type="range" class="form-range" id="bgmRange">
                    </div>
                    <div class="d-flex">
                        <label for="soundRange" class="form-label">효과음</label>
                        <input type="range" class="form-range" id="soundRange">
                    </div>
                </div>
                <div>
                    <h3>카메라</h3>
                    <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked>
                    <label class="form-check-label" for="flexSwitchCheckChecked">관전 카메라 허용</label>
                    </div>
                </div>
                <div>
                    <h3>문의하기
                        <a href="https://docs.google.com/forms/d/e/1FAIpQLSdmmaeY3EmByEDTqNVndKTz2GdtdbLpFgZK2nkJ8rG-IBM3Yg/viewform?usp=sf_link">
                            <font-awesome-icon :icon="['fas', 'envelope-open']" style="color: #ffffff;" />
                        </a>
                    </h3>
                </div>
            </div>

          </div>
        </div>
      </div>
    
</template>

<script setup>

</script>

<style lang="scss" scoped>

</style>