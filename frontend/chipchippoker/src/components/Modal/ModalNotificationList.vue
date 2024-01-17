<template>
    <div class=" modal-dialog modal-dialog-scrollable modal-dialog-centered">
        <div class="modal-content " style="background-color: #ffde76;">
            <div class="modal-header border-0">
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div>
                    알림 리스트
                    <div class="d-flex flex-column gap-1">
                        <ModalNotificationItem v-for="item in items" :key="item"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
</template>

<script setup>
import ModalNotificationItem from './ModalNotificationItem.vue';
const items = [1,2,3,4,5,6,7,8,9,10]
</script>

<style scoped>

</style>