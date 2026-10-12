<template>
  <div class="friend-links-container">
    <!-- 页面头部 -->
    <header class="page-header">
      <div class="header-content">
        <h1 class="page-title">友 情 链 接</h1>
        <p class="page-subtitle">连接已建立，信号稳定</p>
      </div>
    </header>

    <!-- 主体内容 -->
    <main class="main-content">
      <!-- 友链列表区域 -->
      <section class="links-section">
        <div class="section-title">共 {{ friendLinks.length }} 位好友</div>
        <div class="links-grid">
          <a
            v-for="(link, index) in friendLinks"
            :key="link.id"
            :href="getFullUrl(link.url)"
            target="_blank"
            rel="noopener noreferrer"
            class="link-card"
          >
            <!-- 右下角超大首字水印 -->
            <span class="card-watermark" aria-hidden="true">{{ link.name.charAt(0) }}</span>
            <!-- 序号 -->
            <span class="link-index">No.{{ String(index + 1).padStart(2, '0') }}</span>

            <!-- 左侧封面：整块站点图标 / 朱砂印章 -->
            <div class="link-cover">
              <img
                v-if="link.logo"
                :src="link.logo"
                :alt="link.name"
                class="link-cover-img"
              />
              <div v-else class="link-cover-fallback">{{ link.name.charAt(0) }}</div>
            </div>

            <!-- 右侧内容 -->
            <div class="link-body">
              <div class="link-name">{{ link.name }}</div>
              <p v-if="link.description" class="link-description">{{ link.description }}</p>
            </div>
            <!-- 右缘垂直居中箭头 -->
            <span class="link-arrow" aria-hidden="true">→</span>
          </a>

          <!-- 空状态 -->
          <div v-if="friendLinks.length === 0" class="empty-state">
            <span class="empty-icon">🤝</span>
            <p class="empty-text">暂无友链，期待与你交换</p>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import { friendLinkApi } from '@/api'

export default {
  name: 'FriendLinks',
  data() {
    return {
      // 友链列表：由后端 /public/friend-link/list 加载
      friendLinks: []
    }
  },
  mounted() {
    this.loadFriendLinks()
  },
  methods: {
    // 加载友链列表（后端按创建时间正序返回）
    async loadFriendLinks() {
      try {
        const data = await friendLinkApi.getList()
        this.friendLinks = data || []
      } catch (error) {
        console.error('获取友链列表失败:', error)
        this.friendLinks = []
      }
    },
    // 兜底：链接缺少协议头时补 https://，避免被浏览器当相对路径解析到当前域名
    getFullUrl(url) {
      if (!url) return '#'
      return /^https?:\/\//i.test(url) ? url : `https://${url}`
    }
  }
}
</script>

<style scoped>
.friend-links-container {
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
  padding: 0 20px 60px;
}

/* 友链列表区域 */
.section-title {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.6);
  margin-bottom: 20px;
  padding-left: 4px;
}

.links-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
}

/* 卡片：信笺式毛玻璃，左侧整块封面（正方形图 + 等高卡片） */
.link-card {
  position: relative;
  display: flex;
  flex-direction: row;
  overflow: hidden;
  height: 72px;
  /* 毛玻璃底：斜向渐变 + 背景模糊 + 顶部内高光模拟玻璃厚度 */
  background: linear-gradient(160deg, rgba(255, 255, 255, 0.16) 0%, rgba(255, 255, 255, 0.07) 100%);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.22);
  border-radius: 8px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.22), inset 0 1px 0 rgba(255, 255, 255, 0.25);
  transition: all 0.35s ease;
  text-decoration: none;
}

/* 左侧封面区：与卡片等高，72x72 正方形 */
.link-cover {
  width: 72px;
  height: 72px;
  flex-shrink: 0;
  overflow: hidden;
  border-right: 1px solid rgba(255, 255, 255, 0.15);
}

.link-cover-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.4s ease;
}

.link-card:hover .link-cover-img {
  transform: scale(1.06);
}

/* 无图标时：整块朱砂印章 */
.link-cover-fallback {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #c0453e 0%, #93312c 100%);
  color: #ffffff;
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 1.6rem;
  font-weight: 600;
  box-shadow: inset 0 0 0 3px rgba(255, 255, 255, 0.16);
}

/* 右下角衬线首字水印 */
.card-watermark {
  position: absolute;
  right: -2px;
  bottom: -8px;
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 2.6rem;
  font-weight: 700;
  line-height: 1;
  color: rgba(255, 255, 255, 0.07);
  pointer-events: none;
  user-select: none;
}

/* 右上角序号 */
.link-index {
  position: absolute;
  top: 6px;
  right: 9px;
  font-family: 'Georgia', serif;
  font-style: italic;
  font-size: 0.62rem;
  letter-spacing: 1px;
  color: rgba(255, 255, 255, 0.35);
}

/* 底部渐变光条：hover 时从左向右展开 */
.link-card::after {
  content: '';
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  height: 3px;
  background: linear-gradient(90deg, #4ea1e6 0%, #4292e8 50%, #187be6 100%);
  transform: scaleX(0);
  transform-origin: left;
  transition: transform 0.4s ease;
}

.link-card:hover {
  transform: translateY(-5px) rotate(-0.4deg);
  border-color: rgba(255, 255, 255, 0.35);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.3), inset 0 1px 0 rgba(255, 255, 255, 0.3);
}

.link-card:hover::after {
  transform: scaleX(1);
}

/* 右侧内容区 */
.link-body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 2px;
  /* 右侧留出箭头位置 */
  padding: 6px 30px 6px 14px;
  position: relative;
  z-index: 1;
}

.link-name {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.98rem;
  font-weight: 600;
  color: #ffffff;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.link-arrow {
  position: absolute;
  top: 50%;
  right: 12px;
  transform: translateY(-50%);
  font-family: 'Roboto', sans-serif;
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.5);
  transition: all 0.3s ease;
  z-index: 1;
}

.link-card:hover .link-arrow {
  color: #a5b4fc;
  transform: translateY(-50%) translateX(3px);
}

.link-description {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.74rem;
  line-height: 1.5;
  color: rgba(255, 255, 255, 0.78);
  margin: 0;
  word-break: break-word;
  position: relative;
  z-index: 1;
  /* 只展示一行，超出省略 */
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
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

  .links-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
}
</style>
