<template>
    <!-- 나의 정보, 게임 프로필 -->
    <div class="container mainstyle d-flex flex-row profile-box-radius" >
      <!-- 정보 -->
      <div class="d-flex flex-column w-100">
        <!-- 프사, 닉네임, 티어, 포인트 -->
        <div class="d-flex align-items-end fs-3">
          <!-- 프로필 사진 -->
          <div
          class="d-flex justify-content-center ms-5"
          style="position: relative;">
            <!-- 내프로필 -->
            <img v-if="userStore?.profileInfo?.isMine" 
            class="profile-icon-mine fade-in" 
            type="button"
            data-bs-toggle="modal" data-bs-target="#IconModal" 
            :src='userStore.getIconUrl(userStore?.myIcon)' :alt="userStore?.myIcon">

            <font-awesome-icon
            v-if="userStore?.profileInfo?.isMine"
            class="xx-small-icon"
            style="position: absolute; top: 35px; color: #ffffff;"
            type="button"
            data-bs-toggle="modal" data-bs-target="#IconModal" 
            :icon="['fas', 'arrows-rotate']"
            />
            <!-- 다른사람 프로필 -->
            <img v-else class="profile-icon-other" :src='userStore.getIconUrl(userStore?.profileInfo?.icon)' :alt="userStore?.profileInfo?.icon">
            
          </div>

          <!-- 닉네임 티어, 포인트 -->
          <div class="w-75 d-flex justify-content-center">
            <div class="d-flex align-items-center gap-5 fw-bold">
              <div>
                {{ userStore?.profileInfo?.nickname }}
              </div>
              <div style="width: 56px;">
                <img v-if="userStore?.profileInfo?.rank === 1" :src='friendStore.getTierIconUrl("rare")' style="width: 100%; height: 100%; object-fit: cover;">
                <img v-else :src='friendStore.getTierIconUrl(userStore?.profileInfo?.tier)' style="width: 100%; height: 100%; object-fit: cover;">
              </div>
              <div>
                {{ userStore?.profileInfo?.point }}pt
              </div>
            </div>
          </div>
        </div>

        <!-- 상세 정보 -->
        <div class="row profile-outline-darkblue m-3 text-center">
          <!-- 왼쪽 -->
          <div class="col-6 row m-1">
            <!-- 랭킹 전체 / 친구 -->
            <div class="mx-auto col-12 row my-3" v-if="userStore?.profileInfo?.isMine===true">
              <div class="col-6">
                <strong>랭킹(전체 / 친구)</strong>
              </div>
              <div class="col-6">{{ userStore?.profileInfo?.rank }}위 / {{ userStore?.profileInfo?.rankFriend }}위</div>
            </div>
            <!-- 랭킹(전체) -->
            <div class="col-12 row my-3" v-else>
              <div class="col-6">
                <strong>랭킹(전체)</strong>
              </div>
              <div 
               class="col-6">{{ userStore?.profileInfo?.rank }}위</div>
            </div>
            <!-- 게임 수 -->
            <div class="col-6 my-3">
              <strong>게임 수(경쟁 / 친선)</strong>
            </div>
            <div class="col-6 my-3">{{ userStore?.profileInfo?.competitiveTotal }}전 / {{ userStore?.profileInfo?.friendlyTotal }}전</div>
            <!-- 승패 -->
            <div class="col-6 my-3">
              <strong>승패(경쟁 / 친선)</strong>
            </div>
            <div class="col-6 my-3">{{ userStore?.profileInfo?.competitiveWin }}승 {{ userStore?.profileInfo?.competitiveDraw }}무 {{ userStore?.profileInfo?.competitiveLose }}패 / 
              {{ userStore?.profileInfo?.friendlyWin }}승 {{ userStore?.profileInfo?.friendlyDraw }}무 {{ userStore?.profileInfo?.friendlyLose }}패</div>
          </div>

          <!-- 오른쪽 -->
          <div class="col-6 row ms-1">
            <!-- 승률 -->
            <div class="col-6 my-3">
              <strong>승률(경쟁 / 친선)</strong>
            </div>
            <div class="col-6 my-3">{{ userStore?.profileInfo?.competitiveWinningRate }}% / {{ userStore?.profileInfo?.friendlyWinningRate }}%</div>
            <!-- 포인트 -->
            <div class="col-6 my-3">
              <strong>포인트</strong>
            </div>
            <div class="col-6 my-3">{{ userStore?.profileInfo?.point }}pt</div>
            <!-- 최대 연승 -->
            <div class="col-6 my-3">
              <strong>최대연승</strong>
            </div>
            <div class="col-6 my-3">{{ userStore?.profileInfo?.maxWin }}연승</div>
          </div>
        </div>

        <!-- 나의 프로필 버튼 -->
        <div v-if="userStore?.profileInfo?.isMine" class="d-flex flex-row-reverse" style="margin-right: 16px;">
          <button class="btn-2 btn-2-red" data-bs-toggle="modal" data-bs-target="#SignOutModal">회원탈퇴</button>
          <button class="btn-2 btn-2-blue" data-bs-toggle="modal" data-bs-target="#LogOutModal">로그아웃</button>
          <button v-if="userStore.kakaoAccessToken === null || userStore.profileInfo.isKakaoConnect === false" @click="kakaoConnect()" class="btn-2 btn-2-yellow">
            <img class="kakao-logo" src="/src/assets/icons/kakaologo.png" style="width: 20px;" alt="">
            <span>카카오 연동하기</span>
          </button>
        </div>
        <!-- 친구의 프로필 버튼 -->
        <div v-else class="d-flex flex-row-reverse" style="margin-right: 16px;">
          <!-- 신고하기 -->
          <div>
            <a class="btn-2 btn-2-red"
            href="https://docs.google.com/forms/d/e/1FAIpQLSdQLmWHJoz263PcrL3G_SLOzQUY28fVmG2wXJUtObYnEK-_WQ/viewform?usp=sf_link"
            target="_blank">
            <img style="width: 20px;" src="/src/assets/icons/siren.svg" alt="My Happy SVG" />
            <span>신고하기</span>  
            </a>
          </div>
          <!-- 친구가 아니라면 -->
          <div v-if="userStore?.profileInfo?.isFriend==false">
            <!-- 버튼 누르고 바뀜 -->
            <span v-if="isSent===true || userStore?.profileInfo?.isSent" 
            class="btn-2 btn-2-yellow-done">
                대기중
            </span>
            <span v-else @click="friendRequest" class="btn-2 btn-2-yellow">친구 신청</span>
          </div>
        </div>
      </div>
             
      <!-- 아이콘 모달 팝업 -->
      <div data-bs-backdrop="static" class="modal fade" id="IconModal" tabindex="-1" aria-labelledby="IconModalLabel" aria-hidden="true">
        <ModalIconList/>
      </div>

      <!-- 로그아웃 모달 팝업 -->
      <div data-bs-backdrop="static" class="modal fade" id="LogOutModal" tabindex="-1" aria-labelledby="LogOutLabel" aria-hidden="true">
        <ModalLogOut />
      </div>
      <!-- 회원탈퇴 모달 팝업 -->
      <div data-bs-backdrop="static" class="modal fade" id="SignOutModal" tabindex="-1" aria-labelledby="SignOutLabel" aria-hidden="true">
        <ModalSignOut />
      </div>
    </div>
</template>
  
<script setup>
  import ModalIconList from "../Modal/ModalIconList.vue";
  import ModalLogOut from "../Modal/ModalLogOut.vue";
  import ModalSignOut from "../Modal/ModalSignOut.vue";

  import { ref, onMounted } from "vue";
  import { useRoute, onBeforeRouteUpdate } from "vue-router";
  import { useUserStore } from '@/stores/user'
  import { useFriendStore } from "@/stores/friend";
  import { useRouter } from "vue-router";

  const userStore = useUserStore()
  const friendStore = useFriendStore()
  const router = useRouter()
  const route = useRoute()
  const profileInfo = ref({})
  const totalGame = ref(null)
  const isSent = ref(false)

  // 프로필페이지 갱신
  onBeforeRouteUpdate(async (to, from) => {
    if (to.params === from.params) {
      router.go(0)
    }
  })

  // 친구 요청
  const friendRequest = function(){
    console.log('친구신청 걸었음!!!')
    isSent.value = true
    friendStore.friendRequest(userStore?.profileInfo?.nickname)
  }
  
  // 카카오 연동하기
  const kakaoConnect = function() {
    console.log('인가코드 받기');
    userStore.getKakaoCodeToSink()
  }
  const authorizationCode = ref(null)
  // 카카오 인가코드 받기
  authorizationCode.value = route.query.code
  userStore.authorizationCode = authorizationCode.value
  
  // 인가코드 받으면
  if (authorizationCode.value) {
    console.log('카카오 연동 요청');
    userStore.kakaoConnect(authorizationCode.value)
    .then(result => {
      if (result) {
        authorizationCode.value = null
        console.log('카카오 연동 성공');
        alert("카카오 연동 성공하였습니다.")
        router.go(0)
      } else {
        alert("카카오 연동 실패했습니다.")
      }
    })
  }


  onMounted(() => {
    profileInfo.value = userStore.profileInfo
    totalGame.value = profileInfo.win + profileInfo.draw + profileInfo.lose
  })
</script>
  
<style lang="scss" scoped>
  .kakao-logo {
    width: 20px;
    display: inline;
    margin: 0px;
  }

  .detail-info {
    border: 1px solid black;
  }

  .btn-2 {
    height: 35px;
    font-size: 1.2em;
    align-items: center;
    padding: 10px;
  }
</style>