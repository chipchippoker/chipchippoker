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
			path: '/',
			redirect: '/login',
		},
    {
      path: '/main',
      name: 'main',
      component: MainView,
      meta: { requiresAuth: true }
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
      component: ProfileView,
      meta: { requiresAuth: true }
    },
    {
      path: '/play/:roomId?',
      name: 'play',
      component: PlayView,
      meta: { requiresAuth: true }
    },
    {
      path: '/wait/:roomId?',
      name: 'wait',
      component: WaitView,
      meta: { requiresAuth: true }
    },  
  ]
})

router.beforeEach((to, from, next) => {
  import('@/stores/user').then(({ useUserStore }) => {
  const userStore = useUserStore()
  const isAuthenticated = userStore.accessToken !== null

  // 인증이 필요한 페이지이고 사용자가 인증되어 있지 않다면
  if (to.meta.requiresAuth && !isAuthenticated) {
    // 로그인 페이지로 이동
    router.push({ name: 'login' })
  } else if (isAuthenticated && (to.name === 'login' || to.name === 'signup' || to.name === 'kakaosignup')) {
    // 사용자가 인증되어 있고, 로그인 페이지 또는 회원가입 페이지로 가려고 할 때
    // 메인 페이지로 리다이렉트
    router.push({ name: 'main' })
  } else {
    // 다른 경우에는 그냥 진행
    next()
  }
  })
})

export default router
