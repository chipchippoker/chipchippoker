<template>
    <!-- 각각의 사용자 -->
    <div class="bg-lightyellow">
        <div class="friend-item d-flex align-items-center gap-2 bg-light rounded-2 mx-3">
            <!-- 아이콘 - 내용 d-flex  -->
            <img class="x-small-icon"  :src='userStore.getIconUrl(item?.icon)'>
            <!-- 내정보 , 온오프라인 정보 수직정렬 -->
            <div class="d-flex flex-column">
                <div class="container">
                    <!-- 티어, 닉, 카톡 아이콘 -->
                    <div class="row">
                        <div style="width:30px;"><img class="xx-small-icon"  :src='friendStore.getTierIconUrl(item?.tier)'></div>
                        <div class=" text-overflow" style="width:150px;">{{ item?.nickname }}</div>
                        <img v-if="item?.isKakaoFriend" class="icon text-overflow" src="@/assets/icons/kakaologo.png" alt="">
                    </div>
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
    </div>
</template>

<script setup>
// {
    // "icon": "asd",
    //     "tier": "bronze",
    //     "isKakaoFriend": true,
    //     "nickname": "윤예빈",
    //     "isOnline": true
    //   }




import { useUserStore } from '@/stores/user';
const userStore = useUserStore()
import { useFriendStore } from '@/stores/friend';
const friendStore = useFriendStore()
defineProps({item:Object})
</script>

<style scoped>
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