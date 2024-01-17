<template>
    <div class="d-flex flex-column gap-3">
        <div class="d-flex justify-content-between align-items-center w-100 gap-3">
        <!-- 인원수 필터 선택 -->
        <div class="d-flex">
            <div class="form-check ">
                <input class="form-check-input " type="checkbox" value="" id="two">
                <label class="form-check-label x-little-text" for="two">
                    2인
                </label>
            </div>
            <div class="form-check ">
                <input class="form-check-input " type="checkbox" value="" id="three">
                <label class="form-check-label x-little-text" for="three">
                    3인
                </label>
            </div>
            <div class="form-check ">
                <input class="form-check-input " type="checkbox" value="" id="four">
                <label class="form-check-label x-little-text" for="four">
                    4인
                </label>
            </div>
            <div class="form-check ">
                <input class="form-check-input " type="checkbox" value="" id="four">
                <label class="form-check-label x-little-text" for="four">
                    빈방
                </label>
            </div>
        </div>
        
        <!-- 방검색 -->
        <div class="d-flex align-items-center gap-3">
            <input type="text" class="form-control little-text" placeholder="방 이름" aria-label="Recipient's username" aria-describedby="button-addon2">
            <font-awesome-icon :icon="['fas', 'magnifying-glass']" style="color: #ffffff;" />
            <font-awesome-icon :icon="['fas', 'arrows-rotate']" style="color: #ffffff;" />
        </div>
    </div>
    
    <div >
        <!-- 경쟁전 탭, 친선전 탭 버튼-->
        <ul class="nav nav-tabs nav-justified border-0"  id="myTab" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link active text-white" id="v-pills-rank-tab" data-bs-toggle="tab" data-bs-target="#v-pills-rank-tab-pane" type="button" role="tab" aria-controls="v-pills-rank-tab-pane" aria-selected="true">경쟁전</button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link text-white" id="v-pills-friend-tab" data-bs-toggle="tab" data-bs-target="#v-pills-friend-tab-pane" type="button" role="tab" aria-controls="v-pills-friend-tab-pane" aria-selected="false">친선전</button>
        </li>
        </ul>
        <!-- 탭 화면 -->
        <div class="tab-content border-0" id="myTabContent">
            <div class="tab-pane fade show active" id="v-pills-rank-tab-pane" role="tabpanel" aria-labelledby="v-pills-rank-tab" tabindex="0">
                <MainRoomRankList/></div>
            <div class="tab-pane fade" id="v-pills-friend-tab-pane" role="tabpanel" aria-labelledby="v-pills-friend-tab" tabindex="0">
                <MainRoomFriendList/></div>
        </div>

        
    </div>
    <div>
        
        <nav aria-label="Page navigation example ">
        <ul class="pagination pagination-sm justify-content-center gap-1">
            <li class="page-item xx-little-text">
            <a class="btn-outline-yellow rounded-1 px-1 text-black" href="#" aria-label="Previous">
                <font-awesome-icon icon="backward" style="color: #000000;"/>
            </a>
            </li>
            <li class="page-item xx-little-text"><a class="btn-outline-yellow rounded-1 px-1 text-black" href="#">1</a></li>
            <li class="page-item xx-little-text"><a class="btn-outline-yellow rounded-1 px-1 text-black" href="#">2</a></li>
            <li class="page-item xx-little-text"><a class="btn-outline-yellow rounded-1 px-1 text-black" href="#">3</a></li>
            <li class="page-item xx-little-text">
            <a class="btn-outline-yellow rounded-1 px-1 text-black" href="#" aria-label="Next">
                <font-awesome-icon icon="forward" style="color: #000000;" />
            </a>
            </li>
        </ul>
        </nav>
    </div>






    
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

.nav-tabs .nav-link.active {
    background-color: #8497c7;
    border: 0px;
}

.border-0 {
    border: 0px;
}

</style>