<template>
  <div class="position-absolute d-flex flex-column align-items-center p-3" style="width: 100%; height: 100%;">
    <!-- 옵션, 로고, 아이콘 -->
    <div class="d-flex justify-content-between align-items-center position-relative" style="width: 100%; height: 100px;">
      <button @click="goMainPage()" class="btn-transparency position-absolute top-50 start-50 translate-middle">
        <img class="small-logo m-0" src="/src/assets/icons/Logo.png" alt="">
      </button>
    </div>
    
     <!-- 나의 정보 / 전적 검색 -->
     <div class="d-flex flex-row-reverse w-75">
      <!-- 오른쪽에 수직 탭 -->
      <div class="nav flex-column nav-pills" id="v-pills-tab">
        <a class="nav-link active" style="border-top-left-radius: 0; width: 65px;
        border-bottom-left-radius: 0;" data-bs-toggle="pill" href="#v-pills-my-info">유저 정보</a>
        <a class="nav-link" style="border-top-left-radius: 0; width: 65px;
        border-bottom-left-radius: 0; " data-bs-toggle="pill" href="#v-pills-history">전적 검색</a>
      </div>
      <!-- 탭 내용 -->
      <div class="tab-content flex-grow-1">
        <div class="tab-pane fade show active" id="v-pills-my-info">
          <!-- ProfileMyInfo 컴포넌트에 memberId 전달 -->
          <ProfileMyInfo/>
        </div>
        <div class="tab-pane fade" id="v-pills-history">
          <!-- ProfileHistoryList 컴포넌트에 memberId 전달 -->
          <ProfileHistoryList/>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import ProfileMyInfo from '@/components/Profile/ProfileMyInfo.vue'
  import ProfileHistoryList from '@/components/Profile/ProfileHistoryList.vue'
  import { ref, onMounted } from "vue";
  import { useUserStore } from '@/stores/user'
  import { useRouter, onBeforeRouteUpdate } from 'vue-router'
  const userStore = useUserStore()
  const router = useRouter()
  onMounted(() => {
    const nickname = userStore.profileNickname
    userStore.getProfileInfo(nickname)
  })

  const goMainPage = function() {
    router.push({name: 'main'})
  }
  
</script>

<style scoped>
  .button-radius {
  border-top-left-radius: 0;
  border-bottom-left-radius: 0;
}
.nav-pills .nav-link.active {
    background-color: #576fac;
    border: 0px;
    transition: 0s;
  }
</style>