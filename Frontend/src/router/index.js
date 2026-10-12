import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/components/HomePage.vue'
import LoginPage from '@/components/Login.vue'
import AdminPanel from '@/components/Admin.vue'
import ArticleList from '@/components/ArticleList.vue'
import TechArticleList from '@/components/TechArticleList.vue'
import ProjectArticleList from '@/components/ProjectArticleList.vue'
import LogArticleList from '@/components/LogArticleList.vue'
import MessageWall from '@/components/MessageWall.vue'
import FriendLinks from '@/components/FriendLinks.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomePage,
    meta: { requiresAuth: false }
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage,
    meta: { requiresAuth: false }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: AdminPanel,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/article/new',
    name: 'NewArticle',
    component: () => import('@/components/ArticleEditor.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/article/edit/:id',
    name: 'EditArticle',
    component: () => import('@/components/ArticleEditor.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/article/:id',
    name: 'ArticleDetail',
    component: () => import('@/components/ArticleDetail.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/articles',
    name: 'ArticleList',
    component: ArticleList,
    meta: { requiresAuth: false }
  },
  {
    path: '/articles/tech',
    name: 'TechArticleList',
    component: TechArticleList,
    meta: { requiresAuth: false }
  },
  {
    path: '/articles/project',
    name: 'ProjectArticleList',
    component: ProjectArticleList,
    meta: { requiresAuth: false }
  },
  {
    path: '/articles/log',
    name: 'LogArticleList',
    component: LogArticleList,
    meta: { requiresAuth: false }
  },
  {
    path: '/message-wall',
    name: 'MessageWall',
    component: MessageWall,
    meta: { requiresAuth: false }
  },
  {
    path: '/friend-links',
    name: 'FriendLinks',
    component: FriendLinks,
    meta: { requiresAuth: false }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * - router.beforeEach ：注册一个全局前置守卫
 * - to ：即将要进入的目标路由对象（你要去的页面）
 * - from ：当前导航正要离开的路由对象（你从哪个页面来）
 * - next ：放行函数，调用它才能进入下一个钩子
 */
router.beforeEach((to, from, next) => {
  const isLoggedIn = sessionStorage.getItem('isLoggedIn') === 'true'
  // 如果目标页面需要登录权限（ requiresAuth: true ），但用户未登录，重定向到登录页
  if (to.meta.requiresAuth && !isLoggedIn) {
    next('/login')
    // 如果用户已登录，重定向到管理员面板
  } else if (to.path === '/login' && isLoggedIn) {
    next('/admin')
    // 反之放行
  } else {
    next()
  }
})

export default router
