<template>
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content " style="background-color: #fff0c0;">
      <!-- 모달 헤더 -->
      <div class="modal-header border-0 position-relative">
        <h1 class="position-absolute top-50 start-50 translate-middle modal-title fs-5 mt-2" id="makeRoomModalLabel">방 생성</h1>
        <button @click="closeModal()" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <!-- 모달 바디 -->
      <div class="modal-body p-4">
        <form class="container">

          <!-- 방 제목 -->
          <div class="mb-3 row col-12">
            <label for="RoomName" class="col-3 col-form-label align-content-center">방 제목</label>
            <div class="col-9">
                <input type="text" class="form-control" id="RoomName" v-model="title">
            </div>
            <div v-if="title!=='' && title.length > 20" class="fw-lgitighter x-little-text text-danger">방 제목은 1 ~ 20 글자이어야 합니다.</div>
          </div>

          <!-- 공개 여부 -->
          <div class="row col-12 align-items-center mb-3">
            <!-- 비공개 체크 박스 -->
            <label class="col-4">공개 여부</label>
            <div class="col-4 form-check">
                <input @click="toggleIsPrivate('false')" class="form-check-input" type="radio" id="publicRadio" value=false v-model="isPrivate">
                <label class="form-check-label" for="publicRadio">공개</label>
            </div>
            <div class="col-4 form-check">
                <input @click="toggleIsPrivate('true')" class="form-check-input" type="radio" id="privateRadio" value=true v-model="isPrivate">
                <label class="form-check-label" for="privateRadio">비공개</label>
            </div>
          </div>

          <!-- 비밀번호 -->
          <div class="row col-12 align-items-center mb-3">
            <div class="col-3">
              <label for="inputPassword6" class="col-form-label" >Password</label>
            </div>
            <div class="col-9">
              <input :disabled="isPrivate==='false'" type="password" id="inputPassword6" class='form-control' aria-describedby="passwordHelpInline" v-model="password" autocomplete="on">
            </div>
            <div v-if="password!=='' && (password.length !== 4 || isNaN(password))" class="fw-lgitighter x-little-text text-danger">방 비밀번호는 4자리의 숫자이어야 합니다.</div>
          </div>
          
          <!-- 인원 수 -->
          <div class="row col-12 mb-3 align-items-center">
            <label class="col-3">인원</label>
            <div class="d-flex justify-content-between col-9">
              <select class="form-select text-center" style="width: 100px;" aria-label="Small select example" v-model="totalParticipantCnt">
                <option value="2">2인</option>
                <option value="3">3인</option>
                <option value="4">4인</option>
              </select>
              <!-- 방 만들기 버튼 -->
              <button data-bs-dismiss="modal" :disabled="!((0 < title.length && title.length <= 20) && (isPrivate === 'false' || (isPrivate === 'true' && password.length === 4 && !isNaN(password))))" @click="createRoom()" type="button" class="col-3 btn-outline-yellow rounded-2">방만들기</button>
            </div>
          </div>
        </form>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRoomStore } from "@/stores/room";

const roomStore = useRoomStore()

const title = ref('')
const isPrivate = ref('false')
const password = ref('')
const totalParticipantCnt = ref(2)

// 방 생성하기
const createRoom = function () {
  roomStore.isWatcher = false
  const payload = {
      title: title.value,
      isPrivate: isPrivate.value === 'true',
      password: password.value,
      totalParticipantCnt: totalParticipantCnt.value
  }
  console.log('방 생성하기!!');

  roomStore.createRoom(payload)
  title.value = ''
  isPrivate.value = 'false'
  password.value = ''
  totalParticipantCnt.value = 2
}


// 모달 닫으면
const closeModal = function() {
  title.value = ''
  isPrivate.value = 'false'
  password.value = ''
  totalParticipantCnt.value = 2
}

// 공개 비공개
const toggleIsPrivate = function(value) {
  isPrivate.value = value
  if (isPrivate.value === 'false') {
    password.value = ''
  }
}
</script>

<style scoped>

</style>