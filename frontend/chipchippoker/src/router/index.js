import { createRouter, createWebHistory } from 'vue-router'
import MainView from '../views/MainView.vue'
import LoginView from '../views/LoginView.vue'
import SignupView from '../views/SignupView.vue'
import KakaoSignupView from '../views/KakaoSignupView.vue'
import ProfileView from '../views/ProfileView.vue'


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
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView
    },
   
  ]
})

export default router
