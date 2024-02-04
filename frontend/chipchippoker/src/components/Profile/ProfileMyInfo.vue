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
            <div class="col-6 my-3">{{ userStore?.profileInfo?.friendlyWin }}승 {{ userStore?.profileInfo?.friendlyDraw }}무 {{ userStore?.profileInfo?.friendlyLose }}패 / 
              {{ userStore?.profileInfo?.competitiveWin }}승 {{ userStore?.profileInfo?.competitiveDraw }}무 {{ userStore?.profileInfo?.competitiveLose }}패</div>
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

        <!-- 나의 프로필 -->
        <div v-if="userStore?.profileInfo?.isMine" class="d-flex flex-row-reverse">
          <button class="btn btn-signout" data-bs-toggle="modal" data-bs-target="#SignOutModal">회원탈퇴</button>
          <button class="btn btn-logout" data-bs-toggle="modal" data-bs-target="#LogOutModal">로그아웃</button>
          <button v-if="userStore.kakaoAccessToken === null" class="btn btn-kakao">
            <img class="kakao-logo" @click="userStore.kakaoConnect()" src="/src/assets/icons/kakaologo.png" alt="">
            카카오 연동하기
          </button>
        </div>
        <!-- 친구의 프로필 -->
        <div v-else class="d-flex flex-row-reverse">
          <!-- 신고하기 -->
          <div>
            <a class="btn_siren fw-bold"
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
            class="btn_sent">
                대기중
            </span>
            <span v-else @click="friendRequest" class="btn_request">친구 신청</span>
          </div>
        
        </div>
      </div>
             
      <!-- 아이콘 모달 팝업 -->
      <div class="modal fade" id="IconModal" tabindex="-1" aria-labelledby="IconModalLabel" aria-hidden="true">
        <ModalIconList/>
      </div>

      <!-- 로그아웃 모달 팝업 -->
      <div class="modal fade" id="LogOutModal" tabindex="-1" aria-labelledby="LogOutLabel" aria-hidden="true">
        <ModalLogOut />
      </div>
      <!-- 회원탈퇴 모달 팝업 -->
      <div class="modal fade" id="SignOutModal" tabindex="-1" aria-labelledby="SignOutLabel" aria-hidden="true">
        <ModalSignOut />
      </div>
    </div>
</template>
  
<script setup>
  import ModalIconList from "../Modal/ModalIconList.vue";
  import ModalLogOut from "../Modal/ModalLogOut.vue";
  import ModalSignOut from "../Modal/ModalSignOut.vue";

  import { ref, onMounted } from "vue";
  import { useUserStore } from '@/stores/user'
  import { useFriendStore } from "@/stores/friend";
  const userStore = useUserStore()
  const friendStore = useFriendStore()
  const profileInfo = ref({})
  const totalGame = ref(null)
  const isSent = ref(false)

  // 친구 요청
  const friendRequest = function(){
    console.log('친구신청 걸었음!!!')
    isSent.value = true
    friendStore.friendRequest(userStore?.profileInfo?.nickname)
  }
  onMounted(() => {
    profileInfo.value = userStore.profileInfo
    totalGame.value = profileInfo.win + profileInfo.draw + profileInfo.lose
  })
</script>
  
<style scoped>


  .kakao-logo {
    width: 15px;
    height: 15px;
    display: inline;
    margin: 0px;
  }
  .btn-kakao {
        width: 100px;
        font-size: 7pt;
        font-weight: bold;
        background-color: #F7E409;
        color: black;
        border: 0;
        padding: 4px 0px;
        margin: 2px;
    }

  .btn-logout {
    width: 60px;
    font-size: 7pt;
    background-color: #576FAC;
    border: solid 1px salmon;
    color: salmon;
    font-weight: bold;
    padding: 4px 0px;
    margin: 2px;
  }

  .btn-signout {
    width: 60px;
    font-size: 7pt;
    font-weight: bold;
    border: solid 1px rgb(63, 63, 63);
    color: rgb(60, 60, 60);
    padding: 4px 0px;
    margin: 2px;
  }
  .detail-info {
    border: 1px solid black;
  }

  .btn_request {
    padding: 5px 10px;
    border: 2px solid #ffc308;
    background-color: #ffde76;
    border-radius: 10px;
    margin-right: 15px;
    cursor:pointer;

  }
  .btn_request:hover {
    background-color: #ffc308;
    color: white;
    border: 2px solid #ffc308;
  }
  .btn_sent {
    padding: 5px 10px;
    border: 2px solid #afafaf;
    border-radius: 10px;
    margin-right: 15px;
    background-color: #afafaf;
  }

  .btn_siren {
    padding: 5px 10px;
    text-decoration: none;
    border: 2px solid #fff;
    color: red;
    border-radius: 10px;
    margin-right: 15px;
  }
  .btn_siren:hover {
    background-color: red;
    color: white;
    border: 2px solid red;
  }

</style>