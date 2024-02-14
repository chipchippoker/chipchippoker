import { ref } from "vue";
import { defineStore } from "pinia";
import { useRoute, useRouter } from "vue-router";

import webstomp from "webstomp-client";
import { useUserStore } from "./user";
import { useRoomStore } from "./room";
import { useMatchStore } from "./match";
import { useOpenviduStore } from "./openvidu";
import { useSoundStore } from "./sound";

export const useGameStore = defineStore(
  "game",
  () => {
    const userStore = useUserStore();
    const roomStore = useRoomStore();
    const soundStore = useSoundStore();
    const matchStore = useMatchStore();
    const openviduStore = useOpenviduStore();
    const route = useRoute();
    const router = useRouter();

    const url = `wss://chipchippoker.shop/chipchippoker`;

    const stompClient = webstomp.client(url);

    // 구독 정보
    const subscriptionGame = ref(undefined);
    const subscriptionPrivate = ref(undefined);

    // 게임 정보
    const receiveMessage = ref("");
    // 게임 종료 결과 리스트
    const memberEndGameInfos = ref([]);
    const kindGame = ref(null);
    // 라운드 승자
    const winnerNickname = ref("");
    // 게임 방 정보
    const roomInfo = ref({});
    const gameRoomTitle = ref("");
    const memberInfos = ref([]); // 방에 있는 플레이어들 닉네임 순서
    const countOfPeople = ref(0);
    const totalCountOfPeople = ref(0);

    const myOrder = ref(0);
    const roomManagerNickname = ref("");
    const myPrivateSubId = ref("");
    const myGameSubId = ref("");
    const isMatch = ref(false);
    const isMatchStart = ref(false); // 매치로 시작하는 게임인가

    // 배팅 이벤트
    const bettingEvent = ref(false);
    const willBettingCoin = ref(0);

    // 게임 상태 변수
    const firstStart = ref(false);
    const indexing = function (nickname) {
      memberInfos.value.forEach((member, index) => {
        if (member.nickname === nickname) {
          myOrder.value = index;
        }
      });
    };

    // 게임 관련 데이터 -> 라운드상태, 최근라운드, 턴, 게임 멤버 정보, 배팅 정보
    const roundState = ref(false);
    const currentRound = ref(0);
    const yourTurn = ref(null);
    const penaltyInfos = ref([]);
    const gameMemberInfos = ref([]);
    const totalBettingCoin = ref([]);

    // 게임 데이터 임시 저장
    // 그러면 웹소켓 베팅 응답오면 다 여기에 저장 ?
    const nextRoundState = ref(false);
    const nextCurrentRound = ref(0);
    const nextYourTurn = ref(null);
    const nextPenaltyInfos = ref([]);
    const nextGameMemberInfos = ref([]);

    // 애니메이션 진행 상태 저장
    const isAnimationRunning = ref(false);

    // 배팅 잘못했을 때 모달
    const notMatchRound = ref(false);
    const notYourTurn = ref(false);
    const cannotBat = ref(false);

    // 친구신청 알림
    const isAlarmArrive = ref(false);

    // 웹소켓 연결 성공 이벤트
    stompClient.ws.onopen = function (params) {
      // console.log('===========================웹소켓 연결 성공 감지==================================')
    };

    // 웹소켓 연결 끊김 이벤트
    stompClient.ws.onclose = async function (event) {
      // console.log('===========================웹소켓 연결 끊김 감지==================================')
      // 1. 토큰이 있고
      if (userStore.accessToken) {
        // 2. 웹소켓 연결, 개인 메시지함 구독
        await connectHandler();
        // 2. 게임방 구독 중이고, 대기방인 경우 다시 방 구독
        if (subscriptionGame.value !== undefined && route.name === "wait") {
          await subscribeHandler(roomStore.title);
        } else if (
          subscriptionGame.value !== undefined &&
          route.name === "play"
        ) {
          window.location.reload();
        }
      }
    };

    // 웹소켓 연결 핸들러
    const connectHandler = async function () {
      stompClient.connect(
        { "access-token": userStore.accessToken },
        (frame) => {
          // console.log("Connect success", gameRoomTitle.value)
          // stompClient.heartbeat.outgoing = 5000;
          // stompClient.heartbeat.incoming = 0;
          // console.log(stompClient);
          // 개인 메세지함 구독(기본구독)
          subscriptionPrivate.value = stompClient.subscribe(
            `/from/chipchippoker/member/${userStore.myNickname}`,
            (message) => {
              // console.log('개인 메세지함 구독 성공')
              const response = JSON.parse(message.body).body;
              // console.log('============================================================',response,'======================================================================')

              // 개인메세지함 구독 아이디
              myPrivateSubId.value = message.headers.subscription;

              switch (response.code) {
                case "MB001": // 방장만 강제퇴장 요청 가능
                  // console.log(response.message)
                  alert(response.message);
                  break;
                case "MB002": // 모두 준비 상태가 아님
                  // console.log(response.message)
                  alert(response.message);
                  notMatchRound.value = true;
                  break;
                case "MB003": // 방장이 아니라 시작할 수 없음
                  // console.log(response.message)
                  alert(response.message);
                  break;
                case "MB004": // 현재 진행 중인 라운드와 일치하지 않음
                  // console.log('현재 진행 중인 라운드와 일치하지 않습니다.')
                  alert(response.message);
                  notMatchRound.value = true;
                  break;
                case "MB005": // 내 턴이 아님
                  // console.log('본인의 차례가 아닙니다.')
                  alert(response.message);
                  notYourTurn.value = true;
                  break;
                case "MB006": // 배팅 불가
                  alert(response.message);
                  cannotBat.value = true;
                  break;
                case "MB007": // 이미 시작한 게임 방
                  alert(response.message);
                  break;
                case "MB008": // 혼자서는 게임 시작 불가
                  alert(response.message);
                  break;
                case "MS005": // 강퇴 당함(본인)
                  // console.log(response.message);
                  alert(response.message);
                  receiveBanMe(response.message);
                  break;
                case "MS007": // 게임 진행
                  // 맨 처음 데이터 저장시에만 0.1초 미루기
                  if (firstStart.value === false) {
                    firstStart.value = true;
                    setTimeout(() => {
                      if (subscriptionGame.value !== undefined) {
                        nextRoundState.value = response.data.roundState;
                        nextCurrentRound.value = response.data.currentRound;
                        nextYourTurn.value = response.data.yourTurn;
                        nextGameMemberInfos.value =
                          response.data.gameMemberInfos;
                        if (totalBettingCoin?.value.length === 0) {
                          nextGameMemberInfos.value.forEach(() => {
                            totalBettingCoin?.value.push(1);
                          });
                        }
                      }
                    }, 100);
                  } else if (nextRoundState.value === false) {
                    // 새로운 라운드 저장할때는 8초 미루기
                    setTimeout(() => {
                      if (subscriptionGame.value !== undefined) {
                        // console.log(memberInfos.value, gameMemberInfos.value);
                        nextRoundState.value = response.data.roundState;
                        nextCurrentRound.value = response.data.currentRound;
                        nextYourTurn.value = response.data.yourTurn;
                        nextGameMemberInfos.value = response.data.gameMemberInfos;
                        // console.log('라운드 저장 8초 미룸');
                      }
                    }, 10000);
                  } else {
                    // 배팅은 1초 미루기
                    setTimeout(() => {
                      // console.log('배팅 리시브 받아서 1초 미루기');
                      if (subscriptionGame.value !== undefined) {
                        bettingEvent.value = true;
                        nextRoundState.value = response.data.roundState;
                        nextCurrentRound.value = response.data.currentRound;
                        nextYourTurn.value = response.data.yourTurn;
                        nextGameMemberInfos.value = response.data.gameMemberInfos;
                        nextGameMemberInfos.value.forEach((info, index) => {
                          totalBettingCoin.value[index] = info.bettingCoin;
                        });
                      }
                    }, 500);
                  }
                  break;
                case "MS008": // 라운드 종료
                  receiveRoundFinish(response.data);
                  break;
                case "MS012": // 친구요청 받음
                  isAlarmArrive.value = true;
                  break;
                case "ME003": // 방장이 아닌사람이 start 한다면
                  // console.log("님은 방장이 아님")
                  alert(response.message);
                  break;
                case "MN001": // 찾을 수 없는 방
                  // console.log(response.message)
                  break;
              }
            }
          );
        }
      );
    };

    // 구독 핸들러
    const subscribeHandler = async (gameRoomTitle) => {
      // 토픽 구독 및 수신
      subscriptionGame.value = stompClient.subscribe(
        `/from/chipchippoker/checkConnect/${gameRoomTitle}`,
        (message) => {
          // console.log('방 구독하기');
          // console.log("subscribe success")
          // 내 구독 아이디 저장

          myGameSubId.value = message.headers.subscription;
          // console.log(message.headers);
          const response = JSON.parse(message.body).body;
          // console.log(response)

          switch (response.code) {
            case "MS001": // 방 생성
              // console.log(response.message);
              receiveCreateRoom(response.data);
              break;
            case "MS002": // 방 입장
              // console.log(response.message);
              receiveJoinRoom(response.data);
              break;
            case "MS003": // 방 나가기
              // console.log(response.message);
              receiveExitRoom(response.data, response.code, response.message);
              break;
            case "MS004": // 강퇴(타인)
              // console.log(response.message);
              receiveBanYou(response.data);
              break;
            case "MS006": // 게임 준비 완료
              // console.log(response.message);
              receiveReady(response.data);
              break;
            case "MS008": // 라운드 종료
              receiveRoundFinish(response.data);
              break;
            case "MS010": // 친선 게임 종료
              receiveGameFinishFriend(response.data);
              break;
            case "MS009": // 경쟁 게임 종료
              receiveGameFinishRank(response.data);
              break;
            case "MB002": // 모두 준비상태가 아님
              // console.log('모두 준비 상태가 아닙니다.');
              alert(response.message);
              break;
            case "MB003": // 방장이 아님

            case "MS016": // 게임방 시작
              // console.log(response.message)
              receiveStartGame(response);
              break;
            case "MS011": // 매칭
              // console.log(response.message)
              receiveMatching(response.data);
            case "MS013": // 관전자 입장
              // console.log(response.message)
              receiveSpectaionRoom(response.data);
              break;
            case "MS014": // 관전자 퇴장
              // console.log(response.message)
              receiveSpectaionExit(response.data);
              break;
            case "MS015": // 게임방 방장이 게임에서 나감
              // console.log(response.message)
              receiveExitRoom(response.data, response.code, response.message);
              break;
            case "MS017": // 매치 종료 이후 대기방 상태
              // console.log(response.data)
              receiveWaitRoom(response.data);
          }
        }
      );
    };

    // 게임 매칭 SEND
    const sendMatching = function (title, countOfPeople) {
      gameRoomTitle.value = title;
      totalCountOfPeople.value = countOfPeople;
      // console.log(title, countOfPeople)
      stompClient.send(
        `/to/game/matching/${title}`,
        JSON.stringify({ countOfPeople }),
        { "access-token": userStore.accessToken }
      );
    };

    // 게임 매칭 RECEIVE
    const receiveMatching = function (data) {
      memberInfos.value = data.memberInfos;
      roomManagerNickname.value = data.roomManagerNickname;

      // 매치 인원이 다 모이면
      if (data.memberInfos.length === totalCountOfPeople.value) {
        matchStore.isSearching = false;

        setTimeout(() => {
          memberInfos.value.forEach((info) => {
            if (
              info.nickname === userStore.myNickname &&
              userStore.myNickname === roomManagerNickname.value
            ) {
              // console.log('매치 성공!!')
              isMatchStart.value = true; // 매치로 인한 게임 시작
              roomStore.startGame(matchStore.title); // 게임 시작 send
            }
          });
        }, 3000);
      } else {
        // console.log('매칭 중')
        matchStore.isSearching = true;
      }
    };

    // 방 생성 SEND
    const sendCreateRoom = function (title, countOfPeople) {
      // console.log(title, countOfPeople)
      stompClient.send(
        `/to/game/create/${title}`,
        JSON.stringify({ countOfPeople }),
        { "access-token": userStore.accessToken }
      );
    };

    // 방 생성 RECEIVE
    const receiveCreateRoom = function (data) {
      countOfPeople.value = data.countOfPeople;
      memberInfos.value = data.memberInfos;
      roomManagerNickname.value = data.roomManagerNickname;
      gameRoomTitle.value = roomStore.title;
      router.push({
        name: "wait",
        params: { roomId: roomStore.roomId },
      });
    };

    // 게임방 입장 SEND
    const sendJoinRoom = function (gameRoomTitle) {
      stompClient.send(`/to/game/enter/${gameRoomTitle}`, JSON.stringify({}), {
        "access-token": userStore.accessToken,
      });
    };

    // 게임방 입장 RECEIVE
    const receiveJoinRoom = function (data) {
      countOfPeople.value = data.countOfPeople;
      memberInfos.value = data.memberInfos;
      roomManagerNickname.value = data.roomManagerNickname;
      watchersNickname.value = data.spectatorList;
      gameRoomTitle.value = roomStore.title;
      router.push({
        name: "wait",
        params: { roomId: roomStore.roomId },
      });
    };

    // 게임방 나가기 SEND
    const sendExitRoom = function (gameRoomTitle) {
      stompClient.send(`/to/game/exit/${gameRoomTitle}`, JSON.stringify({}), {
        "access-token": userStore.accessToken,
      });
    };

    // 게임방 나가기 RECEIVE
    const receiveExitRoom = function (data, code, message) {
      // 대기방에서 모든 플레이어가 방을 나가면 관전자도 자동으로 나가도록
      if (code === "MS019") {
        alert("모든 플레이어가 방을 나갔습니다.");
        roomStore.leaveWatcher();
        return;
      }
      countOfPeople.value = data.countOfPeople;
      roomManagerNickname.value = data.roomManagerNickname;
      memberInfos.value = data.memberInfos;
      if (code === "MS015" && route.name === "wait") {
        if (roomManagerNickname.value === userStore.myNickname) {
          alert("방장이 되었습니다.");
        }
      }
    };

    // 게임 준비 SEND
    const sendReady = function (gameRoomTitle, isReady) {
      stompClient.send(
        `/to/game/ready/${gameRoomTitle}`,
        JSON.stringify({ isReady }),
        { "access-token": userStore.accessToken }
      );
    };

    // 게임 준비 RECEIVE
    const receiveReady = function (data) {
      countOfPeople.value = data.countOfPeople;
      memberInfos.value = data.memberInfos;
      roomManagerNickname.value = data.roomManagerNickname;
    };

    // 게임 시작 SEND
    const sendStartGame = function (gameRoomTitle) {
      stompClient.send(`/to/game/start/${gameRoomTitle}`, JSON.stringify({}), {
        "access-token": userStore.accessToken,
      });
    };

    // 게임 시작 RECEIVE
    const receiveStartGame = function (body) {
      // console.log('receiveStartGame', body)
      // 새로고침 횟수를 삭제합니다.
      localStorage.removeItem("refreshCount");

      router.push({
        name: "play",
        params: { roomId: roomStore.roomId },
      });
      firstStart.value = false;
    };

    // 배팅
    const bet = function (gameRoomTitle, action, bettingCoin) {
      // console.log('배팅하기!!!!!!!!!!!!!!!!!!!!!!!!!');
      const message = {
        currentRound: currentRound.value,
        action: action,
        bettingCoin: bettingCoin,
      };
      stompClient.send(
        `/to/game/betting/${gameRoomTitle}`,
        JSON.stringify(message),
        { "access-token": userStore.accessToken }
      );
    };

    // 강퇴 send
    const sendBan = function (gameRoomTitle, nickname) {
      stompClient.send(
        `/to/game/ban/${gameRoomTitle}`,
        JSON.stringify({ nickname }),
        { "access-token": userStore.accessToken }
      );
    };

    // 강퇴 타인
    const receiveBanYou = function (data) {
      countOfPeople.value = data.countOfPeople;
      memberInfos.value = data.memberInfos;
      roomManagerNickname.value = data.roomManagerNickname;
    };

    // 강퇴 본인
    const receiveBanMe = function (message) {
      // 게임 관련 데이터 초기화 시켜주기
      resetGameStore();
      openviduStore.leaveSession();
      router.push({ name: "main" });
    };

    // 라운드 종료
    const receiveRoundFinish = function (data) {
      // console.log('라운드 종료');
      nextRoundState.value = data?.roundState;
      nextCurrentRound.value = data?.currentRound;
      nextYourTurn.value = data?.yourTurn;
      nextGameMemberInfos.value = data?.gameMemberInfos;
      winnerNickname.value = data?.winnerNickname;
      nextPenaltyInfos.value = data?.penaltyInfos;

      // console.log("roundState", roundState.value);
      // console.log("currentRound", currentRound.value);
      // console.log("yourTurn", yourTurn.value);
      // console.log("gameMemberInfos", gameMemberInfos.value);
      // console.log("nextPenaltyInfos", nextPenaltyInfos.value);
    };

    // 경쟁 게임 종료
    const receiveGameFinishRank = function (data) {
      setTimeout(() => {
        if (subscriptionGame.value !== undefined || subscriptionSpectation.value!==undefined) {
        memberEndGameInfos.value = data.memberEndGameInfos;
        memberEndGameInfos.value.forEach((info) => {
          if (info.nickname === userStore.myNickname) {
            if (info.isResult === "승") {
              soundStore.winSound();
            } else if (info.isResult === "패") {
              soundStore.loseSound();
            }
          }
        });}
      }, 4000);
    };

    // 친선 게임 종료
    const receiveGameFinishFriend = function (data) {
      setTimeout(() => {
        if (subscriptionGame.value !== undefined || subscriptionSpectation.value!==undefined) {
        memberEndGameInfos.value = data.memberEndGameInfos;
        memberEndGameInfos.value.forEach((info) => {
          if (info.nickname === userStore.myNickname) {
            if (info.isResult === "승") {
              soundStore.winSound();
            } else if (info.isResult === "패") {
              soundStore.loseSound();
            }
          }
        });}
      }, 4000);
    };

    // 친구신청 Send
    const sendFriendRequest = function (nickname) {
      stompClient.send(
        "/to/friend/request",
        JSON.stringify({ nickname: nickname }),
        { "access-token": userStore.accessToken }
      );
    };
    // ------------------------------감정표현-------------------------------------------
    const playerEmotion = ref({});
    const showEmotionNickname = ref(userStore.myNickname);
    // ------------------------------감정표현-------------------------------------------

    // 방 나가기 시 초기화 함수
    const resetGameStore = async function () {
      // console.log('방나가기 데이터 청소')
      if (
        subscriptionGame.value !== undefined &&
        subscriptionGame.value.unsubscribe
      ) {
        subscriptionGame.value.unsubscribe();
      }
      if (
        subscriptionSpectation.value !== undefined &&
        subscriptionSpectation.value.unsubscribe
      ) {
        subscriptionSpectation.value.unsubscribe();
      }
      firstStart.value = false;
      winnerNickname.value = "";
      subscriptionGame.value = undefined;
      roomInfo.value = {};
      totalCountOfPeople.value = 0;
      myOrder.value = 0;
      memberInfos.value = [];
      myPrivateSubId.value = "";
      gameRoomTitle.value = "";
      roomManagerNickname.value = "";
      countOfPeople.value = 0;
      myGameSubId.value = "";
      isMatch.value = false;
      roundState.value = false;
      currentRound.value = 0;
      yourTurn.value = null;
      gameMemberInfos.value = [];
      subscriptionSpectation.value = undefined;
      mySpectateSubId.value = [];
      watchersNickname.value = [];
      memberEndGameInfos.value = [];
      playerEmotion.value = {};
      showEmotionNickname.value = userStore.myNickname;
      nextRoundState.value = false;
      nextCurrentRound.value = 0;
      nextYourTurn.value = null;
      nextGameMemberInfos.value = [];
      bettingEvent.value = false;
      willBettingCoin.value = 0;
      penaltyInfos.value = [];
      nextPenaltyInfos.value = [];
      totalBettingCoin.value = [];
      isAnimationRunning.value = false;
    };

    //---------------------------------------------------관전--------------------------------------------------------
    // 관전 구독 정보
    const subscriptionSpectation = ref(undefined);

    // 관전 정보
    const mySpectateSubId = ref("");
    const watchersNickname = ref([]);

    // 관전 구독 핸들러
    const spectateHandler = (gameRoomTitle) => {
      // 토픽 구독 및 수신
      subscriptionSpectation.value = stompClient.subscribe(
        `/from/chipchippoker/spectation/checkConnect/${gameRoomTitle}`,
        (message) => {
          // console.log('관전 구독하기');
          // 내 구독 아이디 저장

          mySpectateSubId.value = message.headers.subscription;
          // console.log(message.headers);
          const response = JSON.parse(message.body).body;
          // console.log(response)

          switch (response.code) {
            case "MS013": // 관전자 입장
              // console.log(response.message);
              receiveSpectaionRoom(response.data);
              break;
            case "MS014": // 관전자 퇴장
              // console.log(response.message);
              receiveSpectaionExit(response.data);
              break;
            case "MS002": // 방 입장
              // console.log(response.message);
              receiveJoinRoom(response.data);
              break;
            case "MS003": // 방 나가기
              // console.log(response.message);
              receiveExitRoom(response.data, response.code, response.message);
              break;
            case "MS015": // 게임방 방장이 게임에서 나감
              // console.log(response.message)
              receiveExitRoom(response.data, response.code, response.message);
              break;
            case "MS004": // 강퇴(타인)
              // console.log(response.message);
              receiveBanYou(response.data);
              break;
            case "MS006": // 게임 준비 완료
              // console.log(response.message);
              receiveReady(response.data);
              break;
            case "MS016": // 게임방 시작
              // console.log(response.message)
              receiveStartGame(response);
              break;
            case "MS007": // 게임 진행
              // 맨 처음 데이터 저장시에만 0.1초 미루기
              if (firstStart.value === false) {
                firstStart.value = true;
                setTimeout(() => {
                  if (subscriptionSpectation.value !== undefined) {
                    nextRoundState.value = response.data.roundState;
                    nextCurrentRound.value = response.data.currentRound;
                    nextYourTurn.value = response.data.yourTurn;
                    nextGameMemberInfos.value = response.data.gameMemberInfos;
                    nextGameMemberInfos.value.forEach(() => {
                      totalBettingCoin?.value.push(1);
                    });
                  }
                }, 100);
              } else if (nextRoundState.value === false) {
                // 새로운 라운드 저장할때는 2초 미루기
                setTimeout(() => {
                  if (subscriptionSpectation.value !== undefined) {
                    nextRoundState.value = response.data.roundState;
                    nextCurrentRound.value = response.data.currentRound;
                    nextYourTurn.value = response.data.yourTurn;
                    nextGameMemberInfos.value = response.data.gameMemberInfos;
                    // console.log('라운드 저장 4초 미룸');
                  }
                }, 10000);
              } else {
                // 배팅은 1초 미루기
                setTimeout(() => {
                  if (subscriptionSpectation.value !== undefined) {
                    console.log(bettingEvent.value);
                    bettingEvent.value = true;
                    nextRoundState.value = response.data.roundState;
                    nextCurrentRound.value = response.data.currentRound;
                    nextYourTurn.value = response.data.yourTurn;
                    nextGameMemberInfos.value = response.data.gameMemberInfos;
                    nextGameMemberInfos.value.forEach((info, index) => {
                      totalBettingCoin.value[index] = info.bettingCoin;
                    });
                  }
                }, 1000);
              }
              break;
            case "MS008": // 라운드 종료
              receiveRoundFinish(response.data);
              break;
            case "MS010": // 게임 종료
              receiveGameFinishFriend(response.data);
              break;
            case "MS019": // 플레이어가 모두 나가고 아무도 없을 때
              // console.log(response.message);
              receiveExitRoom(response.data, response.code, response.message);
              break;
          }
        }
      );
    };

    // 관전 SEND
    const sendSpectationRoom = function (gameRoomTitle, gameState) {
      stompClient.send(
        `/to/spectation/enter/${gameRoomTitle}`,
        JSON.stringify({ gameState }),
        { "access-token": userStore.accessToken }
      );
    };

    // 관전 RECEIVE
    const receiveSpectaionRoom = function (data) {
      watchersNickname.value = data.spectatorList;
      memberInfos.value = data.memberInfos;
      firstStart.value = false;
    };

    // 관전 나가기 SEND
    const sendSpectationExit = function (gameRoomTitle) {
      stompClient.send(
        `/to/spectation/exit/${gameRoomTitle}`,
        JSON.stringify({}),
        { "access-token": userStore.accessToken }
      );
    };

    // 관전 나가기 RECEIVE
    const receiveSpectaionExit = function (data) {
      watchersNickname.value = data.spectatorList;
    };
    //---------------------------------------------------관전--------------------------------------------------------

    return {
      // State
      rooms: ref([]),
      stompClient,
      roundState,
      currentRound,
      yourTurn,
      gameMemberInfos,
      winnerNickname,
      memberInfos,
      memberEndGameInfos,
      roomManagerNickname,
      resetGameStore,
      playerEmotion,
      showEmotionNickname,
      penaltyInfos,
      nextPenaltyInfos,

      // 임시 저장데이터
      nextRoundState,
      nextCurrentRound,
      nextYourTurn,
      nextGameMemberInfos,

      // 구독 정보
      subscriptionGame,
      myGameSubId,

      // 게임 상태 변수
      firstStart,
      kindGame,

      // 관전 정보
      spectateHandler,
      sendSpectationRoom,
      receiveSpectaionRoom,
      watchersNickname,
      sendSpectationExit,
      receiveSpectaionExit,

      // Action

      receiveMessage,
      sendMatching,
      receiveMatching,
      sendCreateRoom,
      receiveCreateRoom,
      sendJoinRoom,
      receiveJoinRoom,
      sendExitRoom,
      receiveExitRoom,
      sendReady,
      receiveReady,
      sendBan,
      receiveBanMe,
      receiveBanYou,
      sendStartGame,
      receiveStartGame,
      sendFriendRequest,
      bet,
      subscribeHandler,
      isAlarmArrive,
      // 배팅 시 오류 메세지
      notMatchRound,
      notYourTurn,
      cannotBat,
      indexing,
      myOrder,
      connectHandler,

      // 배팅 이벤트
      bettingEvent,
      willBettingCoin,
      totalBettingCoin,

      // 애니메이션
      isAnimationRunning,
    };
  },
  { persist: true }
);
