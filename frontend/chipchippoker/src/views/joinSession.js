import { computed } from 'vue';
import { OpenVidu } from "openvidu-browser";
import { OV, session, subscribers, players, watchers, roomStore, myNickname, messages, getToken, roomId, mainStreamManager, publisher, leaveSession } from "./WaitView.vue";

export function joinSession() {
OV.value = new OpenVidu();
session.value = OV.value.initSession();
console.log('조인 세션!');

session.value.on("streamCreated", ({ stream }) => {
const subscriber = session.value.subscribe(stream);
console.log('세션 생성!!!!');
console.log(stream);

// 닉네임과 watcher 얻기
function getConnectionData() {
const { connection } = stream;
return JSON.parse(connection.data);
}
const clientData = computed(() => {
const { clientData } = getConnectionData();
return clientData;
});
const isWatcher = computed(() => {
const { isWatcher } = getConnectionData();
return isWatcher;
});

subscribers.value.push(subscriber);
// 플레이어, 관전자 리스트에도 추가
if (isWatcher === false) {
players.value.push(subscriber);
} else {
watchers.value.push(subscriber);
// 관전자 이름들도 추가
roomStore.watchersNickname.push(clientData);
}
});

// On every Stream destroyed...
session.value.on("streamDestroyed", ({ stream }) => {
const index = subscribers.value.indexOf(stream.streamManager, 0);
if (index >= 0) {
subscribers.value.splice(index, 1);
}
// 플레이어, 관전자 리스트에도 삭제
if (players.value.includes(stream.streamManager)) {
const index1 = players.value.indexOf(stream.streamManager, 0);
if (index1 >= 0) {
players.value.splice(index1, 1);
}
}
if (watchers.value.includes(stream.streamManager)) {
const index2 = watchers.value.indexOf(stream.streamManager, 0);
if (index2 >= 0) {
watchers.value.splice(index2, 1);
}
}
// 관전자 이름도 삭제
function getConnectionData() {
const { connection } = stream;
return JSON.parse(connection.data);
}
const clientData = computed(() => {
const { clientData } = getConnectionData();
return clientData;
});


if (roomStore.watchersNickname.includes(clientData)) {
const index3 = roomStore.watchersNickname.indexOf(clientData, 0);
if (index3 >= 0) {
roomStore.watchersNickname.splice(index3, 1);
}
}
});

// On every asynchronous exception...
session.value.on("exception", ({ exception }) => {
console.warn(exception);
});

session.value.on('reconnecting', () => console.warn('Oops! Trying to reconnect to the session'));
session.value.on('reconnected', () => console.log('Hurray! You successfully reconnected to the session'));
session.value.on('sessionDisconnected', (event) => {
if (event.reason === 'networkDisconnect') {
console.warn('Dang-it... You lost your connection to the session');
} else {
// Disconnected from the session for other reason than a network drop
}
});

// 채팅 이벤트 수신 처리 함. session.on이 addEventListenr 역할인듯.
session.value.on('signal:chat', (event) => {
const messageData = JSON.parse(event.data);
if (event.from.connectionId === session.value.connection.connectionId) {
messageData['username'] = myNickname.value;
}
messages.value.push(messageData);
});


// --- Connect to the session with a valid user token ---
// Get a token from the OpenVidu deployment
getToken(String(roomId.value)).then((token) => {
console.log("토큰 만들어지나");
session.value.connect(token, { clientData: myNickname.value, isWatcher: roomStore.isWatcher })
.then(() => {
// Get your own camera stream with the desired properties ---
let publisher_tmp = OV.value.initPublisher(undefined, {
audioSource: undefined, // The source of audio. If undefined default microphone
videoSource: undefined, // The source of video. If undefined default webcam


// publishAudio: !muted.value, // Whether you want to start publishing with your audio unmuted or not
// publishVideo: !camerOff.value, // Whether you want to start publishing with your video enabled or not
resolution: "400x300", // The resolution of your video
frameRate: 30, // The frame rate of your video
insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
mirror: false, // Whether to mirror your local video or not
});

// Set the main video in the page to display our webcam and store our Publisher
mainStreamManager.value = publisher_tmp;
publisher.value = publisher_tmp;

// --- Publish your stream ---
session.value.publish(publisher.value);
})
.catch((error) => {
console.log("There was an error connecting to the session:", error.code, error.message);
});
});

window.addEventListener("beforeunload", (event) => {
leaveSession();
// Uncomment the line below if you want to show a confirmation message
// event.returnValue = "Are you sure you want to leave?";
});
}
