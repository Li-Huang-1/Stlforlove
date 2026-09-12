<template>
  <div class="article-editor">
    <div class="editor-header">
      <h1 class="editor-title">{{ isEdit ? '编辑文章' : '文章编辑器' }}</h1>
      <p v-if="isEdit" class="editor-subtitle">正在编辑文章 ID: {{ articleId }}</p>
    </div>
    <div class="editor-body">
      <div class="editor-main">
        <div class="title-input-wrapper">
          <input 
            v-model="title" 
            placeholder="请输入文章标题..." 
            class="title-input"
          />
        </div>
        
        <div class="editor-wrapper">
          <QuillEditor ref="editorRef" />
        </div>
        
        <div class="editor-footer">
          <button class="btn btn-primary" @click="saveArticle">
            <span>{{ isEdit ? '更新文章' : '保存文章' }}</span>
          </button>
        </div>
      </div>
      
      <div class="property-panel" :class="{ collapsed: !isPanelExpanded }">
        <div class="panel-header" @click="togglePanel">
          <span class="panel-title">文章属性</span>
          <span class="panel-toggle">{{ isPanelExpanded ? '▼' : '▶' }}</span>
        </div>
         <div class="panel-content" v-show="isPanelExpanded">
          <div class="panel-section cover-section">
            <label class="section-label">文章封面</label>
            <!-- 封面上传区域 @dragover.prevent阻止默认行为，允许放置文件 -->
            <div class="cover-upload-area" @click="triggerFileInput" @dragover.prevent @drop="handleDrop">
              <div class="cover-preview" v-if="coverUrl">
                <img :src="coverUrl" alt="封面预览" class="cover-image" />
                <button class="remove-cover-btn" @click.stop="removeCover">×</button>
              </div>
              <div class="cover-placeholder" v-else>
                <img src="@/assets/上传.png" alt="上传图标" class="upload-icon" />
                <span class="upload-text">点击或拖拽上传封面</span>
                <span class="upload-hint">支持 JPG、PNG，最大 10MB</span>
              </div>
            </div>
            <div class="upload-status" v-show="uploadStatus">
              <span :class="['status-icon', uploadStatus]">
                {{ uploadStatus === 'uploading' ? '⏳' : uploadStatus === 'success' ? '✅' : '❌' }}
              </span>
              <span class="status-text">
                {{ uploadStatus === 'uploading' ? `上传中 ${uploadProgress}%` : uploadStatus === 'success' ? '上传成功' : '上传失败，点击重试' }}
              </span>
            </div>
            <input type="file" ref="coverFileInput" class="cover-file-input" accept="image/jpeg,image/jpg,image/png" @change="handleFileSelect" />
          </div>
          
          <div class="panel-section">
            <label class="section-label">文章状态</label>
            <div class="status-options">
              <button 
                class="status-btn" 
                :class="{ active: articleStatus === '草稿' }"
                @click="articleStatus = '草稿'"
              >
                草稿
              </button>
              <button 
                class="status-btn" 
                :class="{ active: articleStatus === '已发布' }"
                @click="articleStatus = '已发布'"
              >
                已发布
              </button>
            </div>
            <span class="status-hint">当前状态：{{ articleStatus }}</span>
          </div>
          
          <div class="panel-section">
            <label class="section-label">文章类型</label>
            <div class="type-options">
              <button 
                v-for="t in articleTypes" 
                :key="t.value"
                class="type-btn"
                :class="{ active: selectedType === t.value }"
                @click="selectedType = t.value"
              >
                {{ t.label }}
              </button>
            </div>
            <span class="type-hint">当前类型：{{ getTypeLabel(selectedType) }}</span>
          </div>

          <div class="panel-section">
            <label class="section-label">文章分类</label>
            <input 
              v-model="selectedCategory"
              type="text" 
              placeholder="输入自定义分类..." 
              class="category-input"
            />
            <div class="category-options">
              <button 
                v-for="cat in categories" 
                :key="cat.value"
                class="category-btn"
                :class="{ active: selectedCategory === cat.value }"
                @click="selectedCategory = cat.value"
              >
                {{ cat.label }}
              </button>
            </div>
            <span class="category-hint">当前分类：{{ selectedCategory || '未设置' }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import QuillEditor from '@/components/QuillEditor.vue'
import { articleApi, uploadApi } from '@/api'

const route = useRoute()
const router = useRouter()

const editorRef = ref(null)
const title = ref('')
const articleId = ref(null)

const isPanelExpanded = ref(true)
const articleStatus = ref('草稿')
const selectedCategory = ref('技术')
const selectedType = ref('tech')

const articleTypes = ref([
  { label: '技术栈', value: 'tech' },
  { label: '项目', value: 'project' },
  { label: '日志', value: 'log' }
])

const getTypeLabel = (value) => {
  const found = articleTypes.value.find(t => t.value === value)
  return found ? found.label : '未分类'
}

// 封面上传相关状态
const coverUrl = ref('')           // 封面图片URL
const coverFileInput = ref(null)   // 隐藏的文件输入框DOM引用
const uploadStatus = ref('')       // 上传状态: ''|'uploading'|'success'|'error'
const uploadProgress = ref(0)      // 上传进度百分比(0-100)

const categories = ref([
  { label: '技术', value: '技术' },
  { label: '生活', value: '生活' },
  { label: '随笔', value: '随笔' },
  { label: '读书', value: '读书' },
  { label: '测试', value: '测试' }
])

const isEdit = computed(() => {
  return route.params.id !== undefined
})

const togglePanel = () => {
  isPanelExpanded.value = !isPanelExpanded.value
}

// 触发文件选择对话框（模拟点击隐藏的input）
const triggerFileInput = () => {
  if (uploadStatus.value === 'uploading') return  // 上传中禁止重复触发
  coverFileInput.value?.click()                    // 调用input的原生click事件
}

// 文件验证：格式(JPG/PNG) + 大小(10MB)
const validateFile = (file) => {
  const validTypes = ['image/jpeg', 'image/jpg', 'image/png']
  if (!validTypes.includes(file.type)) {
    return '仅支持 JPG、PNG 格式的图片'
  }
  if (file.size > 10 * 1024 * 1024) {  // 10MB = 10 * 1024KB * 1024B
    return '图片大小不能超过 10MB'
  }
  return null  // 验证通过
}

// 处理文件选择（点击上传）
const handleFileSelect = async (event) => { // event为@change事件的对象
  // event.target 触发事件的 DOM 元素（即 <input> ） 
  // event.target.files 用户选择的文件列表（FileList） event.target.files[0] 获取第一个文件
  const file = event.target.files?.[0]
  if (!file) return
  
  await uploadCover(file)
  event.target.value = ''  // 重置input值，允许重复选择同一文件
}

// 处理文件拖拽上传
const handleDrop = async (event) => {
  const file = event.dataTransfer?.files?.[0]
  if (!file) return
  
  await uploadCover(file)
}

// 执行封面上传（核心逻辑）
const uploadCover = async (file) => {
  // 1. 文件验证
  const error = validateFile(file)
  if (error) {
    uploadStatus.value = 'error'
    setTimeout(() => { uploadStatus.value = '' }, 3000)
    return
  }
  
  // 2. 设置上传状态
  uploadStatus.value = 'uploading'
  uploadProgress.value = 0
  
  try {
    // 3. 构造FormData（文件上传标准格式）
    const formData = new FormData()
    formData.append('image', file)
    
    // 4. 发送上传请求（含进度回调）
    // onUploadProgress：axios 监听浏览器的上传进度事件，回调更新 uploadProgress
    const data = await uploadApi.uploadImage(formData, (progressEvent) => {
      if (progressEvent.total) {
        uploadProgress.value = Math.round((progressEvent.loaded / progressEvent.total) * 100)
      }
    })
    
    // 5. 解析返回的图片URL（兼容多种返回格式）
    const imageUrl = data.url || data.data?.url || data
    if (!imageUrl) {
      throw new Error('上传失败，未返回图片URL')
    }
    
    // 6. 更新封面URL，显示成功状态
    coverUrl.value = imageUrl
    uploadStatus.value = 'success'
    setTimeout(() => { uploadStatus.value = '' }, 2000)
  } catch (err) {
    // 7. 上传失败，显示错误状态
    uploadStatus.value = 'error'
    console.error('封面上传失败:', err)
  }
}

// 删除封面
const removeCover = () => {
  coverUrl.value = ''       // 清空封面URL，恢复占位符
  uploadStatus.value = ''   // 清空上传状态
}

const loadArticle = async () => {
  if (!isEdit.value) return
  
  articleId.value = parseInt(route.params.id)
  
  try {
    const res = await articleApi.getDetail(articleId.value)
    
    if (res) {
      title.value = res.title || ''
      if (res.content) {
        editorRef.value?.setContent(res.content)
      }
      if (res.status !== undefined) {
        articleStatus.value = res.status === 0 ? '草稿' : '已发布'
      }
      if (res.category) {
        selectedCategory.value = res.category
      }
      if (res.type) {
        selectedType.value = res.type
      }
      if (res.coverUrl) {
        coverUrl.value = res.coverUrl
      }
    }
  } catch (error) {
    console.error('加载文章失败:', error)
    alert('加载文章失败，请稍后重试')
  }
}

onMounted(() => {
  loadArticle()
})

const saveArticle = async () => {
  const content = editorRef.value?.getContent?.() || editorRef.value?.content?.value
  const articleTitle = title.value
  
  if (!articleTitle.trim()) {
    alert('请输入文章标题')
    return
  }
  if (!content || content === '<p><br></p>' || content === '<p>欢迎来到我的博客！开始写作吧...</p>') {
    alert('请输入文章内容')
    return
  }
  
  try {
    const statusValue = articleStatus.value === '草稿' ? 0 : 1

    const data = {
      title: articleTitle,
      content: content,
      type: selectedType.value,
      status: statusValue,
      category: selectedCategory.value,
      coverUrl: coverUrl.value
    }

    if (isEdit.value) {
      await articleApi.update({ id: articleId.value, ...data })
    } else {
      await articleApi.add(data)
    }

    alert(isEdit.value ? '✅ 更新成功！' : '✅ 保存成功！')
    
    router.push('/admin')
    
  } catch (error) {
    console.error('保存失败:', error)
    alert('❌ 保存失败，请稍后重试')
  }
}
</script>

<style scoped>
.article-editor {
  min-height: 100vh;
  background: linear-gradient(180deg, #e8f0fe 0%, #f8fafc 100%);
  padding: 40px 20px;
}

.editor-header {
  text-align: center;
  margin-bottom: 32px;
}

.editor-title {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 8px;
}

.editor-subtitle {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.editor-body {
  max-width: 1000px;
  margin: 0 auto;
}

.title-input-wrapper {
  margin-bottom: 20px;
}

.title-input {
  width: 100%;
  padding: 16px 20px;
  font-size: 20px;
  font-weight: 600;
  border: 2px solid rgba(37, 99, 235, 0.15);
  border-radius: 12px;
  background: white;
  color: #1e293b;
  outline: none;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.title-input:focus {
  border-color: rgba(37, 99, 235, 0.4);
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.08);
}

.title-input::placeholder {
  color: #94a3b8;
  font-weight: 400;
}

.editor-wrapper {
  margin-bottom: 24px;
}

.editor-footer {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.btn {
  padding: 12px 24px;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-primary {
  background: linear-gradient(135deg, #4a90c2 0%, #2563eb 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(37, 99, 235, 0.4);
}

.btn-icon {
  font-size: 16px;
}

.property-panel {
  position: fixed;
  right: 20px;
  top: 120px;
  width: 280px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(37, 99, 235, 0.08);
  border: 1px solid rgba(37, 99, 235, 0.1);
  transition: all 0.3s ease;
  overflow: hidden;
  z-index: 100;
}

.property-panel.collapsed {
  width: 40px;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: linear-gradient(135deg, rgba(37, 99, 235, 0.05) 0%, rgba(74, 144, 194, 0.05) 100%);
  border-bottom: 1px solid rgba(37, 99, 235, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
}

.property-panel.collapsed .panel-header {
  justify-content: center;
  padding: 12px;
}

.panel-header:hover {
  background: linear-gradient(135deg, rgba(37, 99, 235, 0.08) 0%, rgba(74, 144, 194, 0.08) 100%);
}

.panel-title {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
}

.panel-toggle {
  font-size: 12px;
  color: #64748b;
  transition: transform 0.3s ease;
}

.property-panel.collapsed .panel-title {
  display: none;
}



.panel-content {
  padding: 16px;
  animation: slideDown 0.3s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.panel-section {
  margin-bottom: 20px;
}

.panel-section:last-child {
  margin-bottom: 0;
}

.cover-section {
  margin-bottom: 20px;
}

.cover-upload-area {
  position: relative;
  width: 100%;
  height: 140px;
  border: 2px dashed rgba(148, 163, 184, 0.4);
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.cover-upload-area:hover {
  border-color: rgba(37, 99, 235, 0.4);
  background: rgba(37, 99, 235, 0.02);
}

.cover-upload-area.dragover {
  border-color: rgba(37, 99, 235, 0.6);
  background: rgba(37, 99, 235, 0.05);
}

.cover-preview {
  position: relative;
  width: 100%;
  height: 100%;
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-cover-btn {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 24px;
  height: 24px;
  border: none;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  font-size: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s ease;
}

.remove-cover-btn:hover {
  background: rgba(239, 68, 68, 0.8);
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.upload-icon {
  width: 40px;
  height: 40px;
  object-fit: contain;
}

.upload-text {
  font-size: 14px;
  color: #64748b;
  font-weight: 500;
}

.upload-hint {
  font-size: 12px;
  color: #94a3b8;
}

.cover-file-input {
  display: none;
}

.upload-status {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 10px;
  padding: 8px 12px;
  border-radius: 8px;
  font-size: 13px;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.upload-status.success {
  background: rgba(34, 197, 94, 0.1);
  color: #16a34a;
}

.upload-status.error {
  background: rgba(239, 68, 68, 0.1);
  color: #dc2626;
  cursor: pointer;
}

.upload-status.error:hover {
  background: rgba(239, 68, 68, 0.15);
}

.upload-status.uploading {
  background: rgba(37, 99, 235, 0.1);
  color: #2563eb;
}

.status-icon {
  font-size: 14px;
}

.status-text {
  flex: 1;
}

.section-label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: #475569;
  margin-bottom: 10px;
}

.status-options {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.status-btn {
  flex: 1;
  padding: 10px 12px;
  font-size: 13px;
  font-weight: 500;
  border: 1px solid rgba(148, 163, 184, 0.3);
  border-radius: 8px;
  background: white;
  color: #64748b;
  cursor: pointer;
  transition: all 0.2s ease;
}

.status-btn:hover {
  border-color: rgba(37, 99, 235, 0.3);
  color: #2563eb;
}

.status-btn.active {
  background: linear-gradient(135deg, rgba(37, 99, 235, 0.1) 0%, rgba(74, 144, 194, 0.1) 100%);
  border-color: rgba(37, 99, 235, 0.4);
  color: #2563eb;
}

.status-hint {
  font-size: 12px;
  color: #94a3b8;
}

.category-input {
  width: 100%;
  padding: 10px 12px;
  font-size: 13px;
  border: 1px solid rgba(148, 163, 184, 0.3);
  border-radius: 8px;
  background: white;
  color: #1e293b;
  outline: none;
  transition: all 0.2s ease;
  margin-bottom: 10px;
  box-sizing: border-box;
}

.category-input:focus {
  border-color: rgba(37, 99, 235, 0.4);
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.08);
}

.category-input::placeholder {
  color: #94a3b8;
}

.category-options {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 8px;
}

.category-btn {
  padding: 8px 14px;
  font-size: 12px;
  font-weight: 500;
  border: 1px solid rgba(148, 163, 184, 0.3);
  border-radius: 20px;
  background: white;
  color: #64748b;
  cursor: pointer;
  transition: all 0.2s ease;
}

.category-btn:hover {
  border-color: rgba(37, 99, 235, 0.3);
  color: #2563eb;
}

.category-btn.active {
  background: linear-gradient(135deg, rgba(37, 99, 235, 0.15) 0%, rgba(74, 144, 194, 0.15) 100%);
  border-color: rgba(37, 99, 235, 0.4);
  color: #2563eb;
}

.category-hint {
  font-size: 12px;
  color: #94a3b8;
}

.type-options {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.type-btn {
  flex: 1;
  padding: 10px 12px;
  font-size: 13px;
  font-weight: 500;
  border: 1px solid rgba(148, 163, 184, 0.3);
  border-radius: 8px;
  background: white;
  color: #64748b;
  cursor: pointer;
  transition: all 0.2s ease;
}

.type-btn:hover {
  border-color: rgba(37, 99, 235, 0.3);
  color: #2563eb;
}

.type-btn.active {
  background: linear-gradient(135deg, rgba(37, 99, 235, 0.15) 0%, rgba(74, 144, 194, 0.15) 100%);
  border-color: rgba(37, 99, 235, 0.4);
  color: #2563eb;
}

.type-hint {
  font-size: 12px;
  color: #94a3b8;
}

.panel-minimized {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px 0;
}

.minimized-icon {
  font-size: 20px;
  opacity: 0.5;
}

@media (max-width: 900px) {
  .editor-body {
    padding-right: 0;
  }
  
  .property-panel {
    position: static;
    width: 100%;
    margin-top: 20px;
  }
  
  .property-panel.collapsed {
    width: 100%;
  }
  
  .property-panel.collapsed .panel-title {
    display: inline;
  }
  
  .property-panel.collapsed .panel-toggle {
    display: inline;
  }
  
  .property-panel.collapsed .panel-header {
    justify-content: space-between;
    padding: 12px 16px;
  }
  
  .panel-minimized {
    display: none;
  }
}
</style>