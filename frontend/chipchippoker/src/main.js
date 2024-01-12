import './main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import Vue from 'vue'
import WebRTC from 'vue-webrtc'
Vue.use(WebRTC)

import App from './App.vue'
import router from './router'
// import socket from 'vue3-websocket'


const app = createApp(App)

app.use(createPinia())
app.use(router)
// app.use(socket, '백앤드 URL')
app.mount('#app')
