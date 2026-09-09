<template>
  <div class="message-wall-container">
    <!-- 页面头部 -->
    <header class="page-header">
      <div class="header-content">
        <h1 class="page-title">留 言 板</h1>
        <p class="page-subtitle">留下你的足迹与心声</p>
      </div>
    </header>

    <!-- 主体内容 -->
    <main class="main-content">
      <!-- 留言列表区域 -->
      <section class="message-section">
        <div class="section-title">共 {{ messages.length }} 条留言</div>
        <div class="message-grid">
          <article
            v-for="msg in messages"
            :key="msg.id"
            class="message-card"
          >
            <div class="message-header">
              <div class="avatar" :style="{ background: getAvatarColor(msg.nickname) }">
                {{ msg.nickname.charAt(0) }}
              </div>
              <div class="message-meta">
                <div class="nickname">{{ msg.nickname }}</div>
                <div class="time">{{ msg.createTime }}</div>
              </div>
            </div>
            <p class="message-content">{{ msg.content }}</p>
          </article>

          <!-- 空状态 -->
          <div v-if="messages.length === 0" class="empty-state">
            <span class="empty-icon">💌</span>
            <p class="empty-text">还没有留言，快来留下第一条吧</p>
          </div>
        </div>
      </section>

      <!-- 发布留言区域 -->
      <section class="post-section">
        <div class="post-card">
          <input
            v-model="form.nickname"
            class="nickname-input"
            placeholder="你的昵称"
            maxlength="20"
          />
          <textarea
            v-model="form.content"
            class="content-textarea"
            placeholder="写下你想说的话..."
            maxlength="200"
            rows="3"
          ></textarea>
          <div class="post-footer">
            <span class="char-count">{{ form.content.length }} / 200</span>
            <button class="post-btn" @click="submitMessage">发布留言</button>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import request from '@/utils/request'

// 头像渐变色盘：根据昵称首字符稳定取色
const AVATAR_COLORS = [
  'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
  'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
  'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
  'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
  'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
  'linear-gradient(135deg, #30cfd0 0%, #330867 100%)'
]

export default {
  name: 'MessageWall',
  data() {
    return {
      // 发布表单
      form: {
        nickname: '',
        content: ''
      },
      // 留言列表：由后端 /public/message/list 加载
      messages: []
    }
  },
  mounted() {
    // 挂载
    this.loadMessages()
  },
  methods: {
    // 加载留言列表（后端按创建时间倒序返回）
    async loadMessages() {
      try {
        const data = await request({
          url: '/public/message/list',
          method: 'get'
        })
        this.messages = data || []
      } catch (error) {
        console.error('获取留言列表失败:', error)
        this.messages = []
      }
    },
    // 发布留言：前端即时校验后调用后端接口
    async submitMessage() {
      const nickname = this.form.nickname.trim()
      const content = this.form.content.trim()
      if (!nickname) {
        this.$message.warning('请输入昵称')
        return
      }
      if (!content) {
        this.$message.warning('请输入留言内容')
        return
      }
      try {
        // 后端返回新插入的留言对象（含真实 id 与 createTime）
        const saved = await request({
          url: '/public/message',
          method: 'post',
          data: { nickname, content }
        })
        // 插入列表头部（最新留言在最前）
        if (saved) {
          this.messages.unshift(saved)
        }
        // 清空表单
        this.form.nickname = ''
        this.form.content = ''
        this.$message.success('留言发布成功')
      } catch (error) {
        // request 拦截器已 reject(Error(res.msg))，error.message 即后端校验错误消息
        this.$message.error(error.message || '留言发布失败')
      }
    },
    // 根据昵称首字符 charCode 稳定取一个渐变色
    getAvatarColor(nickname) {
      const code = nickname ? nickname.charCodeAt(0) : 0
      return AVATAR_COLORS[code % AVATAR_COLORS.length]
    }
  }
}
</script>

<style scoped>
.message-wall-container {
  min-height: 100vh;
  background-image: url('../assets/bottom-card1.jpg');
  background-size: cover;
  background-position: center center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  padding-bottom: 60px;
}

.page-header {
  background: transparent;
  padding: 60px 20px 40px;
  text-align: center;
}

.header-content {
  max-width: 800px;
  margin: 0 auto;
}

.page-title {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 2.5rem;
  font-weight: 700;
  color: #ffffff;
  margin: 0 0 12px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
  letter-spacing: 4px;
}

.page-subtitle {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 1.1rem;
  color: rgba(255, 255, 255, 0.6);
  margin: 0;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  /* 底部留出固定 post-card 的占位空间，避免最后几条留言被遮挡 */
  padding: 0 20px 280px;
}

/* 发布留言区域 */
.post-section {
  margin-top: 40px;
}

.post-card {
  position: fixed;
  bottom: 24px;
  left: 50%;
  transform: translateX(-50%);
  width: calc(100% - 40px);
  max-width: 1160px;
  z-index: 100;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
}

.nickname-input {
  width: 100%;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 12px;
  color: #ffffff;
  font-family: 'Noto Serif SC', 'Roboto', sans-serif;
  font-size: 1rem;
  margin-bottom: 12px;
  box-sizing: border-box;
  transition: border-color 0.3s ease, background 0.3s ease;
}

.nickname-input::placeholder {
  color: rgba(255, 255, 255, 0.4);
}

.nickname-input:focus {
  outline: none;
  border-color: rgba(99, 102, 241, 0.6);
  background: rgba(255, 255, 255, 0.12);
}

.content-textarea {
  width: 100%;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 12px;
  color: #ffffff;
  font-family: 'Noto Serif SC', 'Roboto', sans-serif;
  font-size: 1rem;
  line-height: 1.6;
  resize: vertical;
  min-height: 80px;
  box-sizing: border-box;
  margin-bottom: 12px;
  transition: border-color 0.3s ease, background 0.3s ease;
}

.content-textarea::placeholder {
  color: rgba(255, 255, 255, 0.4);
}

.content-textarea:focus {
  outline: none;
  border-color: rgba(99, 102, 241, 0.6);
  background: rgba(255, 255, 255, 0.12);
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.char-count {
  font-family: 'Roboto', sans-serif;
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.4);
}

.post-btn {
  padding: 10px 28px;
  background: linear-gradient(135deg, #1d3d80 0%, #385fb2 100%);
  color: #ffffff;
  border: none;
  border-radius: 12px;
  font-family: 'Noto Serif SC', 'Roboto', sans-serif;
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
}

.post-btn:hover {
  background: linear-gradient(135deg, #2851a0 0%, #4a72c8 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(99, 102, 241, 0.4);
}

.post-btn:active {
  transform: translateY(0);
}

/* 留言列表区域 */
.section-title {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.6);
  margin-bottom: 20px;
  padding-left: 4px;
}

.message-grid {
  column-width: 300px;
  column-gap: 24px;
}

.message-card {
  width: 100%;
  margin-bottom: 24px;
  break-inside: avoid;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.25);
}

.message-header {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #ffffff;
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 1.25rem;
  font-weight: 600;
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.message-meta {
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 0;
}

.nickname {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 1rem;
  font-weight: 600;
  color: #1f2937;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.time {
  font-family: 'Roboto', 'Helvetica Neue', sans-serif;
  font-size: 0.8rem;
  color: #9ca3af;
}

.message-content {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.95rem;
  line-height: 1.7;
  color: #374151;
  margin: 0;
  word-break: break-word;
}

/* 空状态 */
.empty-state {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 4rem;
  opacity: 0.5;
}

.empty-text {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 1.1rem;
  color: rgba(255, 255, 255, 0.5);
  margin-top: 16px;
}

/* 响应式 */
@media (max-width: 768px) {
  .page-header {
    padding: 40px 20px 30px;
  }

  .page-title {
    font-size: 1.8rem;
  }

  .page-subtitle {
    font-size: 1rem;
  }

  .message-grid {
    column-count: 1;
    column-gap: 20px;
  }

  .message-card {
    max-width: 100%;
  }
}

@media (max-width: 480px) {
  .post-card {
    padding: 16px;
  }

  .message-card {
    padding: 16px;
  }
}
</style>
