<template>
    <div>    
    <div>
        <!-- 탭 버튼 -->
        <ul class="nav nav-tabs nav-justified border-0"  id="myTab" role="tablist">
        <li class="nav-item" role="presentation">
            <button @click="getRankData" class="nav-link active text-white" id="all-rank-tab" data-bs-toggle="tab" data-bs-target="#all-rank-tab-pane" type="button" role="tab" aria-controls="all-rank-tab-pane" aria-selected="true">전체</button>
        </li>
        <li class="nav-item" role="presentation">
            <button @click="getRankData" class="nav-link text-white" id="friend-tab" data-bs-toggle="tab" data-bs-target="#friend-tab-pane" type="button" role="tab" aria-controls="friend-tab-pane" aria-selected="false">친구</button>
        </li>
        </ul>

        <!-- 탭 화면 -->
        <div class="tab-content border-0" style="width: 300px;"  id="myTabContent">
            <div class="tab-pane fade show active "  id="all-rank-tab-pane" role="tabpanel" aria-labelledby="all-rank-tab" tabindex="0">
                <MainRankAllList/></div>
            <div class="tab-pane fade" id="friend-tab-pane" role="tabpanel" aria-labelledby="friend-tab" tabindex="0">
                <MainRankFriendList/></div>
        </div>
    </div>

    </div>
</template>

<script setup>
import MainRankAllList from '../Rank/MainRankAllList.vue';
import MainRankFriendList from '../Rank/MainRankFriendList.vue';
import { useFriendStore } from '@/stores/friend';
const friendStore = useFriendStore()
const getRankData = function (){
    friendStore.getAllRankList()
    friendStore.getFriendRankList()
    friendStore.getMyRankList()
}
</script>

<style scoped>

.nav-tabs .nav-link.active {
    background-color: #8497c7;
    border: 0px;
}

.border-0 {
    border: 0px;
}
</style>