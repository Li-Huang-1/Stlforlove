<template>
  <div id="app">
    <NavBar v-if="showNavBar" />
    <router-view v-slot="{ Component, route: currentRoute }">
      <!-- 普通页面使用out-in过渡 -->
      <transition v-if="!isAuthRoute" name="fade" mode="out-in">
        <!-- :key 就是给每个元素/组件一个 身份证号 ，Vue 用它来区分不同的元素。
        当 key 变化时，Vue 会销毁旧组件实例并创建新实例，而不是复用。 -->
        <component :is="Component" :key="currentRoute.fullPath" />
      </transition>
      <!-- 认证相关页面（登录、后台）不使用过渡 -->
      <component v-else :is="Component" :key="'auth-' + currentRoute.fullPath" />
    </router-view>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import NavBar from '@/components/NavBar.vue'

const route = useRoute()
const showNavBar = computed(() => {
  const noNavBarPaths = ['/login', '/admin']
  return !noNavBarPaths.some(p => route.path.startsWith(p))
})

// 判断当前是否是认证相关路由
const isAuthRoute = computed(() => {
  return route.path.startsWith('/login') || route.path.startsWith('/admin')
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
