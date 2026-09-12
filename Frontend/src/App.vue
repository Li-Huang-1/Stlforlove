<template>
  <div id="app">
    <!-- 导航栏随认证相关页面（登录、后台）的进出一并淡入淡出 -->
    <transition name="fade">
      <NavBar v-if="showNavBar" />
    </transition>
    <router-view v-slot="{ Component, route: currentRoute }">
      <!-- 所有页面统一使用 out-in 淡入淡出：旧页先淡出，新页再淡入，避免两页同时存在的布局跳动。
           transition 的直接子节点必须是稳定的真实 DOM 元素，因此用 div 包裹动态组件，
           并把 :key 放在 div 上（key 变化时 Vue 销毁旧 div、挂载新 div）。 -->
      <transition name="fade" mode="out-in" appear>
        <div :key="currentRoute.fullPath" class="route-wrapper">
          <component :is="Component" />
        </div>
      </transition>
    </router-view>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import NavBar from '@/components/NavBar.vue'

const route = useRoute()
// 认证相关页面（登录、后台）不显示导航栏
const showNavBar = computed(() => {
  const noNavBarPaths = ['/login', '/admin']
  return !noNavBarPaths.some(p => route.path.startsWith(p))
})
</script>

<style>
body {
  margin: 0;
  padding: 0;
  background-color: #1a202c;
}

#app {
  min-height: 100vh;
  background-color: #1a202c;
}

/* 路由页面包裹层：复刻 #app 的最小高度兜底，保证短内容页面背景也能铺满视口；
   本身为普通 block 元素，不影响各页面内部的 flex 等布局 */
.route-wrapper {
  min-height: 100vh;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from {
  opacity: 0;
}

.fade-leave-to {
  opacity: 0;
}
</style>
