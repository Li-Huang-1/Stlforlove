/**
 * 图片懒加载：自定义指令 v-lazy + v-html 内容批量处理函数
 *
 * 两种用法：
 * 1. 模板中的 <img>：<img v-lazy="真实图片地址" alt="..." />
 * 2. v-html 渲染出的正文图片：DOM 插入后调用 setupLazyImages(容器元素)
 *
 * 原理：
 * 1. 处理时先把 img 的 src 换成本地占位图（loading gif），并加上
 *    lazy-loading 状态类（各页面可据此让占位图居中完整显示、保留可见高度），
 *    真实地址暂存在 dataset.lazySrc 中，用 data-lazy-bound 标记防止重复处理；
 * 2. 通过 IntersectionObserver 监听图片，进入视口前不发起真实图片请求；
 * 3. 图片进入视口（提前 50px）时，先用 new Image() 预加载真实图片，
 *    下载完成后再替换 src 并移除状态类，避免占位图被拉伸或切换闪烁；
 * 4. 不支持 IntersectionObserver 的旧浏览器直接加载真实图片（降级）。
 */
import placeholderImg from '@/assets/懒加载占位图.gif'

// 全局共享一个观察者实例，避免每张图片各创建一个，减少性能开销
let lazyObserver = null

/** 图片进入视口后：预加载真实图片，完成后替换 src */
function loadRealImg(img) {
  const realSrc = img.dataset.lazySrc
  if (!realSrc) return
  // 先静默预加载真实图片，下载完成后再替换，占位图全程保持 loading 样式
  const loader = new Image()
  loader.onload = () => {
    img.src = realSrc
    img.classList.remove('lazy-loading')
  }
  loader.onerror = () => {
    // 真实图加载失败：移除 loading 态并赋真实地址，
    // 让元素上原有的 @error 处理逻辑接管（如显示兜底占位）
    img.classList.remove('lazy-loading')
    img.src = realSrc
  }
  loader.src = realSrc
}

function getObserver() {
  if (lazyObserver) return lazyObserver
  lazyObserver = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          loadRealImg(entry.target)
          // 只加载一次，加载后即取消观察
          lazyObserver.unobserve(entry.target)
        }
      })
    },
    {
      // 提前 50px 开始加载，滚动到图片位置时通常已加载完成，体验更顺滑
      rootMargin: '50px 0px'
    }
  )
  return lazyObserver
}

/**
 * 让单张 img 进入懒加载流程（指令与批量处理共用）
 * @param {HTMLImageElement} img 目标图片元素
 * @param {string} realSrc 真实图片地址
 */
function observeImg(img, realSrc) {
  if (!realSrc) return
  // 暂存真实地址，先展示占位图并标记 loading 态
  img.dataset.lazySrc = realSrc
  img.dataset.lazyBound = 'true'
  img.src = placeholderImg
  img.classList.add('lazy-loading')

  if ('IntersectionObserver' in window) {
    getObserver().observe(img)
  } else {
    // 旧浏览器降级：直接加载真实图片
    img.src = realSrc
    img.classList.remove('lazy-loading')
  }
}

/**
 * 批量处理容器内所有未懒加载的 <img>（用于 v-html 渲染的富文本正文）
 * 在内容渲染到 DOM 之后（如 nextTick 中）调用
 * @param {HTMLElement} container 包含正文图片的容器元素
 */
export function setupLazyImages(container) {
  if (!container || !container.querySelectorAll) return
  const imgs = container.querySelectorAll('img:not([data-lazy-bound])')
  imgs.forEach((img) => {
    // v-html 中图片的真实地址就在 src 上，取出后交由懒加载流程接管
    observeImg(img, img.getAttribute('src'))
  })
}

/**
 * 取消容器内所有懒加载图片的观察（组件卸载前调用，防止内存泄漏）
 * @param {HTMLElement} container 包含正文图片的容器元素
 */
export function teardownLazyImages(container) {
  if (!container || !container.querySelectorAll || !lazyObserver) return
  container.querySelectorAll('img[data-lazy-bound]').forEach((img) => {
    lazyObserver.unobserve(img)
  })
}

export const lazyDirective = {
  mounted(el, binding) {
    // 指令只作用于 img 元素
    if (el.tagName !== 'IMG') return
    if (binding.value) {
      observeImg(el, binding.value)
    }
  },
  updated(el, binding) {
    if (el.tagName !== 'IMG') return
    // 真实地址发生变化时同步更新（如列表数据刷新）
    if (binding.value && binding.value !== binding.oldValue) {
      el.dataset.lazySrc = binding.value
    }
  },
  unmounted(el) {
    // 组件卸载时取消观察，防止内存泄漏
    if (lazyObserver) {
      lazyObserver.unobserve(el)
    }
  }
}
