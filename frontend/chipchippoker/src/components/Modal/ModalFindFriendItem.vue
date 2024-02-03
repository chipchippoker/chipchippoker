<template>
    <!-- 각각의 사용자 -->
    <div class="bg-modal">
        <div class="friend-item d-flex justify-content-between align-items-center bg-white rounded-2 mx-5">
            <div class="d-flex align-items-center  gap-2">
                <!-- 아이콘 - 내용 d-flex  -->
                <img type="button" @click="gotoProfile(item?.nickname)" class="x-small-icon"  :src='userStore.getIconUrl(item?.icon)'>
                <!-- 내정보 , 온오프라인 정보 수직정렬 -->
                <div class="d-flex flex-column">
                    <div type="button" @click="gotoProfile(item?.nickname)" class="container">
                        <!--닉-->
                        <div class="text-overflow text-start" style="width:150px;">{{ item?.nickname }}</div>
                    
                    </div>
                    <!-- 온오프라인 표시 -->
                    <div v-if="item?.isOnline" class="online-color x-little-text">
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
            <div type="button" v-if="item?.isFriend==true" class="bg-secondary-subtle p-1 rounded-3">
                이미 친구입니다
            </div>
            <!-- 이미 보냈습니다 -->
            <div type="button" v-else-if="item?.isSent==true || friendStore.isSent===true" class="bg-secondary-subtle p-1 rounded-3">
                수락 대기중
            </div>
            <!-- 친구요청 -->
            <div v-else-if="item?.nickname != userStore.myNickname" @click="friendRequest(item?.nickname)" type="button"  class="btn-outline-lightyellow p-1 rounded-3">
                친구신청
            </div>

        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
defineProps({
    item:Object
})
import { useUserStore } from '@/stores/user';
const userStore = useUserStore()
import { useFriendStore } from '@/stores/friend';
const friendStore = useFriendStore()
import { useRouter } from 'vue-router';

const router = useRouter()
// 페이지 이동
const gotoProfile = function(nickname){
    console.log(`${nickname}의 페이지로 이동`);
    router.push({name:'profile',params:{nickname:nickname}})
    friendStore.searchedPerson = {}
    friendStore.isSearched = false
    friendStore.isSent = false
}
// 친구요청
const friendRequest = function(nickname){
    friendStore.friendRequest(nickname)
    friendStore.isSent = true
}
</script>

<style  scoped>
.friend-item {
    height: 50px;
    margin-bottom: 10px;
    padding:10px;
    overflow: hidden;
}
.icon {
    width: 50px;
    height: 100%;
}
</style>