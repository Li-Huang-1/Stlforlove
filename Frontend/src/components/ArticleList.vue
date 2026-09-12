<template>
  <div class="article-list-container">
    <!-- 页面头部 -->
    <header class="page-header">
      <div class="header-content">
        <h1 class="page-title">{{ pageTitle }}</h1>
        <p class="page-subtitle">{{ pageSubtitle }}</p>
      </div>
    </header>

    <!-- 左侧侧栏 + 右侧瀑布流 -->
    <main class="main-content">
      <!-- 侧栏：个人信息 + 查找，随页面正常滚动 -->
      <aside class="side-panel">
        <!-- 卡片一：个人信息名片 -->
        <div class="side-card profile-card">
          <div class="profile-banner"></div>
          <div class="profile-avatar-wrap">
            <img src="@/assets/image.jpg" alt="Avatar" class="profile-avatar" />
            <span class="profile-status-dot" title="在线"></span>
          </div>
          <div class="profile-name">Li_Huang1</div>
          <div class="profile-handle">@stlforlove</div>
          <p class="profile-bio">随心所欲的 Java 后端开发者</p>
          <div class="profile-tags">
            <span class="profile-tag">Java</span>
            <span class="profile-tag">Vue</span>
            <span class="profile-tag">Spring</span>
          </div>
          <div class="profile-stats">
            <div class="profile-stat">
              <div class="profile-stat-value">{{ articleCount }}</div>
              <div class="profile-stat-label">文章</div>
            </div>
            <div class="profile-stat-divider"></div>
            <div class="profile-stat">
              <div class="profile-stat-value">{{ visitorCount }}</div>
              <div class="profile-stat-label">访客</div>
            </div>
            <div class="profile-stat-divider"></div>
            <div class="profile-stat">
              <div class="profile-stat-value">{{ runningDays }}</div>
              <div class="profile-stat-label">运行天数</div>
            </div>
          </div>
        </div>

        <!-- 卡片二：标签快捷查找 + 关键词搜索 -->
        <div class="side-card search-card">
          <h3 class="search-card-title">查找文章</h3>

          <!-- 关键词搜索框 -->
          <div class="search-box">
            <input
              v-model="keyword"
              type="text"
              class="search-input"
              placeholder="搜索文章标题..."
              @keyup.enter="handleSearch"
            />
            <button class="search-btn" @click="handleSearch" title="搜索">
              <svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round">
                <circle cx="11" cy="11" r="7"></circle>
                <line x1="16.5" y1="16.5" x2="21" y2="21"></line>
              </svg>
            </button>
          </div>

          <!-- 标签快捷筛选 -->
          <div class="tag-section">
            <div class="tag-section-label">热门标签</div>
            <div class="tag-list">
              <span
                v-for="tag in allTags"
                :key="tag"
                class="filter-tag"
                :class="{ active: activeTag === tag }"
                @click="toggleTag(tag)"
              >{{ tag }}</span>
            </div>
          </div>

          <!-- 清除筛选 -->
          <button
            v-if="activeTag || keyword"
            class="clear-filter"
            @click="clearFilter"
          >清除筛选条件</button>
        </div>
      </aside>

      <!-- 右侧：瀑布流列表 -->
      <div class="list-wrapper">
        <div class="masonry-container">
          <div
            v-for="col in columnCount"
            :key="col"
            class="masonry-column"
          >
            <article 
              v-for="article in getColumn(col - 1)" 
              :key="article.id" 
              class="article-card"
              @click="goToArticle(article.id)"
            >
              <!-- 封面：统一 3:2 固定比例，图片按 cover 裁剪填充，保证所有卡片等高 -->
              <div class="article-cover">
                <img
                  v-if="article.coverUrl"
                  v-lazy="article.coverUrl"
                  :alt="article.title"
                  class="cover-image"
                  @error="handleCoverError($event)"
                />
                <div v-else class="cover-placeholder">
                  <span class="placeholder-icon">📝</span>
                </div>
              </div>

              <!-- 底部信息层：标签+日期同一行，标题在下 -->
              <div class="card-bottom">
                <div class="card-meta-row">
                  <div class="card-tags">
                    <span class="glass-tag" :class="getTypeClass(article.type)">
                      {{ getTypeLabel(article.type) }}
                    </span>
                    <span class="glass-tag tag-category">{{ article.category || '未分类' }}</span>
                  </div>
                  <p class="article-date">{{ formatDate(article.updateTime) }}</p>
                </div>
                <h2 class="article-title">{{ article.title }}</h2>
              </div>
            </article>
          </div>
        </div>

        <!-- 加载中指示器 -->
        <div v-if="isLoading" class="loading-indicator">
          <div class="loading-spinner"></div>
          <span class="loading-text">加载中...</span>
        </div>

        <!-- 到底了提示 -->
        <div v-if="!hasMore && articles.length > 0" class="end-indicator">
          <span class="end-line"></span>
          <span class="end-text">已经到底啦 ~</span>
          <span class="end-line"></span>
        </div>

        <!-- 空状态：有筛选条件时提示「无匹配」，否则提示「暂无文章」 -->
        <div v-if="!isLoading && articles.length === 0" class="empty-state">
          <span class="empty-icon">{{ hasFilter ? '🔍' : '�' }}</span>
          <p class="empty-text">{{ hasFilter ? '没有找到匹配的文章' : `暂无${pageTitle}` }}</p>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { articleApi, visitorApi } from '@/api'

const TYPE_LABELS = {
  tech: '技术栈',
  project: '项目',
  log: '日志'
}

const TYPE_CLASSES = {
  tech: 'type-tech',
  project: 'type-project',
  log: 'type-log'
}

export default {
  name: 'ArticleList',
  props: {
    type: {
      type: String,
      default: null
    },
    pageTitle: {
      type: String,
      default: '文章列表'
    },
    pageSubtitle: {
      type: String,
      default: '探索我的技术分享与生活感悟'
    }
  },
  data() {
    return {
      articles: [],
      pageSize: 6,
      currentPage: 0,
      isLoading: false,
      hasMore: true,
      scrollTimer: null,
      resizeTimer: null,
      columnCount: 3,
      articleCount: 0,
      visitorCount: 0,
      keyword: '',      // 搜索关键词
      activeTag: ''     // 当前选中的标签（category）
    }
  },
  computed: {
    // 博客运行天数（从 1.0 上线日起算，与首页一致）
    runningDays() {
      const startDate = new Date('2026-07-27')
      const today = new Date()
      return Math.floor((today - startDate) / (1000 * 60 * 60 * 24))
    },
    // 热门标签：从已加载文章的 category 聚合去重，按出现次数降序
    allTags() {
      const counter = {}
      this.articles.forEach(a => {
        if (a.category) counter[a.category] = (counter[a.category] || 0) + 1
      })
      return Object.keys(counter).sort((x, y) => counter[y] - counter[x])
    },
    // 当前是否存在筛选条件（用于区分「暂无文章」与「筛选无结果」两种空状态）
    hasFilter() {
      return !!(this.activeTag || this.keyword.trim())
    }
  },
  watch: {
    type() {
      this.resetAndLoad()
    }
  },
  methods: {
    async loadProfileStats() {
      try {
        const [countData, visitorData] = await Promise.all([
          articleApi.getCount(),
          visitorApi.getCount()
        ])
        this.articleCount = countData || 0
        this.visitorCount = visitorData?.count || 0
      } catch (error) {
        console.error('获取个人信息统计失败:', error)
      }
    },
    // 根据屏幕宽度动态计算列数（侧栏宽 260px + 间距 28px，断点需为侧栏预留空间）
    calcColumnCount() {
      const w = window.innerWidth
      if (w <= 768) return 1   // 窄屏：侧栏隐藏，单列
      if (w <= 1280) return 2  // 中屏：侧栏 + 双列，保证卡片宽度
      return 3                 // 宽屏：三列
    },
    // 取第 colIndex 列的文章（按原数组索引取模分配）
    getColumn(colIndex) {
      return this.articles.filter((_, i) => i % this.columnCount === colIndex)
    },
    // 点击标签：再次点击同一标签则取消选中，并按新分类重新请求第 1 页
    toggleTag(tag) {
      this.activeTag = this.activeTag === tag ? '' : tag
      this.reload()
    },
    // 关键词搜索（回车/点按钮触发，携带 keyword 请求服务端并重置分页）
    handleSearch() {
      this.reload()
    },
    // 清除筛选条件并重新请求
    clearFilter() {
      this.keyword = ''
      this.activeTag = ''
      this.reload()
    },
    getTypeLabel(type) {
      return TYPE_LABELS[type] || '未分类'
    },
    getTypeClass(type) {
      return TYPE_CLASSES[type] || 'type-default'
    },
    // 切换文章类型（type）：清空筛选条件后重新加载
    resetAndLoad() {
      this.keyword = ''
      this.activeTag = ''
      this.reload()
    },
    // 重置分页状态，按当前 type/keyword/category 条件重新请求第 1 页
    reload() {
      this.isLoading = false
      this.articles = []
      this.currentPage = 0
      this.hasMore = true
      this.fetchArticles(1)
    },
    async fetchArticles(page = 1) {
      if (this.isLoading) return
      if (!this.hasMore) return
      
      this.isLoading = true
      try {
        const data = {
          pageNum: page,
          pageSize: this.pageSize
        }
        if (this.type) {
          data.type = this.type
        }
        const keyword = this.keyword.trim()
        if (keyword) {
          data.keyword = keyword
        }
        if (this.activeTag) {
          data.category = this.activeTag
        }

        const response = await articleApi.getList(data)
        
        if (response.rows && response.total !== undefined) {
          if (page === 1) {
            this.articles = response.rows
          } else {
            this.articles.push(...response.rows)
          }
          this.currentPage = page
          this.hasMore = response.rows.length >= this.pageSize
        } else {
          this.hasMore = false
        }
      } catch (error) {
        console.error('获取文章列表失败:', error)
        this.hasMore = false
      } finally {
        this.isLoading = false
      }
    },
    handleScroll() {
      if (this.scrollTimer) clearTimeout(this.scrollTimer)
      this.scrollTimer = setTimeout(() => {
        const scrollTop = window.scrollY || document.documentElement.scrollTop
        const windowHeight = window.innerHeight
        const documentHeight = document.documentElement.scrollHeight
        if (scrollTop + windowHeight >= documentHeight - 100) {
          this.fetchArticles(this.currentPage + 1)
        }
      }, 150)
    },
    handleResize() {
      if (this.resizeTimer) clearTimeout(this.resizeTimer)
      this.resizeTimer = setTimeout(() => {
        const newCount = this.calcColumnCount()
        if (newCount !== this.columnCount) {
          this.columnCount = newCount
        }
      }, 150)
    },
    goToArticle(id) {
      this.$router.push(`/article/${id}`)
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    handleCoverError(e) {
      const img = e.target
      if (!img) return
      img.style.display = 'none'
      const parent = img.parentElement
      if (parent) {
        const placeholder = parent.querySelector('.cover-placeholder')
        if (placeholder) {
          placeholder.style.display = 'flex'
        }
      }
    }
  },
  mounted() {
    this.columnCount = this.calcColumnCount()
    this.fetchArticles(1)
    this.loadProfileStats()
    window.addEventListener('scroll', this.handleScroll)
    window.addEventListener('resize', this.handleResize)
  },
  beforeUnmount() {
    window.removeEventListener('scroll', this.handleScroll)
    window.removeEventListener('resize', this.handleResize)
    if (this.scrollTimer) clearTimeout(this.scrollTimer)
    if (this.resizeTimer) clearTimeout(this.resizeTimer)
  }
}
</script>

<style scoped>
.article-list-container {
  min-height: 100vh;
  background-image: url('../assets/bottom-card1.jpg');
  background-size: cover;
  background-position: center center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  padding-bottom: 60px;
}

/* 长条状页面导航栏：毛玻璃横条，与下方文章紧贴 */
.page-header {
  padding: 14px 20px;
  background: rgba(17, 31, 51, 0.45);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(147, 197, 253, 0.15);
}

.header-content {
  display: flex;
  align-items: baseline;
  flex-wrap: wrap;
  gap: 6px 14px;
  margin: 0;
  padding-left: 20px;
}

.page-title {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 1.35rem;
  font-weight: 700;
  color: #ffffff;
  margin: 0;
  letter-spacing: 2px;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.35);
  white-space: nowrap;
}

.page-subtitle {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.6);
  margin: 0;
  letter-spacing: 1px;
}

/* 标题与副标题之间的斜杠分隔 */
.page-subtitle::before {
  content: '/';
  margin-right: 14px;
  color: rgba(147, 197, 253, 0.5);
  font-family: 'Roboto', sans-serif;
}

.main-content {
  /* 不限最大宽度，左右各留 20px 对称边距，中间空间全给列表；顶部收紧贴近导航栏 */
  margin: 0;
  padding: 20px 20px 40px;
  display: flex;
  align-items: flex-start; /* 侧栏按自身内容高度，顶部与列表对齐 */
  gap: 28px;
}

/* 左侧侧栏：随页面正常滚动 */
.side-panel {
  width: 300px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 侧栏卡片基类：仅共享圆角；背景/边框/内边距由各卡片自行定义 */
.side-card {
  border-radius: 14px;
}

/* 个人信息名片 */
.profile-card {
  padding: 0;
  overflow: hidden; /* 圆角裁切 */
  text-align: center;
  /* 深蓝渐变，与页面顶部天空蓝背景图协调；左上角保留天光高光 */
  background:
    radial-gradient(circle at 15% -15%, rgba(147, 197, 253, 0.22), transparent 55%),
    linear-gradient(160deg, #1e3a5f 0%, #192f4d 50%, #111f33 100%);
  border: 1px solid rgba(147, 197, 253, 0.12);
}

/* 顶部横幅：卡片底色已统一，横幅只保留波点纹理装饰 */
.profile-banner {
  height: 68px;
  position: relative;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

.profile-banner::after {
  content: '';
  position: absolute;
  inset: 0;
  background-image: radial-gradient(rgba(255, 255, 255, 0.15) 1px, transparent 1px);
  background-size: 14px 14px;
  opacity: 0.4;
}

/* 悬挂头像：压在横幅下沿 */
.profile-avatar-wrap {
  position: relative;
  margin-top: -38px;
  display: inline-block;
}

.profile-avatar {
  width: 76px;
  height: 76px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.4);
  background: #111f33;
}

/* 在线状态绿点 */
.profile-status-dot {
  position: absolute;
  right: 4px;
  bottom: 4px;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: #22c55e;
  border: 3px solid #192f4d;
  box-shadow: 0 0 0 1px rgba(34, 197, 94, 0.4);
}

.profile-name {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 1.1rem;
  font-weight: 700;
  color: #ffffff;
  letter-spacing: 1px;
  margin-top: 10px;
}

.profile-handle {
  font-family: 'Roboto', 'Helvetica Neue', sans-serif;
  font-size: 0.75rem;
  color: rgba(255, 255, 255, 0.45);
  margin-top: 2px;
}

.profile-bio {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.7);
  margin: 10px 20px 0;
  letter-spacing: 0.5px;
}

/* 技术标签胶囊 */
.profile-tags {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 6px;
  margin: 12px 16px 0;
}

.profile-tag {
  font-family: 'Roboto', 'Helvetica Neue', sans-serif;
  font-size: 0.7rem;
  font-weight: 500;
  color: #93c5fd;
  background: rgba(37, 99, 235, 0.18);
  border: 1px solid rgba(96, 165, 250, 0.35);
  padding: 3px 10px;
  border-radius: 999px;
  letter-spacing: 0.5px;
}

/* 底部统计条 */
.profile-stats {
  display: flex;
  align-items: center;
  margin-top: 16px;
  padding: 14px 8px;
  border-top: 1px solid rgba(255, 255, 255, 0.12);
  background: rgba(0, 0, 0, 0.15);
}

.profile-stat {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.profile-stat-divider {
  width: 1px;
  height: 28px;
  background: rgba(255, 255, 255, 0.15);
}

.profile-stat-value {
  font-family: 'Roboto', 'Helvetica Neue', sans-serif;
  font-size: 1.1rem;
  font-weight: 700;
  color: #ffffff;
}

.profile-stat-label {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.7rem;
  color: rgba(255, 255, 255, 0.5);
  letter-spacing: 1px;
}

/* 查找卡片：搜索框 + 标签；背景与个人信息卡片一致（深蓝渐变实色） */
.search-card {
  padding: 16px;
  background:
    radial-gradient(circle at 15% -15%, rgba(147, 197, 253, 0.22), transparent 55%),
    linear-gradient(160deg, #1e3a5f 0%, #192f4d 50%, #111f33 100%);
  border: 1px solid rgba(147, 197, 253, 0.12);
}

.search-card-title {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.95rem;
  font-weight: 600;
  color: #ffffff;
  margin: 0 0 12px;
  letter-spacing: 2px;
}

/* 搜索框 */
.search-box {
  display: flex;
  align-items: center;
  background: rgba(0, 0, 0, 0.25);
  border: 1px solid rgba(147, 197, 253, 0.2);
  border-radius: 10px;
  padding: 0 4px 0 12px;
  transition: border-color 0.2s ease;
}

.search-box:focus-within {
  border-color: rgba(96, 165, 250, 0.6);
}

.search-input {
  flex: 1;
  min-width: 0;
  background: transparent;
  border: none;
  outline: none;
  color: #ffffff;
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.82rem;
  padding: 9px 0;
}

.search-input::placeholder {
  color: rgba(255, 255, 255, 0.4);
}

.search-btn {
  flex-shrink: 0;
  width: 30px;
  height: 30px;
  border: none;
  border-radius: 8px;
  background: rgba(37, 99, 235, 0.5);
  color: #ffffff;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s ease;
}

.search-btn:hover {
  background: rgba(37, 99, 235, 0.8);
}

/* 标签区 */
.tag-section {
  margin-top: 14px;
}

.tag-section-label {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.75rem;
  color: rgba(255, 255, 255, 0.5);
  letter-spacing: 2px;
  margin-bottom: 8px;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.filter-tag {
  font-family: 'Roboto', 'Helvetica Neue', sans-serif;
  font-size: 0.72rem;
  color: rgba(255, 255, 255, 0.75);
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
  padding: 4px 10px;
  border-radius: 999px;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
}

.filter-tag:hover {
  background: rgba(96, 165, 250, 0.25);
  border-color: rgba(96, 165, 250, 0.5);
  color: #ffffff;
}

.filter-tag.active {
  background: rgba(37, 99, 235, 0.75);
  border-color: rgba(147, 197, 253, 0.8);
  color: #ffffff;
  font-weight: 600;
}

/* 清除筛选 */
.clear-filter {
  width: 100%;
  margin-top: 12px;
  padding: 7px 0;
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.65);
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.75rem;
  letter-spacing: 1px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.clear-filter:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #ffffff;
}

/* 右侧列表区 */
.list-wrapper {
  flex: 1;
  min-width: 0;
}

/* 手动分列瀑布流：外层 flex 横向排列列容器 */
.masonry-container {
  display: flex;
  gap: 24px;
}

/* 每列纵向 flex，互不影响；min-width:0 打破 flex 项 min-width:auto，
   防止 aspect-ratio+min-height 反推最小宽度把列撑出容器 */
.masonry-column {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.article-card {
  position: relative;
  width: 100%;
  min-width: 0;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(15, 23, 42, 0.18);
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.article-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 44px rgba(15, 23, 42, 0.35);
}

/* 封面统一 3:2 固定比例（图片 object-fit:cover 裁剪填充），同屏卡片宽度一致则高度一致；
   min-height 兜底，防止窄屏下列宽过窄导致封面太矮 */
.article-cover {
  position: relative;
  width: 100%;
  aspect-ratio: 3 / 2;
  min-height: 250px;
  overflow: hidden;
}

.cover-image {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

/* 懒加载占位态 */
.cover-image.lazy-loading {
  object-fit: none;
  background: linear-gradient(135deg, #e8f0fe 0%, #f8fafc 100%);
}

.article-card:hover .cover-image:not(.lazy-loading) {
  transform: scale(1.06);
}

.cover-placeholder {
  position: absolute;
  inset: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1e3a5f 0%, #0f172a 100%);
}

.placeholder-icon {
  font-size: 3rem;
  opacity: 0.35;
}

/* 底部信息层：深色渐变 + 标签 + 日期 + 标题 */
.card-bottom {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1;
  padding: 16px 18px 18px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  background: linear-gradient(
    rgba(15, 23, 42, 0) 0%,
    rgba(15, 23, 42, 0.82) 55%,
    rgba(15, 23, 42, 0.92) 100%
  );
}

.card-meta-row {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.card-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  min-width: 0;
}

.glass-tag {
  padding: 3px 10px;
  font-size: 0.7rem;
  font-weight: 600;
  border-radius: 20px;
  color: #ffffff;
  letter-spacing: 0.5px;
  background: rgba(255, 255, 255, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}

.tag-category {
  background: rgba(37, 99, 235, 0.35);
  border-color: rgba(96, 165, 250, 0.55);
}

.type-tech {
  background: rgba(102, 126, 234, 0.35);
  border-color: rgba(167, 139, 250, 0.55);
}

.type-project {
  background: rgba(245, 87, 108, 0.35);
  border-color: rgba(251, 113, 133, 0.55);
}

.type-log {
  background: rgba(79, 172, 254, 0.3);
  border-color: rgba(56, 189, 248, 0.55);
}

.type-default {
  background: rgba(148, 163, 184, 0.3);
  border-color: rgba(148, 163, 184, 0.55);
}

.article-date {
  font-family: 'Roboto', 'Helvetica Neue', sans-serif;
  font-size: 0.78rem;
  color: rgba(255, 255, 255, 0.7);
  letter-spacing: 1px;
  margin: 0;
  flex-shrink: 0;
  white-space: nowrap;
}

.article-title {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 1.2rem;
  font-weight: 700;
  color: #ffffff;
  margin: 0;
  line-height: 1.45;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.4);
}

.loading-indicator {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 40px 20px;
}

.loading-spinner {
  width: 32px;
  height: 32px;
  border: 3px solid rgba(255, 255, 255, 0.2);
  border-top-color: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-text {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.6);
}

.end-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 40px 20px 20px;
}

.end-line {
  flex: 1;
  max-width: 80px;
  height: 1px;
  background: rgba(255, 255, 255, 0.25);
}

.end-text {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.45);
  letter-spacing: 2px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 4rem;
  opacity: 0.5;
}

.empty-text {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 1.2rem;
  color: rgba(255, 255, 255, 0.5);
  margin-top: 16px;
}

/* 响应式：列数由 JS calcColumnCount 驱动，这里只调容器间距 */
@media (max-width: 768px) {
  .page-header {
    padding: 12px 16px;
  }

  .page-title {
    font-size: 1.1rem;
    letter-spacing: 1px;
  }

  .page-subtitle {
    font-size: 0.78rem;
  }

  .main-content {
    padding: 16px 16px 32px;
  }

  /* 窄屏隐藏侧栏，列表全宽 */
  .side-panel {
    display: none;
  }
  
  .masonry-container {
    gap: 20px;
  }
  
  .masonry-column {
    gap: 20px;
  }
}

@media (max-width: 480px) {
  .article-title {
    font-size: 1.05rem;
  }
}
</style>
