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
const filterFriendList = computed(()=>{
    if (nickname.value ===''){
        return friendStore.friendList
    } else {
        return friendStore.friendList.filter(friend => friend.nickname.includes(nickname.value))
    }
})



</script>

<style lang="scss" scoped>

</style>