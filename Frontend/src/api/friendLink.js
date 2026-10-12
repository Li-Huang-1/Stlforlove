/**
 * 友链相关接口
 */
import request from '@/utils/request'

export const friendLinkApi = {
  /** 获取友链列表（后端按创建时间正序返回） */
  getList() {
    return request({
      url: '/public/friend-link/list',
      method: 'get'
    })
  },

  /** 新增友链（需登录），参数 { name, url, logo, description } */
  add(data) {
    return request({
      url: '/admin/friend-link',
      method: 'post',
      data
    })
  },

  /** 根据 id 删除友链（需登录） */
  remove(id) {
    return request({
      url: `/admin/friend-link/${id}`,
      method: 'delete'
    })
  }
}
