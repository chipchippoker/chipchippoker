<template>
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content " style="background-color: #fff0c0;">
            <!-- 닫힘버튼 -->
            <div class="modal-header border-0 position-relative">
                <h1 class="position-absolute top-50 start-50 translate-middle modal-title fs-5 mt-2">친구 찾기</h1>
                <button @click="dataRefresh" type="button" class="btn-close" data-bs-dismiss="modal"
                    aria-label="Close"></button>
            </div>
            <div class="modal-body d-flex justify-content-center align-items-center">
                <div class="row g-3 align-items-center">
                    <!-- 모달 내용 -->
                    <div class="col-auto">
                        <label for="userId" class="visually-hidden">nickname</label>
                        <input v-model="nickname" type="text" class="form-control" id="userId" placeholder="Nickname">
                    </div>
                    <div class="col-auto">
                        <button @click="findNickname" type="submit" class="btn-outline-yellow rounded-2">검색</button>
                    </div>
                </div>
            </div>

            <div class="text-center mb-3 ">
                <div v-if="Object.keys(friendStore.searchedPerson).length">
                    <!-- 검색 결과 -->
                    <div class="friend-item d-flex justify-content-between align-items-center bg-white rounded-2 mx-5">
                        <div type="button" 
                        @click="gotoProfile(friendStore.searchedPerson?.nickname)"
                        data-bs-dismiss="modal" aria-label="Close"
                            class="d-flex align-items-center gap-2">
                            <!-- 아이콘 - 내용 d-flex  -->
                            <img class="x-small-icon" :src='userStore.getIconUrl(friendStore.searchedPerson?.icon)'>
                            <!-- 내정보 , 온오프라인 정보 수직정렬 -->
                            <div class="d-flex flex-column">
                                <div class="container">
                                    <!--닉-->
                                    <div class="text-overflow text-start" style="width:150px;">{{
                                        friendStore.searchedPerson?.nickname }}</div>

                                </div>
                                <!-- 온오프라인 표시 -->
                                <div v-if="friendStore.searchedPerson?.isOnline" class="online-color x-little-text">
                                    <div class="d-flex align-items-center gap-2">
                                        <font-awesome-icon :icon="['fas', 'circle']" style="color: #4cba0d;" />
                                        <div>
                                            온라인
                                        </div>
                                    </div>
                                </div>
                                <div v-else class="x-little-text">
                                    <div class="d-flex align-items-center gap-2">
                                        <font-awesome-icon :icon="['fas', 'circle']" style="color: #eeeeee;" />
                                        <div class="text-secondary">
                                            오프라인
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 친구 신청 수신확인 -->
                        <!-- 이미 친구라면 -->
                        <div type="button" v-if="friendStore.searchedPerson?.isFriend == true"
                            class="bg-secondary-subtle p-1 rounded-3">
                            이미 친구입니다
                        </div>
                        <!-- 이미 보냈습니다 -->
                        <div type="button"
                            v-else-if="friendStore.searchedPerson?.isSent == true || friendStore.isSent === true"
                            class="bg-secondary-subtle p-1 rounded-3">
                            수락 대기중
                        </div>
                        <!-- 친구요청 (나라면 안보이게) -->
                        <div v-else-if="friendStore.searchedPerson?.nickname != userStore.myNickname"
                            @click="friendRequest(friendStore.searchedPerson?.nickname)" type="button"
                            class="btn-outline-lightyellow p-1 rounded-3">
                            친구신청
                        </div>

                    </div>
                </div>
                <div v-else-if="friendStore.isSearched === true">검색 결과 없음</div>
            </div>

        </div>
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useFriendStore } from '@/stores/friend'
import { useRouter } from 'vue-router'
import { faL } from '@fortawesome/free-solid-svg-icons';
import { useUserStore } from '@/stores/user';
const userStore = useUserStore()
const router = useRouter()
const friendStore = useFriendStore()

const nickname = ref(null)
//  데이터 청소
const dataRefresh = function () {
    friendStore.searchedPerson = {}
    friendStore.isSearched = false
    friendStore.isSent = false
    nickname.value = null
}
// 닉네임 검색
const findNickname = function () {
    console.log(`${nickname.value} 닉네임 검색`)
    friendStore.findFriendinAll(nickname.value)
    friendStore.isSent = false
    friendStore.isSearched = true
}

// 페이지 이동
const gotoProfile = function (nickname) {
    console.log(`${nickname}의 페이지로 이동`);
    dataRefresh()
    router.push({ name: 'profile', params: { nickname: nickname } })
}
// 친구요청
const friendRequest = function (nickname) {
    friendStore.friendRequest(nickname)
    friendStore.isSent = true
}


</script>
<style scoped>
.friend-item {
    height: 50px;
    margin-bottom: 10px;
    padding: 10px;
    overflow: hidden;
}

.icon {
    width: 50px;
    height: 100%;
}
</style>