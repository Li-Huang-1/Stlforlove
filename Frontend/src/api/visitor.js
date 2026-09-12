/**
 * 访客统计相关接口
 */
import request from '@/utils/request'

export const visitorApi = {
  /** 获取当前访客总数 */
  getCount() {
    return request({
      url: '/public/visitor/count',
      method: 'get'
    })
  },

  /** 访客数 +1 并返回最新计数 */
  increment() {
    return request({
      url: '/public/visitor/increment',
      method: 'get'
    })
  }
}
