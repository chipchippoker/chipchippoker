<template>
  <div class="d-flex flex-column align-items-center p-3" style="width: 100%; height: 100%;">
    <!-- 옵션, 로고, 아이콘 -->
    <div class="d-flex justify-content-between align-items-center position-relative" style="width: 100%; height: 100px;">
      <div class="d-flex flex-row justify-content-between" style="width: 10%;">
        <div class="mx-3">
          <font-awesome-icon icon="bell"  shake size="lg" style="color: #ffffff;" />
        </div>
        <div class="mx-3">
          <font-awesome-icon icon="gear" size="lg" style="color: #ffffff;" />
        </div>
        <div class="ms-3">
          <font-awesome-icon icon="book" size="lg" style="color: #ffffff;" />
        </div>
      </div>
      <div class="position-absolute top-50 start-50 translate-middle"><img class="small-logo mt-2" src="/src/assets/Logo.png" alt=""></div>
      <div class="mx-3">
        <font-awesome-icon icon="user" size="lg" style="color: #ffffff;"  />
      </div>
    </div>
    <!-- 나의 정보 / 전적 검색 -->
    <div class="d-flex flex-row-reverse w-75">
      <!-- 오른쪽에 수직 탭 -->
      <div class="nav flex-column nav-pills" id="v-pills-tab">
        <a class="nav-link active" data-bs-toggle="pill" href="#v-pills-my-info">나의 정보</a>
        <a class="nav-link" data-bs-toggle="pill" href="#v-pills-history">전적 검색</a>
      </div>

      <!-- 탭 내용 -->
      <div class="tab-content flex-grow-1">
        <div class="tab-pane fade show active" id="v-pills-my-info">
          <!-- ProfileMyInfo 컴포넌트에 memberId 전달 -->
          <ProfileMyInfo :memberId="memberId" />
        </div>
        <div class="tab-pane fade" id="v-pills-history">
          <!-- ProfileHistoryList 컴포넌트에 memberId 전달 -->
          <ProfileHistoryList :memberId="memberId" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import ProfileMyInfo from '@/components/ProfileMyInfo.vue'
  import ProfileHistoryList from '@/components/ProfileHistoryList.vue'
  import { ref, onMounted } from "vue";
  import { useUserStore } from '@/stores/user'
  import { useRouter } from 'vue-router'

  const userStore = useUserStore()
  const recentPlayList = ref(null)

  // onMounted(() => {
  //   userStore.getProfileInfo(MemberId)
  // })
  
</script>

<style scoped>
  @import "@/assets/color.css";
  @import "@/assets/size.css";
</style>