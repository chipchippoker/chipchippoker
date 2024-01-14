import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import socket from 'vue3-websocket'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { fas } from '@fortawesome/free-solid-svg-icons'

import "./style.css";
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'

// window.Kakao.init("e38b60d9d4f380ee6e87b6deeebf7a0a")


const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
library.add(fas)

// app.use(createPinia())
app.use(pinia)
app.use(router)
// app.use(socket, '백앤드 URL')
.component('font-awesome-icon', FontAwesomeIcon)
app.mount('#app')
