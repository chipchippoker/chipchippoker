import { createRouter, createWebHistory } from 'vue-router'
import MainView from '../views/MainView.vue'
import LoginView from '../views/LoginView.vue'
import SignupView from '../views/SignupView.vue'
import KakaoSignupView from '../views/KakaoSignupView.vue'
import ProfileView from '../views/ProfileView.vue'
import PlayView from '@/views/PlayView.vue'

import WaitView from '../views/WaitView.vue'
import GameTest from '@/components/Game/GameTest.vue'
import animation from '@/views/animation.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/main',
      name: 'main',
      component: MainView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
   
    {
      path: '/signup',
      name: 'signup',
      component: SignupView
    },
    {
      path: '/kakaosignup',
      name: 'kakaosignup',
      component: KakaoSignupView
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView
    },
    {
      path: '/play/:roomId',
      name: 'play',
      component: PlayView
    },
    {
      path: '/wait/:roomId',
      name: 'wait',
      component: WaitView
    },  
    {
      path: '/game',
      name: 'game',
      component: GameTest
    },
    {
      path: '/animation',
      name: 'animation',
      component: animation
    }
  ]
})

export default router
