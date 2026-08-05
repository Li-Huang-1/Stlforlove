<template>
  <div class="editor-wrapper">
    <QuillEditor
      ref="quillEditorRef"
      v-model:content="content"
      content-type="html"
      theme="snow"
      :options="editorOptions"
      @update:content="handleContentChange"
      @ready="handleEditorReady"
    />
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount } from 'vue'
import { Quill, QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import request from '@/utils/request'

import hljs from 'highlight.js'
import 'highlight.js/styles/github-dark.css'

// 将 hljs 挂载到全局，供 Quill syntax 模块查找
if (typeof window !== 'undefined') {
  window.hljs = hljs
}

import BlotFormatter from '@enzedonline/quill-blot-formatter2'
import '@enzedonline/quill-blot-formatter2/dist/css/quill-blot-formatter2.css'

// 在组件加载时立即注册 BlotFormatter，确保编辑器初始化时就能使用
Quill.register('modules/blotFormatter', BlotFormatter)

const content = ref('<p>欢迎来到我的博客！开始写作吧...</p>')
const quillEditorRef = ref(null)
let quillInstance = null

// editor是一个Quill实例，用于操作编辑器
const handleEditorReady = (editor) => {
  // 初始化编辑器实例
  quillInstance = editor
  // 设置 highlight.js，用于代码块语法高亮
  editor.highlight = hljs.highlightElement
  // 使用捕获阶段监听粘贴事件，确保先于 Quill 内部处理执行
  editor.root.addEventListener('paste', handlePaste, true)
  // 监听文本变化以触发代码块高亮
  editor.on('text-change', highlightCodeBlocks)
  // 监听语言选择框变化，实现每个代码块独立语言选择
  // <select>Dom 可以触发change事件，事件冒泡到父元素，最后触发editor.root的change事件
  // 所以这里需要监听editor.root的change事件，而不是直接监听<select>的change事件
  editor.root.addEventListener('change', handleLanguageChange)
  // 监听鼠标悬停事件，显示代码块的语言选择框
  editor.root.addEventListener('mouseover', handleMouseOver)
  editor.root.addEventListener('mouseout', handleMouseOut)
}

// 防抖定时器<(300ms)>(防抖：延迟执行，如果频繁触发，只执行最后一次。）
let highlightTimer = null
// 高亮中标志，防止无限循环
let isHighlighting = false

const highlightCodeBlocks = () => {
  if (!quillInstance || isHighlighting) return
  
  // 使用防抖，避免每次按键都触发高亮
  if (highlightTimer) {
    // 清除之前的定时器，避免重复触发高亮
    clearTimeout(highlightTimer)
  }
  // setTimeout语法：
  
  // setTimeout(() => {
  // 要执行的代码
  // }, 300)

  // highlightTimer 定时器id，用于取消定时器
  // setTimeout() 返回一个数字，表示定时器的唯一标识
  highlightTimer = setTimeout(() => {
    isHighlighting = true
    try {
      // 1. 先保存所有代码块的文本内容，防止 Quill 重新同步时覆盖
      const containers = quillInstance.root.querySelectorAll('.ql-code-block-container')
      const codeBlocksData = []
      
      containers.forEach((container) => {
        const select = container.querySelector('select')
        const codeBlock = container.querySelector('.ql-code-block')
        if (!codeBlock) return
        
        // 保存每个代码块的引用、语言和文本内容
        // .push 添加一个或多个元素。
        codeBlocksData.push({
          // 预留，将来可能用到
          container,
          codeBlock,
          language: select?.value || '',
          code: codeBlock.textContent || ''
        })
      })
      
      // 2. 逐个处理每个代码块，使用保存的文本内容
      codeBlocksData.forEach(({ codeBlock, language, code }) => {
        if (!code || code.trim() === '') return
        
        // 根据语言类型选择高亮方式
        let result
        // 如果语言不为plain或者autodetect，即使用指定语言高亮
        if (language && language !== 'plain' && language !== 'autodetect') {
          result = hljs.highlight(code, { language, ignoreIllegals: true })
        } else {
          result = hljs.highlightAuto(code)
        }
        
        // 先清空内容，确保 Quill 内部状态同步
        codeBlock.textContent = ''
        // 写入高亮结果
        codeBlock.innerHTML = result.value
      })
    } finally {
      isHighlighting = false
    }
  }, 300) // 300ms 防抖
}

const handleLanguageChange = (event) => {
  const target = event.target
  // 验证是否为 select 元素
  if (target.tagName !== 'SELECT') return
  const container = target.closest('.ql-code-block-container')

  if (!container) return
  const language = target.value
  const codeBlock = container.querySelector('.ql-code-block')

  if (!codeBlock) return
  const code = codeBlock.textContent
  
  if (!code || code.trim() === '') return

  isHighlighting = true
  // 保存光标位置
  const selection = quillInstance?.getSelection()

  try {
    // 1. 先保存所有代码块的文本内容
    const containers = quillInstance.root.querySelectorAll('.ql-code-block-container')
    const codeBlocksData = []
    
    containers.forEach((c) => {
      const cb = c.querySelector('.ql-code-block')
      const s = c.querySelector('select')
      if (!cb) return
      codeBlocksData.push({
        codeBlock: cb,
        language: s?.value || '',
        code: cb.textContent || ''
      })
    })
    
    // 2. 高亮目标代码块
    const result = (language && language !== 'plain' && language !== 'autodetect')
      ? hljs.highlight(code, { language, ignoreIllegals: true })
      : hljs.highlightAuto(code)
    
    // 先清空内容，确保 Quill 内部状态同步
    codeBlock.textContent = ''
    codeBlock.innerHTML = result.value
    
    // 3. 重新高亮其他代码块（防止被 Quill 重新同步覆盖）
    codeBlocksData.forEach(({ codeBlock: cb, language: lang, code: c }) => {
      if (cb === codeBlock) return // 跳过已处理的目标代码块
      if (!c || c.trim() === '') return
      
      let r
      if (lang && lang !== 'plain' && lang !== 'autodetect') {
        r = hljs.highlight(c, { language: lang, ignoreIllegals: true })
      } else {
        r = hljs.highlightAuto(c)
      }
      
      cb.textContent = ''
      cb.innerHTML = r.value
    })
  } finally {
    // 恢复光标位置
    setTimeout(() => {
      if (selection && quillInstance) {
        quillInstance.setSelection(selection)
      }
      isHighlighting = false
    }, 50)
  }
}

const handleMouseOver = (event) => {
  const container = event.target.closest('.ql-code-block-container')
  if (!container) return
  // 给代码块容器添加一个名为 select-visible 的 CSS 类。
  container.classList.add('select-visible')
}

const handleMouseOut = (event) => {
  const container = event.target.closest('.ql-code-block-container')
  if (!container) return
  // 移除代码块容器的 select-visible 类。
  container.classList.remove('select-visible')
}

const handlePaste = async (event) => {
  // 获取剪贴板数据
  const items = event.clipboardData?.items
  if (!items) return
  // 遍历剪贴板数据，查找图片
  for (let i = 0; i < items.length; i++) {
    const item = items[i]
    if (item.type.startsWith('image/')) {
      // 检查 MIME 类型是否为图片
      
      const file = item.getAsFile()
      // getAsFile() 将剪贴板图片转为 File 对象

      // 检查文件是否存在 && 编辑器实例是否存在
      if (!file || !quillInstance) return
      
      // 验证通过后再阻止事件，防止 Quill 内部 clipboard 模块再次处理
      event.preventDefault()
      event.stopPropagation()
      event.stopImmediatePropagation()
      
      const range = quillInstance.getSelection()
      const insertIndex = range ? range.index : quillInstance.getLength()
      
      const loadingPlaceholder = '⏳ 图片上传中...'
      quillInstance.insertText(insertIndex, loadingPlaceholder)
      
      try {
        const formData = new FormData()
        formData.append('image', file)
        
        const data = await request({
          url: '/admin/upload',
          method: 'post',
          data: formData
        })
        
        const imageUrl = data.url || data.data?.url || data
        
        if (!imageUrl) {
          throw new Error('上传失败，未返回图片URL')
        }
        
        quillInstance.deleteText(insertIndex, loadingPlaceholder.length)
        quillInstance.insertEmbed(insertIndex, 'image', imageUrl)
        quillInstance.setSelection(insertIndex + 1)
      } catch (error) {
        console.error('图片上传失败:', error)
        try {
          quillInstance.deleteText(insertIndex, loadingPlaceholder.length)
        } catch (e) {
          console.warn('清理占位符失败:', e)
        }
        alert('图片上传失败，请重试')
      }
      return
    }
  }
}

onBeforeUnmount(() => {
  if (quillInstance) {
    // 与 addEventListener 保持一致，使用捕获阶段移除
    quillInstance.root.removeEventListener('paste', handlePaste, true)
    // 移除文本变化的监听
    quillInstance.off('text-change', highlightCodeBlocks)
    // 移除语言选择框变化的监听
    quillInstance.root.removeEventListener('change', handleLanguageChange)
    // 移除鼠标悬停事件监听
    quillInstance.root.removeEventListener('mouseover', handleMouseOver)
    quillInstance.root.removeEventListener('mouseout', handleMouseOut)
  }
})

const handleImageUpload = () => {
  // 创建文件输入元素
  const input = document.createElement('input')
  input.setAttribute('type', 'file')
  input.setAttribute('accept', 'image/*')
  input.click()

  // 监听文件选择事件
  input.onchange = async () => {
    const file = input.files[0]
    if (!file || !quillInstance) return

    // 获取当前光标位置
    const range = quillInstance.getSelection()
    // 确定插入位置
    const loadingIndex = range ? range.index : quillInstance.getLength()
    // 创建占位符
    const placeholder = '⏳ 图片上传中...'
    // 插入占位符
    quillInstance.insertText(loadingIndex, placeholder)

    // 上传图片
    try {
      const formData = new FormData()
      formData.append('image', file)

      const data = await request({
        url: '/admin/upload',
        method: 'post',
        data: formData
      })

      // 处理上传结果
      const imageUrl = data.url || data.data?.url || data

      if (!imageUrl) {
        throw new Error('上传失败，未返回图片URL')
      }
      // 删除占位符
      quillInstance.deleteText(loadingIndex, placeholder.length)
      // 插入图片
      quillInstance.insertEmbed(loadingIndex, 'image', imageUrl)
      // 设置光标位置向后移动一位
      quillInstance.setSelection(loadingIndex + 1)

    } catch (error) {
      console.error('图片上传失败:', error)
      try {
        quillInstance.deleteText(loadingIndex, placeholder.length)
      } catch (e) {
        console.warn('清理占位符失败:', e)
      }
      alert('图片上传失败，请重试')
    }
  }
}

const editorOptions = {
  placeholder: '   开始写作...',
  theme: 'snow',
  modules: {
    clipboard: {
      // 禁用 Quill 的默认剪贴板处理
      matchVisual: false,
    },
    toolbar: {
      container: [
        ['bold', 'italic', 'underline', 'strike'],
        ['code'],
        [{ header: [1, 2, 3, 4, 5, 6, false] }],
        [{ list: 'ordered' }, { list: 'bullet' }],
        [{ align: [] }],
        ['blockquote', 'code-block'],
        ['link', 'image'],
        ['clean']
      ],
      /*
        用户点击工具栏的图片按钮
          ↓
        Quill 检测到该按钮绑定了自定义 handler
          ↓
        调用 handleImageUpload() 函数
          ↓
        函数完全接管后续流程（打开文件对话框、上传、插入）
      */
      handlers: {
        image: handleImageUpload
      }
    },
    blotFormatter: {},
    syntax: true
  }
}

const handleContentChange = () => {
  // 内容变化时触发，用于同步 v-model
}

const getContent = () => {
  if (quillInstance) {
    return quillInstance.root.innerHTML
  }
  return content.value
}

const setContent = (html) => {
  content.value = html
}

// eslint-disable-next-line no-undef
defineExpose({
  content,
  getContent,
  setContent
})
</script>

<style scoped>
.editor-wrapper {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  border: 1px solid rgba(37, 99, 235, 0.1);
}

:deep(.ql-toolbar) {
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
  background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
  border-color: rgba(37, 99, 235, 0.15);
  border-bottom: 1px solid rgba(37, 99, 235, 0.1);
  padding: 10px 16px;
}

:deep(.ql-toolbar button) {
  color: #64748b;
  border-radius: 6px;
  transition: all 0.2s ease;
}

:deep(.ql-toolbar button:hover) {
  background: rgba(37, 99, 235, 0.1);
  color: #2563eb;
}

:deep(.ql-toolbar button.ql-active) {
  background: rgba(37, 99, 235, 0.15);
  color: #2563eb;
}

:deep(.ql-toolbar .ql-picker-label) {
  color: #64748b;
  border-radius: 6px;
  padding: 4px 8px;
}

:deep(.ql-toolbar .ql-picker-label:hover) {
  color: #2563eb;
}

:deep(.ql-toolbar .ql-picker-options) {
  border-radius: 8px;
  border-color: rgba(37, 99, 235, 0.15);
  background: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

:deep(.ql-container) {
  border-bottom-left-radius: 12px;
  border-bottom-right-radius: 12px;
  font-size: 16px;
  min-height: 500px;
  border-color: rgba(37, 99, 235, 0.1);
}

:deep(.ql-editor) {
  padding: 28px;
  min-height: 600px;
  height: 600px;
  font-family: 'Noto Serif SC', 'Microsoft YaHei', serif;
  line-height: 1.8;
  color: #334155;
  overflow-y: auto;
}

:deep(.ql-editor p) {
  margin-bottom: 12px;
}

:deep(.ql-editor h1) {
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
  margin: 24px 0 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid rgba(37, 99, 235, 0.2);
}

:deep(.ql-editor h2) {
  font-size: 20px;
  font-weight: 600;
  color: #1e293b;
  margin: 20px 0 12px;
}

:deep(.ql-editor h3) {
  font-size: 18px;
  font-weight: 600;
  color: #334155;
  margin: 16px 0 10px;
}

:deep(.ql-editor blockquote) {
  border-left: 4px solid #2563eb;
  padding-left: 16px;
  margin: 16px 0;
  color: #64748b;
  font-style: italic;
  background: rgba(37, 99, 235, 0.03);
  padding: 12px 16px;
  border-radius: 0 8px 8px 0;
}

:deep(.ql-editor code) {
  background: rgba(37, 99, 235, 0.08);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Fira Code', 'Consolas', monospace;
  font-size: 14px;
  color: #2563eb;
}

:deep(.ql-editor ul),
:deep(.ql-editor ol) {
  padding-left: 24px;
  margin: 12px 0;
}

:deep(.ql-editor li) {
  margin-bottom: 6px;
}

:deep(.ql-editor a) {
  color: #2563eb;
  text-decoration: none;
  border-bottom: 1px solid rgba(37, 99, 235, 0.3);
}

:deep(.ql-editor a:hover) {
  border-bottom-color: #2563eb;
}

:deep(.ql-editor img) {
  max-width: 100%;
  border-radius: 8px;
  margin: 12px 0;
}

:deep(.ql-editor .ql-align-center) {
  text-align: center;
}

:deep(.ql-editor .ql-align-right) {
  text-align: right;
}

:deep(.ql-editor.ql-blank::before) {
  color: #94a3b8;
  font-style: normal;
}

:deep(.ql-editor::-webkit-scrollbar) {
  width: 6px;
}

:deep(.ql-editor::-webkit-scrollbar-track) {
  background: rgba(37, 99, 235, 0.03);
}

:deep(.ql-editor::-webkit-scrollbar-thumb) {
  background: rgba(37, 99, 235, 0.3);
  border-radius: 3px;
}

:deep(.ql-editor::-webkit-scrollbar-thumb:hover) {
  background: rgba(37, 99, 235, 0.5);
}
</style> 