<template>
  <div class="article-detail">
    <div v-if="loading" class="loading">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>
    <div v-else-if="article" class="article-wrapper">
      <header class="article-header">
        <h1 class="article-title">{{ article.title }}</h1>
        <div class="article-meta">
          <span class="meta-item"> {{ formatDate(article.createTime) }}</span>
          <span class="meta-divider">|</span>
          <span class="meta-item"> 管理员 LiHuang1</span>
        </div>
      </header>
      <div class="article-content ql-editor" v-html="article.content"></div>
      <footer class="article-footer">
        <button class="back-btn" @click="goBack">
          <span>←</span>
          <span>返回首页</span>
        </button>
      </footer>
    </div>
    <div v-else class="empty">
      <div class="empty-icon">📭</div>
      <p>文章不存在或已被删除</p>
      <button class="empty-btn" @click="goBack">返回首页</button>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'ArticleDetail',
  data() {
    return {
      article: null,
      loading: false
    }
  },
  computed: {
    articleId() {
      return this.$route.params.id
    }
  },
  mounted() {
    this.fetchArticle()
  },
  methods: {
    async fetchArticle() {
      this.loading = true
      try {
        const res = await request({
          url: `/public/article/${this.articleId}`,
          method: 'get'
        })
        this.article = res.data || res
      } catch (error) {
        console.error('加载失败:', error)
      } finally {
        this.loading = false
      }
    },
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const hour = String(d.getHours()).padStart(2, '0')
      const minute = String(d.getMinutes()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}`
    },
    goBack() {
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.article-detail {
  min-height: 100vh;
  background: linear-gradient(180deg, #e8f0fe 0%, #f8fafc 100%);
  padding: 40px 20px;
}

.loading {
  text-align: center;
  padding: 120px 0;
  color: #64748b;
}

.loading-spinner {
  width: 48px;
  height: 48px;
  border: 4px solid rgba(37, 99, 235, 0.1);
  border-top-color: #2563eb;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 16px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.article-wrapper {
  max-width: 1000px;
  margin: 0 auto;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.article-header {
  padding: 40px 40px 24px;
  border-bottom: 1px solid rgba(37, 99, 235, 0.1);
}

.article-title {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 16px;
  line-height: 1.3;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #64748b;
  font-size: 14px;
}

.meta-divider {
  color: rgba(37, 99, 235, 0.3);
}

.article-content {
  padding: 32px 40px;
  font-family: 'Noto Serif SC', 'Microsoft YaHei', serif;
  line-height: 2;
  font-size: 16px;
  color: #334155;
}

.article-content :deep(p) {
  margin-bottom: 16px;
}

.article-content :deep(h1) {
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
  margin: 32px 0 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid rgba(37, 99, 235, 0.2);
}

.article-content :deep(h2) {
  font-size: 20px;
  font-weight: 600;
  color: #1e293b;
  margin: 28px 0 14px;
}

.article-content :deep(h3) {
  font-size: 18px;
  font-weight: 600;
  color: #334155;
  margin: 24px 0 12px;
}

.article-content :deep(h4),
.article-content :deep(h5),
.article-content :deep(h6) {
  font-size: 16px;
  font-weight: 600;
  color: #475569;
  margin: 20px 0 10px;
}

.article-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 12px;
  margin: 16px 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.article-content :deep(code) {
  background: rgba(37, 99, 235, 0.08);
  padding: 3px 8px;
  border-radius: 4px;
  font-family: 'Fira Code', 'Consolas', monospace;
  font-size: 14px;
  color: #2563eb;
}

/* 代码块容器样式 - 兼容旧文章的 pre 标签 */
.article-content :deep(pre) {
  background: #1E1F22;
  padding: 16px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 16px 0;
  border: 1px solid #30363d;
  font-family: 'Fira Code', 'Consolas', monospace;
}

.article-content :deep(pre code) {
  background: transparent;
  color: #c9d1d9;
  padding: 0;
  font-size: 14px;
}

.article-content :deep(blockquote) {
  border-left: 4px solid #2563eb;
  padding: 16px 20px;
  margin: 20px 0;
  color: #64748b;
  font-style: italic;
  background: rgba(37, 99, 235, 0.03);
  border-radius: 0 8px 8px 0;
}

/* Quill 列表样式支持 - 使用 ::before 伪元素模拟 Quill 的标记 */
.article-content.ql-editor :deep(li[data-list]) {
  padding-left: 1.5em !important;
  position: relative;
  /* 关掉浏览器自带的标记 */
  list-style-type: none !important;
  margin-bottom: 8px !important;
}

/* 无序列表 - 使用与 Quill 相同的大圆点 '\2022'
.article-content.ql-editor :deep(li[data-list="bullet"]::before) {
  content: '\2022';
  position: absolute;
  left: 0;
  width: 1.5em;
  text-align: center;
  color: inherit;
  display: none;
} */

/* 有序列表 - 使用 CSS counter 实现数字编号 */
.article-content.ql-editor :deep(li[data-list="ordered"]) {
  counter-increment: list-counter;
}

.article-content.ql-editor :deep(li[data-list="ordered"]::before) {
  content: counter(list-counter) '. ';
  position: absolute;
  left: 0;
  width: 1.5em;
  text-align: right;
  color: inherit;
}

/* 重置有序列表计数器 */
.article-content.ql-editor :deep(ol),
.article-content.ql-editor :deep(li[data-list="ordered"]:first-child) {
  counter-reset: list-counter;
}

/* 嵌套列表缩进 */
.article-content.ql-editor :deep(li.ql-indent-1) {
  padding-left: 3em !important;
}

.article-content.ql-editor :deep(li.ql-indent-2) {
  padding-left: 6em !important;
}

.article-content.ql-editor :deep(li.ql-indent-3) {
  padding-left: 9em !important;
}

.article-content.ql-editor :deep(li.ql-indent-4) {
  padding-left: 12em !important;
}

/* Quill 文本对齐样式支持 */
.article-content.ql-editor :deep(.ql-align-center) {
  text-align: center !important;
}

.article-content.ql-editor :deep(.ql-align-right) {
  text-align: right !important;
}

.article-content.ql-editor :deep(.ql-align-justify) {
  text-align: justify !important;
}

.article-content :deep(a) {
  color: #2563eb;
  text-decoration: none;
  border-bottom: 1px solid rgba(37, 99, 235, 0.3);
  transition: border-color 0.2s ease;
}

.article-content :deep(a:hover) {
  border-bottom-color: #2563eb;
}

.article-content :deep(hr) {
  border: none;
  height: 1px;
  background: rgba(37, 99, 235, 0.15);
  margin: 32px 0;
}

.article-footer {
  padding: 20px 40px 32px;
  border-top: 1px solid rgba(37, 99, 235, 0.1);
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: rgba(37, 99, 235, 0.08);
  border: 1px solid rgba(37, 99, 235, 0.2);
  border-radius: 8px;
  color: #2563eb;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background: rgba(37, 99, 235, 0.15);
  border-color: rgba(37, 99, 235, 0.3);
}

.empty {
  text-align: center;
  padding: 100px 0;
  color: #64748b;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-btn {
  margin-top: 24px;
  padding: 12px 28px;
  background: linear-gradient(135deg, #4a90c2 0%, #2563eb 100%);
  border: none;
  border-radius: 10px;
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.empty-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(37, 99, 235, 0.4);
}
</style>