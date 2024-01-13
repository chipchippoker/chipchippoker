import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
// import Vue from 'vue'
// import WebRTC from 'vue-webrtc'
import socket from 'vue3-websocket'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import "./style.css";
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'

// Vue.use(WebRTC)

const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

// app.use(createPinia())
app.use(pinia)
app.use(router)
// app.use(socket, '백앤드 URL')
app.mount('#app')
