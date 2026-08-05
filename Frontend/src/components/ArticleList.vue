<template>
  <div class="article-list-container">
    <!-- 页面头部 -->
    <header class="page-header">
      <div class="header-content">
        <h1 class="page-title">文章列表</h1>
        <p class="page-subtitle">探索我的技术分享与生活感悟</p>
      </div>
    </header>

    <!-- 文章列表区域 -->
    <main class="main-content">
      <div class="article-grid">
        <!-- 文章卡片 -->
        <article 
          v-for="article in articles" 
          :key="article.id" 
          class="article-card"
          @click="goToArticle(article.id)"
        >
          <!-- 封面图片 -->
          <div class="article-cover">
            <img 
              v-if="article.coverUrl" 
              :src="article.coverUrl" 
              :alt="article.title"
              class="cover-image"
              @error="handleCoverError($event)"
            />
            <div v-else class="cover-placeholder">
              <span class="placeholder-icon">📝</span>
            </div>
          </div>

          <!-- 文章内容 -->
          <div class="article-body">
            <div class="article-meta">
              <span class="category-tag">{{ article.category || '未分类' }}</span>
              <span class="status-tag" :class="article.status === 1 ? 'published' : 'draft'">
                {{ article.status === 1 ? '已发布' : '草稿' }}
              </span>
            </div>
            <h2 class="article-title">{{ article.title }}</h2>
            <p class="article-date">{{ formatDate(article.createTime) }}</p>
          </div>

          <!-- 悬停效果 -->
          <div class="article-overlay">
            <span class="read-more">阅读全文 →</span>
          </div>
        </article>

        <!-- 空状态 -->
        <div v-if="articles.length === 0" class="empty-state">
          <span class="empty-icon">📄</span>
          <p class="empty-text">暂无文章</p>
        </div>
      </div>

      <!-- 分页组件 -->
      <nav v-if="total > 0" class="pagination-wrapper">
        <div class="pagination">
          <!-- 当处于第 1 页时 禁用按钮 （防止向前翻页） -->
          <!-- 点击时调用方法，传入 当前页码 - 1 作为新页码 -->
          <button 
            class="page-btn prev-btn" 
            :disabled="currentPage === 1"
            @click="fetchArticles(currentPage - 1)"
          >
            ←
          </button>
          
          <!-- 当前页码 / 总页数  -->
          <span class="page-info">
            第 {{ currentPage }} / {{ totalPages }} 页
          </span>

          <!-- 当处于最后一页时 禁用按钮 （防止向后翻页） -->
          <!-- 点击时调用方法，传入 当前页码 + 1 作为新页码 -->
          <button 
            class="page-btn next-btn" 
            :disabled="currentPage === totalPages"
            @click="fetchArticles(currentPage + 1)"
          >
            →
          </button>

        </div>
        <div class="total-count">
          共 {{ total }} 篇文章
        </div>
      </nav>
    </main>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'ArticleList',
  data() {
    return {
      articles: [],
      currentPage: 1,
      pageSize: 6,
      total: 0
    }
  },
  computed: {
    totalPages() {
      // 总页数 = 向上取整(文章总数 / 每页条数)
      return Math.ceil(this.total / this.pageSize)
    }
  },
  methods: {
    async fetchArticles(page = 1) {
      try {
        const response = await request({
          url: '/public/article/list',
          method: 'post',
          data: {
            pageNum: page,
            pageSize: this.pageSize
          }
        })
        
        // TableDataInfo 格式：{ rows: [], total: 0 }
        if (response.rows && response.total !== undefined) {
          this.articles = response.rows
          this.total = response.total
          this.currentPage = page
        }
      } catch (error) {
        console.error('获取文章列表失败:', error)
      }
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
    //todo 修改：添加图片加载失败处理（懒加载）
    handleCoverError(e) {
      // 获取触发错误的图片元素
      const img = e.target
      // 检查图片元素是否存在
      if (!img) return
      
      // 隐藏图片
      img.style.display = 'none'
      
      // 安全查找 placeholder
      // 获取图片的父容器
      const parent = img.parentElement
      // 如果父容器存在
      if (parent) {
        // 查找父容器中的 placeholder 元素
        const placeholder = parent.querySelector('.cover-placeholder')
        if (placeholder) {
          // 显示 placeholder 元素
          placeholder.style.display = 'flex'
        }
      }
    }
  },
  mounted() {
    this.fetchArticles(1)
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
  padding: 40px 20px;
}

.article-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
  justify-items: center;
}

.article-card {
  width: 100%;
  max-width: 360px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.article-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.25);
}

/* 封面容器 */
.article-cover {
  width: 100%;
  height: 200px;
  position: relative;
  /* 隐藏超出部分 */
  overflow: hidden;
}

/* 封面图片 */
.cover-image {
  width: 100%;
  height: 100%;
  /* 图片等比例缩放，保持原始比例 */
  object-fit: cover;
  transition: transform 0.5s ease;
}

.article-card:hover .cover-image {
  /* 鼠标悬停时，图片缩放 1.1 倍 */
  transform: scale(1.1);
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #4a5568 0%, #2d3748 100%);
  display: none;
}

.placeholder-icon {
  font-size: 3rem;
  opacity: 0.5;
}

.article-body {
  padding: 20px;
}

.article-meta {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.category-tag {
  padding: 4px 12px;
  background: linear-gradient(135deg, #4a90c2 0%, #2563eb 100%);
  color: #ffffff;
  font-size: 0.75rem;
  font-weight: 500;
  border-radius: 12px;
}

.status-tag {
  padding: 4px 12px;
  font-size: 0.75rem;
  font-weight: 500;
  border-radius: 12px;
}

.status-tag.published {
  background: rgba(34, 197, 94, 0.15);
  color: #22c55e;
}

.status-tag.draft {
  background: rgba(209, 213, 219, 0.5);
  color: #6b7280;
}

.article-title {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 12px;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.article-date {
  font-family: 'Roboto', 'Helvetica Neue', sans-serif;
  font-size: 0.875rem;
  color: #9ca3af;
  margin: 0;
}

.article-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.6), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
  display: flex;
  align-items: flex-end;
}

.article-card:hover .article-overlay {
  opacity: 1;
}

.read-more {
  font-family: 'Noto Serif SC', 'Georgia', serif;
  font-size: 0.9rem;
  font-weight: 500;
  color: #ffffff;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.5);
}

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
  font-size: 1.2rem;
  color: rgba(255, 255, 255, 0.5);
  margin-top: 16px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 30px;
  margin-top: 40px;
}

.pagination {
  display: flex;
  align-items: center;
  gap: 16px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  padding: 12px 24px;
  border-radius: 30px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.page-btn {
  width: 40px;
  height: 40px;
  border: none;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  color: #ffffff;
  font-size: 1.2rem;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  justify-content: center;
  align-items: center;
}

.page-btn:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
}

.page-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.page-info {
  font-family: 'Roboto', 'Helvetica Neue', sans-serif;
  font-size: 1rem;
  color: #ffffff;
  font-weight: 500;
}

.total-count {
  font-family: 'Roboto', 'Helvetica Neue', sans-serif;
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.6);
}

/* 响应式设计 */
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
  
  .main-content {
    padding: 24px 16px;
  }
  
  .article-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .article-card {
    max-width: 100%;
  }
  
  .article-cover {
    height: 180px;
  }
  
  .pagination-wrapper {
    flex-direction: column;
    gap: 16px;
  }
  
  .pagination {
    padding: 10px 20px;
  }
  
  .page-btn {
    width: 36px;
    height: 36px;
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .article-body {
    padding: 16px;
  }
  
  .article-title {
    font-size: 1.1rem;
  }
  
  .article-cover {
    height: 150px;
  }
}
</style>