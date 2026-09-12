<template>
  <!-- 登录容器：全屏居中显示，带渐变背景 -->
  <div class="login-container">
    <!-- 登录面板：白色半透明卡片，带毛玻璃效果 -->
    <div class="login-wrapper">
      <!-- 头部区域：Logo和标题 -->
      <div class="login-header">
        <div class="logo">
          <span class="logo-icon">🎨</span>
        </div>
        <h1 class="title">后台管理系统</h1>
        <p class="subtitle">欢迎回来，请登录</p>
      </div>
      
      <!-- 登录表单：@submit.prevent 阻止默认提交，触发 handleLogin 方法 -->
      <form class="login-form" @submit.prevent="handleLogin">
        <!-- 用户名输入框组 -->
        <div class="form-group">
          <!-- 输入框标签，for 关联到 input 的 id -->
          <label for="username" class="form-label">用户名</label>
          <div class="input-wrapper">
            <!-- 输入框左侧图标 -->
            <span class="input-icon">👤</span>
            <!-- 用户名输入框：v-model 双向绑定到 form.username -->
            <input 
              id="username"
              v-model="form.username"
              type="text"
              class="form-input"
              placeholder="请输入用户名"
              required
            />
          </div>
        </div>
        
        <!-- 密码输入框组 -->
        <div class="form-group">
          <label for="password" class="form-label">密码</label>
          <div class="input-wrapper">
            <span class="input-icon">🔒</span>
            <!-- 密码输入框：type="password" 隐藏输入内容 -->
            <input 
              id="password"
              v-model="form.password"
              type="password"
              class="form-input"
              placeholder="请输入密码"
              required
            />
          </div>
        </div>
        
        <!-- 记住我复选框 -->
        <div class="form-group form-check">
          <!-- 复选框：v-model 绑定到布尔值 -->
            <input 
              id="remember"
              v-model="form.remember"
              type="checkbox"
              class="check-input"
            />
          <label for="remember" class="check-label">记住我</label>
        </div>
        
        <!-- 登录按钮：type="submit" 触发表单提交 -->
        <button type="submit" class="login-btn" :disabled="isLoading">
          <!-- 加载状态时显示旋转图标 -->
          <span v-if="isLoading" class="loading-icon">⏳</span>
          <!-- 根据加载状态显示不同文字 -->
          {{ isLoading ? '登录中...' : '登 录' }}
        </button>
      </form>
      
      <!-- 底部区域：错误提示、提示信息、返回链接 -->
      <div class="login-footer">
        <!-- v-if 条件渲染：有错误时显示红色提示 -->
        <p class="error-message" v-if="errorMessage">{{ errorMessage }}</p>
        <!-- 路由链接：跳转到首页 -->
        <router-link to="/" class="back-link">← 返回首页</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { userApi } from '@/api'
// Vue 组件配置对象
export default {
  // 组件名称（用于调试和递归引用）
  name: 'LoginPage',
  
  /**
   * data() 函数：定义组件的响应式数据
   * 返回一个对象，对象的属性会变成组件的响应式数据
   * 通过 this.属性名 访问
   */
  data() {
    return {
      // 表单数据对象（与模板中的 v-model 绑定）
      form: {
        username: '',           // 用户名（字符串类型）
        password: '',           // 密码（字符串类型）
        remember: false         // 是否记住我（布尔类型）
      },
      isLoading: false,         // 加载状态：true=登录中，false=空闲
      errorMessage: ''          // 错误提示信息（字符串类型）
    }
  },
  
  /**
   * methods 对象：定义组件的方法
   * 通过 this.方法名() 调用
   */
  methods: {
    /**
     * async 异步方法：处理登录逻辑
     * async 关键字表示这是一个异步函数，可以使用 await
     */
    async handleLogin() {
      // 1. 设置加载状态为 true（显示加载动画）
      this.isLoading = true
      // 注意：这里不再清空 errorMessage，让错误提示常驻显示
      
      try {
        // 3. 调用后端登录接口，传入用户名和密码
        // await：等待异步请求完成，获取返回结果
        const response = await userApi.login({
          username: this.form.username,  // 从表单数据中获取用户名
          password: this.form.password   // 从表单数据中获取密码
        })
        
        // 4. 登录成功后，保存 Token 到 sessionStorage
        // sessionStorage：会话级存储，关闭浏览器即消失（安全）
        sessionStorage.setItem('token', response.token)
        // 5. 标记登录状态为已登录
        sessionStorage.setItem('isLoggedIn', 'true')
        
        // 7. 如果勾选了"记住我"，保存用户名到 localStorage
        // localStorage：持久化存储，关闭浏览器后仍保留
        if (this.form.remember) {
          localStorage.setItem('username', response.username)
        }
        
        // 8. 跳转到后台管理页面
        // this.$router：Vue Router 实例
        // push()：导航到指定路由
        this.$router.push('/admin')
        
      } catch (error) {
        // 9. 登录失败时，显示错误信息
        // error.message：错误信息（来自响应拦截器）
        this.errorMessage = error.message || '登录失败，请重试'
        // 清除密码，保留用户名
        this.form.password = ''
        
      } finally {
        // 10. 无论成功还是失败，都关闭加载状态
        this.isLoading = false
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #21338e 100%);
  padding: 20px;
}

.login-wrapper {
  width: 100%;
  max-width: 420px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  background: linear-gradient(135deg, #667eea 0%, #3856ec 100%);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
}

.logo-icon {
  font-size: 32px;
}

.title {
  font-size: 24px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 14px;
  color: #888;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-label {
  font-size: 14px;
  font-weight: 600;
  color: #555;
}

.input-wrapper {
  position: relative;
}

.input-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 16px;
  color: #999;
}

.form-input {
  width: 100%;
  padding: 14px 16px 14px 48px;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  font-size: 14px;
  transition: all 0.3s ease;
  background: #fafafa;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
  background: #fff;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-input::placeholder {
  color: #bbb;
}

.form-check {
  flex-direction: row;
  align-items: center;
  justify-content: flex-start;
  gap: 10px;
}

.check-input {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.check-label {
  font-size: 13px;
  color: #666;
  cursor: pointer;
}

.login-btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #667eea 0%, #3856ec 100%);
  border: none;
  border-radius: 10px;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
}

.login-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.login-btn:active:not(:disabled) {
  transform: translateY(0);
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.login-footer {
  margin-top: 24px;
  text-align: center;
}

.error-message {
  color: #e74c3c;
  font-size: 13px;
  margin-bottom: 12px;
}

.hint {
  font-size: 12px;
  color: #999;
  margin-bottom: 12px;
}

.back-link {
  font-size: 14px;
  color: #667eea;
  text-decoration: none;
}

.back-link:hover {
  text-decoration: underline;
}

.loading-icon {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>
