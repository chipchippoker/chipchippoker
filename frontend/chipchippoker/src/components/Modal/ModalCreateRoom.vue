<template>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content " style="background-color: #ffde76;">
            <div class="modal-header border-0">
                <h1 class="modal-title fs-5" id="makeRoomModalLabel">방 생성</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
        <div class="modal-body">
            <div>
                <div class="mb-3 row">
                    <label for="RoomName" class="col-sm-2 col-form-label">방 제목</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="RoomName" v-model="title">
                    </div>
                </div>
                <div class="d-flex justify-content-between align-items-center">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault" v-model="isPrivate">
                        <label class="form-check-label" for="flexCheckDefault">
                            비공개
                        </label>
                    </div>
                <div class="row g-3 align-items-center">
                    <div class="col-auto">
                        <label for="inputPassword6" class="col-form-label">Password</label>
                    </div>
                    <div class="col-auto">
                        <input type="password" id="inputPassword6" class="form-control" aria-describedby="passwordHelpInline" v-model="password">
                    </div>
                      
                      
                    </div>
                </div>
                
                <div class="d-flex align-items-center gap-3">
                    <span>인원</span>
                    <select class="form-select form-select-sm" style="width: 100px;" aria-label="Small select example" v-model="totalParticipantsCnt">
                        <option value="1">2인</option>
                        <option value="2">3인</option>
                        <option value="3">4인</option>
                    </select>
                </div>
                
                <div class="modal-footer border-0">
                    <button @click="createRoom()" type="button" class="btn btn-primary">방만들기</button>
                </div>
            </div>
        </div>
    </div>
</div>

</template>

<script setup>
import { ref } from "vue";
import { useUserStore } from "@/stores/user";
import { useRoomStore } from "@/stores/room";

const userStore = useUserStore()
const roomStore = useRoomStore()

const title = ref(null)
const isPrivate = ref(null)
const password = ref(null)
const totalParticipantsCnt = ref(1)

const createRoom = function(){
  const payload = {
      title: title.value,
      isPrivate: isPrivate.value,
      password: password.value,
      totalParticipantsCnt: totalParticipantsCnt.value
    }
  console.log('방 생성하기!!');
  roomStore.makeRoom(userStore.accessToken, payload.value)
}
</script>

<style lang="scss" scoped>

</style>