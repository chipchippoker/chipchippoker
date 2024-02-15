<template>
    <div>
        <h4 class="text-center fw-bold mb-3">친구목록</h4>
        <div class=" m-3">
            <input v-model="nickname" type="text" class="form-control " id="friendId" placeholder="Nickname">
        </div>
        <MainFriendItem v-for="item in filterFriendList" :key="item.name"
        :item="item"/>
    </div>
</template>

<script setup>
import MainFriendItem from './MainFriendItem.vue';
import { useFriendStore } from '@/stores/friend';
import { computed, ref, watch } from 'vue';

const friendStore = useFriendStore()
const nickname = ref('')

// 친구 목록 필터
const filterFriendList = computed(()=>{
    let filterdList = friendStore.friendList
    if (nickname.value){
        filterdList = friendStore.friendList.filter(friend => friend.nickname.includes(nickname.value))
    }

    const onlineFriends = filterdList.filter(friend => friend.isOnline);
    const offlineFriends = filterdList.filter(friend => !friend.isOnline);

    // 온라인 친구 목록 정렬
    onlineFriends.sort((a, b) => {
        return a.nickname.localeCompare(b.nickname);
    });

    // 오프라인 친구 목록 정렬
    offlineFriends.sort((a, b) => {
        return a.nickname.localeCompare(b.nickname);
    });

    // 온라인 친구 + 오프라인 친구
    return onlineFriends.concat(offlineFriends)
})



</script>

<style lang="scss" scoped>

</style>