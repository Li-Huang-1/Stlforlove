/**
 * 用户相关接口
 */
import request from '@/utils/request'

export const userApi = {
  /** 登录，参数 { username, password } */
  login(data) {
    return request({
      url: '/public/user/login',
      method: 'post',
      data
    })
  },

  /** 获取当前登录用户信息（头像等） */
  getInfo() {
    return request({
      url: '/public/user/info',
      method: 'get'
    })
  }
}
