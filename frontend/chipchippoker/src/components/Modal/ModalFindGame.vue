<template>
  <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content pb-3" style="background-color: #fff0c0;">
            <div class="modal-header border-0">
                <button @click="stopFindGame()" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body d-flex flex-column justify-content-center align-items-center">
                <h3>게임을 찾는 중입니다.</h3>
                <div class="d-flex justify-content-center">
                    <div class="spinner-border mt-3" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                </div>
            </div>
        </div>
  </div>
</template>

<script setup>
import { useMatchStore } from '@/stores/match';
import { useRoomStore } from '@/stores/room';

const matchStore = useMatchStore()
const roomStore = useRoomStore()

const stopFindGame = function() {
    matchStore.stopFindGame()
}

</script>

<style scoped>

</style>