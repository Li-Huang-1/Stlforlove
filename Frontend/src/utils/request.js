  /**
   * Axios 封装工具文件
   * 
   * 功能：
   * 1. 创建统一的 axios 实例，配置基础 URL 和超时时间
   * 2. 请求拦截器：在发送请求前自动添加 Token 到请求头
   * 3. 响应拦截器：统一处理响应数据和错误（如 401 未授权）
   * 4. 错误处理：统一的错误提示和跳转逻辑
   * 
   * 使用方法：
   * import request from '@/utils/request'
   * 
   * request({
   *   url: '/api/user',
   *   method: 'get'
   * })
   * 
   * 设计理念：
   * - 统一配置：所有请求共享同一个基础配置
   * - 拦截器模式：请求和响应的公共逻辑集中处理
   * - 错误统一：所有错误在一个地方处理，减少重复代码
   */

  // 导入 axios 库（HTTP 请求库）
  import axios from 'axios'

  /**
   * 创建 axios 实例
   * axios.create() 返回一个新的 axios 实例，可以独立配置
   * 
   * @param {string} baseURL - 后端 API 基础地址
   * @param {number} timeout - 请求超时时间（毫秒）
   */
  const service = axios.create({
    // 读取环境变量配置的 API 地址，默认为本地后端服务（端口 8081）
    // process.env.VUE_APP_BASE_API：来自 .env 文件的环境变量
    baseURL: process.env.VUE_APP_BASE_API || 'http://localhost:8081',
    timeout: 15000  // 15秒超时：超过15秒没响应就放弃
  })

  /**
   * 请求拦截器（Interceptor）
   * 在发送请求之前执行的逻辑
   * 
   * 作用：
   * - 自动添加 Token 到请求头（实现身份认证）
   * - 添加公共请求头（如 Content-Type）
   * - 请求参数预处理（如格式化数据）
   */
  service.interceptors.request.use(
    /**
     * 请求成功时的回调函数
     * @param {Object} config - axios 请求配置对象（包含 url、method、headers、data 等）
     * @returns {Object} - 修改后的配置对象
     */
    config => {
      // 1. 从 sessionStorage 获取登录 Token（登录成功后保存的）
      const token = sessionStorage.getItem('token')
      
      // 2. 如果有 Token，添加到请求头的 Authorization 字段
      // Bearer：标准的 Token 认证类型
      if (token) {
        config.headers['Authorization'] = 'Bearer ' + token
      }

      // 3. 处理 FormData 类型数据（如上传文件）
      // 删除 Content-Type 头，让浏览器自动设置
      if (config.data instanceof FormData) {
        delete config.headers['Content-Type']
      }
      
      
      // 4. 返回修改后的配置对象（必须返回，否则请求无法发送）
      return config
    },
    
    /**
     * 请求出错时的回调函数
     * @param {Error} error - 请求错误对象
     * @returns {Promise} - 返回错误 Promise
     */
    error => {
      // 打印错误信息（便于调试）
      console.error('请求错误:', error)
      // 返回错误，让调用方可以捕获处理
      return Promise.reject(error)
    }
  )

  /**
   * 响应拦截器（Interceptor）
   * 在接收到响应后执行的逻辑
   * 
   * 作用：
   * - 统一处理响应数据（如提取 data 字段）
   * - 统一处理错误（如 401 跳转登录页）
   * - 响应数据格式化
   */
  service.interceptors.response.use(
    /**
     * 响应成功时的回调函数
     * @param {Object} response - axios 响应对象（包含 status、data、headers 等）
     * @returns {Object} - 处理后的响应数据
     */
    response => {
      // 1. 获取响应体数据（后端返回的 JSON）
      const res = response.data
      
      // 2. 判断响应状态码（后端约定：code === 200 表示成功）
      if (res.code !== 200) {
        // 非成功状态，打印错误信息
        console.error('请求失败:', res.msg || '服务器错误')
        
        // 3. code === 401：未授权（Token 过期或无效）
        if (res.code === 401) {
          // 获取当前是否已登录（有 token 说明之前登录过）
          const hasToken = sessionStorage.getItem('token')
          if (hasToken) {
            // 只有已登录状态下 Token 过期才清除并跳转
            sessionStorage.removeItem('token')
            sessionStorage.removeItem('isLoggedIn')
            // 跳转到登录页
            window.location.href = '/login'
          }
        }
        
        // 4. 返回错误，让调用方可以捕获处理
        return Promise.reject(new Error(res.msg || '服务器错误'))
      }
      
      if (res.data !== undefined) {
        return res.data
      } else if (res.rows !== undefined) {
        return res
      }
      return res
    },
    
    /**
     * 响应出错时的回调函数
     * @param {Error} error - 响应错误对象
     * @returns {Promise} - 返回错误 Promise
     */
    error => {
      // 打印错误信息（便于调试）
      console.error('响应错误:', error)
      
      // 判断是否是 401 错误（HTTP 状态码）
      if (error.response && error.response.status === 401) {
        // 清除本地存储的 Token 和登录状态
        sessionStorage.removeItem('token')
        sessionStorage.removeItem('isLoggedIn')
        // 跳转到登录页
        window.location.href = '/login'
      }
      
      // 返回错误，让调用方可以捕获处理
      return Promise.reject(error)
    }
  )

  // 导出封装好的 axios 实例
  // 其他文件通过 import request from '@/utils/request' 使用
  export default service