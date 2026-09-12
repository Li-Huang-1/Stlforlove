<template>
  <div class="admin-container">
    <aside class="sidebar">
      <div class="sidebar-header">
        <div class="sidebar-logo">
          <span class="logo-icon">🎨</span>
          <span class="logo-text">管理系统</span>
        </div>
      </div>
      
      <nav class="sidebar-menu">
        <div 
          v-for="item in menuItems" 
          :key="item.id"
          class="menu-item"
          :class="{ 'active': activeMenu === item.id }"
          @click="activeMenu = item.id"
        >
          <span class="menu-icon">{{ item.icon }}</span>
          <span class="menu-text">{{ item.label }}</span>
        </div>
      </nav>
      
      <div class="sidebar-footer">
        <button class="logout-btn" @click="handleLogout">
          <span class="logout-icon">🚪</span>
          <span>退出登录</span>
        </button>
      </div>
    </aside>
    
    <main class="main-content">
      <header class="content-header">
        <div class="header-left">
          <h2 class="page-title">{{ currentPageTitle }}</h2>
          <p class="page-subtitle">{{ currentPageSubtitle }}</p>
        </div>
        <div class="header-right">
          <div class="user-info">
            <img 
              :src="userAvatar || require('@/assets/默认头像.png')" 
              class="user-avatar" 
              alt="用户头像"
            />
            <span class="user-name">管理员</span>
          </div>
        </div>
      </header>
      
      <div class="content-body">
        <div class="stats-grid">
          <div class="stat-card">
            <div class="stat-icon blue">📝</div>
            <div class="stat-info">
              <div class="stat-value">{{ articleCount }}</div>
              <div class="stat-label">文章数量</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon green">👥</div>
            <div class="stat-info">
              <div class="stat-value">{{ visitorCount }}</div>
              <div class="stat-label">访客总数</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon orange">❤️</div>
            <div class="stat-info">
              <div class="stat-value">0</div>
              <div class="stat-label">点赞数</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon purple">💬</div>
            <div class="stat-info">
              <div class="stat-value">0</div>
              <div class="stat-label">评论数</div>
            </div>
          </div>
        </div>
        
        <div class="content-section">
          <div class="section-header">
            <h3 class="section-title">最近文章</h3>
            <button class="add-btn" @click="handleAddArticle">+ 新增文章</button>
          </div>
          
          <div class="article-table">
            <table>
              <thead>
                <tr>
                  <th>标题</th>
                  <th>分类</th>
                  <th>发布时间</th>
                  <th>状态</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="recentArticles.length === 0">
                  <td colspan="5" style="text-align: center; padding: 40px; color: #999;">
                    📭 暂无文章，点击右上角按钮添加
                  </td>
                </tr>
                <tr v-for="article in recentArticles" :key="article.id">
                  <td class="article-title">{{ article.title }}</td>
                  <td><span class="category-tag">{{ article.category }}</span></td>
                  <td>{{ article.createTime }}</td>
                  <td><span :class="['status-tag', article.status]">{{ article.status }}</span></td>
                  <td class="actions">
                    <!-- 由于渲染是v-for循环渲染，所以实际点击按钮时就已经获取到当前文章的示例数据，
                     接着将当前文章的示例数据传递给编辑文章函数，即可完成对应操作 -->
                    <button class="action-btn edit" @click="handleEditArticle(article)">✏️</button>
                    <button class="action-btn delete" @click="deleteArticle(article)">🗑️</button>
                    <button class="action-btn search" @click="handleCheckArticle(article)">🔍</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        
        <div class="content-section">
          <div class="section-header">
            <h3 class="section-title">系统设置</h3>
          </div>
          
          <div class="settings-form">
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">网站标题</label>
                <input type="text" class="form-input" value="StLforLove" />
              </div>
              <div class="form-group">
                <label class="form-label">网站描述</label>
                <input type="text" class="form-input" value="个人技术博客" />
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">ICP备案号</label>
                <input type="text" class="form-input" value="闽ICP备2026002991号-1" />
              </div>
              <div class="form-group">
                <label class="form-label">公安备案号</label>
                <input type="text" class="form-input" value="闽公网安备35058102000458号" />
              </div>
            </div>
            <button class="save-btn">保存设置</button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { articleApi, userApi, visitorApi } from '@/api'

export default {
  name: 'AdminPanel',
  data() {
    return {
      activeMenu: 'dashboard',
      menuItems: [
        { id: 'dashboard', icon: '📊', label: '数据统计' },
        { id: 'articles', icon: '📝', label: '文章管理' },
        { id: 'comments', icon: '💬', label: '评论管理' },
        { id: 'settings', icon: '⚙️', label: '系统设置' }
      ],
      recentArticles: [],
      userAvatar: '',
      articleCount: 0,
      visitorCount: 1
    }
  },
  computed: {
    currentPageTitle() {
      const titles = {
        dashboard: '数据统计',
        articles: '文章管理',
        comments: '评论管理',
        settings: '系统设置'
      }
      return titles[this.activeMenu] || '数据统计'
    },
    currentPageSubtitle() {
      const subtitles = {
        dashboard: '查看网站数据概览',
        articles: '管理您的文章内容',
        comments: '管理用户评论',
        settings: '配置网站参数'
      }
      return subtitles[this.activeMenu] || ''
    }
  },
  methods: {
    async fetchUserInfo() {
      try {
        const res = await userApi.getInfo()
        if (res.avatar) {
          this.userAvatar = res.avatar
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
      }
    },
    handleAddArticle() {
      this.$router.push('/admin/article/new')
    },
    handleEditArticle(article) {
      this.$router.push('/admin/article/edit/' + article.id)
    },
    handleLogout() {
      sessionStorage.removeItem('isLoggedIn')
      this.$emit('logout')
      this.$router.push('/')
    },
    handleCheckArticle(article) {
      this.$router.push('/article/' + article.id)
    },
    async fetchArticles() {
      try {
        const res = await articleApi.getList({
          pageNum: 1,
          pageSize: 4,
          includeDraft: true
        })
        // 后端返回格式为 { rows: [] }，取 rows 作为文章列表
        this.recentArticles = res.rows || []
        this.recentArticles.forEach(article => {
          article.createTime = new Date(article.createTime).toLocaleDateString('zh-CN')
        })
        this.recentArticles.forEach(article => {
          article.status = article.status === 1 ? '已发布' : '草稿'
        })
      } catch (error) {
        console.error('加载文章失败:', error)
      }
    },
    async deleteArticle(article) {
      try {
        await articleApi.remove(article.id)
        // 再次刷新文章列表，确保删除后的文章列表是最新状态
        this.fetchArticles()
        // $message 是 Quasar 提供的消息提示组件，用于显示成功、错误等消息
        this.$message({
          message: '删除成功',
          type: 'success',
          showClose: true
        })
        this.$router.push('/admin')
      } catch (error) {
        console.error('删除失败:', error)
        this.$message({
          message: '删除失败',
          type: 'error',
          showClose: true
        })
      }
    },
    async loadArticleCount() {
      try {
        const data = await articleApi.getCount()
        this.articleCount = data || 0
      } catch (error) {
        console.error('获取文章总数失败:', error)
        this.articleCount = 0
      }
    },
    async loadVisitorCount() {
      try {
        // 检查当前浏览器会话是否已经计数
        const hasCounted = sessionStorage.getItem('visitor_counted')
        
        if (hasCounted) {
          // 已经计数过，只获取当前计数
          const data = await visitorApi.getCount()
          this.visitorCount = data?.count || 1
        } else {
          // 首次访问，增加计数并标记
          const data = await visitorApi.increment()
          this.visitorCount = data?.count || 1
          // 标记已计数
          sessionStorage.setItem('visitor_counted', 'true')
        }
      } catch (error) {
        console.error('获取访客数失败:', error)
        this.visitorCount = 1
      }
    },
  },
 mounted() {
    this.fetchArticles()
    // 调用接口获取用户信息（包含头像）
    this.fetchUserInfo()
    this.loadArticleCount()
    this.loadVisitorCount()
  }
}
</script>

<style scoped>
.admin-container {
  display: flex;
  min-height: 100vh;
  background: linear-gradient(180deg, #e8f0fe 0%, #f8fafc 100%);
}

.sidebar {
  width: 240px;
  background: linear-gradient(180deg, #1e3a5f 0%, #0d2137 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  z-index: 100;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 24px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
}

.sidebar-menu {
  flex: 1;
  padding: 16px;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 4px;
}

.menu-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.menu-item.active {
  background: linear-gradient(135deg, #4a90c2 0%, #2563eb 100%);
}

.menu-icon {
  font-size: 18px;
}

.menu-text {
  font-size: 14px;
  font-weight: 500;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.logout-btn {
  width: 100%;
  padding: 12px;
  background: rgba(231, 76, 60, 0.2);
  border: 1px solid rgba(231, 76, 60, 0.3);
  border-radius: 8px;
  color: #e74c3c;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.logout-btn:hover {
  background: rgba(231, 76, 60, 0.3);
}

.main-content {
  flex: 1;
  margin-left: 240px;
  display: flex;
  flex-direction: column;
}

.content-header {
  background: #fff;
  padding: 20px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.page-subtitle {
  font-size: 13px;
  color: #888;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 16px;
  background: #f5f7fa;
  border-radius: 20px;
}

.user-avatar {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.content-body {
  padding: 30px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
}

.stat-icon.blue {
  background: rgba(37, 99, 235, 0.1);
}

.stat-icon.green {
  background: rgba(16, 185, 129, 0.1);
}

.stat-icon.orange {
  background: rgba(245, 158, 11, 0.1);
}

.stat-icon.purple {
  background: rgba(99, 102, 241, 0.1);
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #333;
}

.stat-label {
  font-size: 13px;
  color: #888;
}

.content-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.add-btn {
  padding: 8px 20px;
  background: linear-gradient(135deg, #4a90c2 0%, #2563eb 100%);
  border: none;
  border-radius: 20px;
  color: #fff;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.4);
}

.article-table {
  overflow-x: auto;
}

.article-table table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

.article-table th,
.article-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
  vertical-align: middle;
}

.article-table th {
  font-size: 13px;
  font-weight: 600;
  color: #666;
  background: #fafafa;
}

.article-table th:nth-child(1) { width: 40%; min-width: 180px; }
.article-table th:nth-child(2) { width: 12%; min-width: 80px; }
.article-table th:nth-child(3) { width: 20%; min-width: 140px; }
.article-table th:nth-child(4) { width: 10%; min-width: 70px; }
.article-table th:nth-child(5) { width: 18%; min-width: 120px; }

.article-table tbody tr:hover {
  background: #fafafa;
}

.article-title {
  font-size: 14px;
  color: #333;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 300px;
  cursor: pointer;
  transition: color 0.2s ease;
}

.article-title:hover {
  color: #2563eb;
}

.category-col {
  white-space: nowrap;
}

.time-col {
  font-size: 13px;
  color: #999;
  white-space: nowrap;
}

.status-col {
  white-space: nowrap;
}

.actions-col {
  white-space: nowrap;
}

.category-tag {
  background: rgba(37, 99, 235, 0.1);
  color: #2563eb;
  border-radius: 12px;
  font-size: 16px;
}

.status-tag {
  border-radius: 12px;
  font-size: 16px;
}

.status-tag.published {
  background: rgba(46, 213, 115, 0.1);
  color: #2ed573;
}

.status-tag.draft {
  background: rgba(249, 115, 22, 0.1);
  color: #f97316;
}

.actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
}

.action-btn.edit {
  background: rgba(37, 99, 235, 0.1);
}

.action-btn.edit:hover {
  background: rgba(37, 99, 235, 0.2);
}

.action-btn.delete {
  background: rgba(231, 76, 60, 0.1);
}

.action-btn.delete:hover {
  background: rgba(231, 76, 60, 0.2);
}

.action-btn.search {
  background: rgba(255, 196, 0, 0.1);
}

.action-btn.search:hover {
  background: rgba(255, 196, 0, 0.2);
}

.settings-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.settings-form .form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.settings-form .form-label {
  font-size: 14px;
  font-weight: 600;
  color: #555;
}

.settings-form .form-input {
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.settings-form .form-input:focus {
  outline: none;
  border-color: #667eea;
}

.save-btn {
  align-self: flex-end;
  padding: 12px 32px;
  background: linear-gradient(135deg, #4a90c2 0%, #2563eb 100%);
  border: none;
  border-radius: 8px;
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.save-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

@media (max-width: 768px) {
  .sidebar {
    width: 60px;
  }
  
  .logo-text,
  .menu-text,
  .logout-btn span:last-child {
    display: none;
  }
  
  .sidebar-logo {
    justify-content: center;
  }
  
  .menu-item {
    justify-content: center;
  }
  
  .logout-btn {
    justify-content: center;
  }
  
  .main-content {
    margin-left: 60px;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>