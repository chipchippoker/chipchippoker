<template>
    <div style="width: 80%;">
        <!-- 로고와 방목록 -->

        <div class="d-flex align-items-center justify-content-end" style="width: 100%;" >
            <img class="small-logo" src="/src/assets/Logo.png" alt="">
            <div class="d-flex gap-3">
                <button class="btn btn-outline-yellow p-1 rounded-2"><strong>경쟁 모드</strong></button>
                <button class="btn btn-outline-yellow p-1 rounded-2"><strong>친선 모드</strong></button>
                <button class="btn btn-outline-yellow p-1 rounded-2"><strong>방 만들기</strong></button>
                <button class="btn btn-outline-yellow p-1 rounded-2"><strong>친구 찾기</strong></button>
            </div>
        </div>

        
        <div class="d-flex">
            <!-- 랭킹 -->
            <div style="width:350px">
                <MainRank class="mainstyle m-1"/>
            </div>
            <!-- 방목록 -->
            <div style="width:900px">
                <MainRoom class="mainstyle m-1"/>
            </div>
        </div>

        <!-- 친구 목록 -->
        <button class="btn-outline-yellow fixed-left" type="button" data-bs-toggle="offcanvas" data-bs-target="#friendList" aria-controls="friendList">
            <font-awesome-icon :icon="['fas', 'caret-left']" style="color: #ffffff;" />
        </button>

        <div class="offcanvas offcanvas-end" tabindex="-1" id="friendList" aria-labelledby="friendListLabel">
        <div class="offcanvas-header bg-lightyellow">
            <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body bg-lightyellow">
            <MainFriendList/>
        </div>
        </div>
    </div>
    
</template>

<script setup>
import MainFriendList from '@/components/Main/Friend/MainFriendList.vue';
import MainRank from '@/components/Main/Rank/MainRank.vue';
import MainRoom from '@/components/Main/Room/MainRoom.vue'
</script>

<style scoped>
@import "@/assets/color.css";
@import "@/assets/size.css";
.btn-outline-weightyellow {
    height: 30px;
    display: flex;
    align-items: center;
}
.fixed-left {
    position: fixed;
    right: 0;
    top: 100px;
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
    border-top-left-radius: 5px;
    border-bottom-left-radius: 5px;
    background-color: #ffde76;    
    height: 100px;
}

</style>