<template>
    <div>
        <h3 class="text-white">MainRoom</h3>
        <div class="d-flex justify-content-between align-items-center w-100">
        <!-- 인원수 필터 선택 -->
        <div class="d-flex">
            <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="two">
                <label class="form-check-label" for="two">
                    2인
                </label>
            </div>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="three">
                <label class="form-check-label" for="three">
                    3인
                </label>
            </div>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="four">
                <label class="form-check-label" for="four">
                    4인
                </label>
            </div>
        </div>
        
        <!-- 방검색 -->
        <div>
            <input type="text" class="form-control" placeholder="방 이름" aria-label="Recipient's username" aria-describedby="button-addon2">
        </div>
    </div>


    <!-- 탭으로 나눌 경쟁전, 친선전 -->
    <MainRoomRankList/>
    <MainRoomFriendList/>


    
    </div>
</template>

<script setup>
import MainRoomRankList from './MainRoomRankList.vue';
import MainRoomFriendList from './MainRoomFriendList.vue';
const items = [1,2,3]
</script>

<style lang="css" scoped>
@import "@/assets/color.css";
@import "@/assets/size.css";
.form-check {
    margin-left: 10px;
    margin-right: 10px;
}
</style>