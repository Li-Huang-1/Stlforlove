/**
 * 留言板相关接口
 */
import request from '@/utils/request'

export const messageApi = {
  /** 获取留言列表（后端按创建时间倒序返回） */
  getList() {
    return request({
      url: '/public/message/list',
      method: 'get'
    })
  },

  /** 发布留言，参数 { nickname, content }，返回新插入的留言对象 */
  add(data) {
    return request({
      url: '/public/message',
      method: 'post',
      data
    })
  }
}
