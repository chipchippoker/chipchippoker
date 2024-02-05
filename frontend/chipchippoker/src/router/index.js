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
      meth: { auth: true }
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
      path: '/play/:roomId?',
      name: 'play',
      component: PlayView
    },
    {
      path: '/wait/:roomId?',
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

router.beforeEach((to, from, next) => {
  import('@/stores/user').then(({ useUserStore }) => {
  const userStore = useUserStore()

  if (userStore.accessToken === null && to.name !== 'login' && to.name !== 'signup' && to.name !== 'kakaosignup') {
    alert('로그인이 필요합니다.')
    console.log('로그인이 필요합니다.');
    router.push({ name: 'login'})
    return
  }
    next();
  });
});

export default router
