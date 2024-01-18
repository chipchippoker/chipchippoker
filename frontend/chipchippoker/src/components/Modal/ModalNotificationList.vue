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
                        <ModalNotificationItem v-for="item in friendStore.alarmList" :key="item"
                        :item="item"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
</template>

<script setup>
import ModalNotificationItem from './ModalNotificationItem.vue';
import { useFriendStore } from '@/stores/friend';
const friendStore = useFriendStore()

</script>

<style scoped>

</style>