<template>
    <div class="d-flex flex-column gap-3" style="height: 530px; width: 750px;">
        <div class="d-flex justify-content-between align-items-center w-100 gap-3">
        <!-- 인원수 필터 선택 -->
        <div class="d-flex ps-3">
            <div class="form-check ">
                <input v-model="isTwo" @change="updateRoomList" class="form-check-input " type="checkbox" value="" id="two">
                <label class="form-check-label x-little-text" for="two">
                    2인
                </label>
            </div>
            <div class="form-check ">
                <input v-model="isThree" @change="updateRoomList" class="form-check-input " type="checkbox" value="" id="three">
                <label class="form-check-label x-little-text" for="three">
                    3인
                </label>
            </div>
            <div class="form-check ">
                <input v-model="isFour" @change="updateRoomList" class="form-check-input " type="checkbox" value="" id="four">
                <label class="form-check-label x-little-text" for="four">
                    4인
                </label>
            </div>
            <div class="form-check ">
                <input v-model="isEmpty" @change="updateRoomList" class="form-check-input " type="checkbox" value="" id="empty">
                <label class="form-check-label x-little-text" for="empty">
                    빈방
                </label>
            </div>
        </div>
        
        <!-- 방검색 -->
        <div class="d-flex align-items-center gap-3">
            <input v-model="title" id="room-search" type="text" class="form-control little-text" placeholder="방 이름" aria-label="Recipient's username" aria-describedby="button-addon2" autocomplete="false">
            <font-awesome-icon type="button" @click="searchRoom" :icon="['fas', 'magnifying-glass']" style="color: #ffffff;" />
            <font-awesome-icon type="button" @click="refreshRoom" :icon="['fas', 'arrows-rotate']" style="color: #ffffff;" />
        </div>
    </div>
    
    <div class="w-100">
        <!-- 경쟁전 탭, 친선전 탭 버튼-->
        <ul class="nav nav-tabs nav-justified border-0"  id="myTab" role="tablist">
            <li class="nav-item" role="presentation">
                <button @click="changeType('친선')" class="nav-link active text-white" id="v-pills-friend-tab" data-bs-toggle="tab" data-bs-target="#v-pills-friend-tab-pane" type="button" role="tab" aria-controls="v-pills-friend-tab-pane" aria-selected="true">친선전</button>
            </li>
            <li class="nav-item" role="presentation">
                <button @click="changeType('경쟁')" class="nav-link text-white" id="v-pills-rank-tab" data-bs-toggle="tab" data-bs-target="#v-pills-rank-tab-pane" type="button" role="tab" aria-controls="v-pills-rank-tab-pane" aria-selected="false">경쟁전</button>
            </li>
        </ul>
        <!-- 탭 화면 -->
        <div class="tab-content border-0" id="myTabContent">
            <div class="tab-pane fade show active" id="v-pills-friend-tab-pane" role="tabpanel" aria-labelledby="v-pills-friend-tab" tabindex="0">
                <MainRoomFriendList/></div>
            <div class="tab-pane fade"  id="v-pills-rank-tab-pane" role="tabpanel" aria-labelledby="v-pills-rank-tab" tabindex="0">
                <MainRoomRankList/></div>
        </div>
    </div>

    <!-- 페이지네이션 -->
    <div>
        <nav aria-label="Page navigation example">
        <ul class="pagination pagination-sm justify-content-center gap-1" type="button">
            <!-- 이전 버튼 -->
            <!-- <li v-if="roomStore.isFirst" class="page-item xx-little-text">
                <a class="btn-outline-yellow rounded-1 px-1 text-black" href="#" aria-label="Previous">
                    <font-awesome-icon icon="backward" style="color: #000000;"/>
                </a>
            </li> -->
            <!-- 숫자 버튼 -->
            <li class="page-item" v-for="pagenum in roomStore?.pageArray" :key="pagenum">
                <a @click="pageRoom(pagenum)" class="btn-outline-yellow rounded-1 px-1 text-black">
                {{pagenum }}
                </a>
            </li>
            
            <!-- 이후 버튼 -->
            <!-- <li v-if="!roomStore.isLast" class="page-item xx-little-text">
            <a class="btn-outline-yellow rounded-1 px-1 text-black" href="#" aria-label="Next">
                <font-awesome-icon icon="forward" style="color: #000000;" />
            </a>
            </li> -->
        </ul>
        </nav>
    </div>
    </div>
</template>

<script setup>
import MainRoomRankList from './MainRoomRankList.vue';
import MainRoomFriendList from './MainRoomFriendList.vue';
import { ref, onMounted } from 'vue';
import { useRoomStore } from '@/stores/room';

const roomStore = useRoomStore()

const roomType = ref('친선')
const changeType = function(type){
    roomType.value = type
    roomStore.roomType = type
    refreshRoom()
}

const title = ref('')
const isTwo = ref(false)
const isThree = ref(false)
const isFour = ref(false)
const isEmpty = ref(false)

// 방제 검색
const searchRoom = function(){
    const payload = {
        type:roomType.value,
        title:title.value,
        isTwo:isTwo.value,
        isThree:isThree.value,
        isFour:isFour.value,
        isEmpty:isEmpty.value,
        page:0,
        size:8
    }
    roomStore.getRoomList(payload)
}

// 새로고침 검색
const refreshRoom = function(){
    const payload = {
        type:roomType.value,
        title: title.value, 
        isTwo:isTwo.value,
        isThree:isThree.value,
        isFour:isFour.value,
        isEmpty:isEmpty.value,
        page:0,
        size:8
    }
    roomStore.getRoomList(payload)
}

// 체크박스 검색
const updateRoomList = function () {
    const payload = {
        type: roomType.value,
        title: title.value,
        isTwo: isTwo.value,
        isThree: isThree.value,
        isFour: isFour.value,
        isEmpty: isEmpty.value,
        page: 0,
        size: 8
    };
    roomStore.getRoomList(payload);
}
    
// 페이지네이션 검색
const pageRoom = function(pagenum){
    const payload = {
        type:roomType.value,
        title: title.value,
        isTwo:isTwo.value,
        isThree:isThree.value,
        isFour:isFour.value,
        isEmpty:isEmpty.value,
        page:pagenum - 1,
        size:8
    }
    roomStore.getRoomList(payload)
    }

onMounted (() => {
    changeType('친선')
})


</script>

<style lang="css" scoped>
.form-check {
    margin-left: 10px;
    margin-right: 10px;
    font-size: 15px;
    cursor: pointer;
}
.form-check-label {
    font-size: 13px;
}

.nav-tabs .nav-link.active {
    background-color: #8497c7;
    border: 0px;
}

.border-0 {
    border: 0px;
}

.icon-hover{
    color: #fff;
    border-radius: 4px;
    padding: 3px;
}
.icon-hover:hover{
    background-color: #365aa1;
}
.pagination a {
  text-decoration: none;
}
</style>