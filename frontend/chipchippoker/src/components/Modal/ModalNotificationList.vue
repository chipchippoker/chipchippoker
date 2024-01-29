<template>
    <div class=" modal-dialog modal-dialog-scrollable modal-dialog-centered">
        <div class="modal-content " style="background-color: #fff0c0;">
            <div class="modal-header border-0 position-relative">
                <h1 class="position-absolute top-50 start-50 translate-middle modal-title fs-5 mt-2">알림 리스트</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div>
                    <div class="d-flex flex-column gap-1">
                        <ModalNotificationItem v-for="item in friendStore.alarmList" :key="item"
                        :item="item"/>
                        <div v-if="friendStore.alarmList.length===0">알림없음</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
</template>

<script setup>
import ModalNotificationItem from '@/components/Modal/ModalNotificationItem.vue';
import { useFriendStore } from '@/stores/friend';
import { useUserStore } from '@/stores/user';
const friendStore = useFriendStore()
const userStore = useUserStore()

</script>

<style scoped>

</style>