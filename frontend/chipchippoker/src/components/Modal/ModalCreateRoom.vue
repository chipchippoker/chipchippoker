<template>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content " style="background-color: #fff0c0;">
            <div class="modal-header border-0 position-relative">
                <h1 class="position-absolute top-50 start-50 translate-middle modal-title fs-5 mt-2"
                    id="makeRoomModalLabel">방 생성</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form>
                    <!-- 방 제목 -->
                    <div class="mb-3 row">
                        <label for="RoomName" class="col-sm-2 col-form-label">방 제목</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="RoomName" v-model="title">
                        </div>
                    </div>
                    <!-- 공개 여부 -->
                    <div class="d-flex justify-content-between align-items-center">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault"
                                v-model="isPrivate">
                            <label class="form-check-label" for="flexCheckDefault">
                                비공개
                            </label>
                        </div>
                        <!-- 비밀번호 -->
                        
                    <div class="row g-3 align-items-center">
                        <div class="col-auto">
                            <label for="inputPassword6" class="col-form-label" >Password</label>
                        </div>
                        <div class="col-auto">
                            <input type="password" id="inputPassword6" class="form-control" aria-describedby="passwordHelpInline" v-model="password" autocomplete="on">
                        </div>
                      
                      
                    </div>
                </div>
                
                <div class="d-flex align-items-center gap-3">
                    <span>인원</span>
                    <select class="form-select form-select-sm" style="width: 100px;" aria-label="Small select example" v-model="totalParticipantCnt">
                        <option value="2">2인</option>
                        <option value="3">3인</option>
                        <option value="4">4인</option>
                    </select>
                </div>
                
                <div class="modal-footer border-0">
                    <button data-bs-dismiss="modal" @click="createRoom()" type="button" class="btn-outline-yellow rounded-2">방만들기</button>
                </div>
              </form>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from "vue";
import { useUserStore } from "@/stores/user";
import { useRoomStore } from "@/stores/room";
import { useRouter } from "vue-router";
import { faL } from "@fortawesome/free-solid-svg-icons";
const router = useRouter()

const userStore = useUserStore()
const roomStore = useRoomStore()

const title = ref('')
const isPrivate = ref(false)
const password = ref(null)
const totalParticipantCnt = ref(2)

// 방 생성하기
const createRoom = function () {
    const payload = {
        title: title.value,
        isPrivate: isPrivate.value,
        password: password.value,
        totalParticipantCnt: totalParticipantCnt.value
    }
    console.log('방 생성하기!!');

    roomStore.createRoom(payload)
    title.value = ''

    isPrivate.value = false
    password.value = null
    totalParticipantCnt.value = 2
}

</script>

<style lang="scss" scoped></style>