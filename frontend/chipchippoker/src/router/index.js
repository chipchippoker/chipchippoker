import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import SignupView from '../views/SignupView.vue'
import KakaoSignupView from '../views/KakaoSignupView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'logIn',
      component: LoginView
    },
   
    {
      path: '/signup',
      name: 'signUp',
      component: SignupView
    },
    {
      path: '/kakaosignup',
      name: 'kakaoSignUp',
      component: KakaoSignupView
    },
   
  ]
})

export default router
