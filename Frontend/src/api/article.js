/**
 * 文章相关接口
 */
import request from '@/utils/request'

export const articleApi = {
  /**
   * 分页查询文章简略列表（无 content）
   * @param {Object} data - { pageNum, pageSize, type, keyword, category, includeDraft }
   */
  getList(data) {
    return request({
      url: '/public/article/list',
      method: 'post',
      data
    })
  },

  /** 根据 id 查询文章详情（含 content） */
  getDetail(id) {
    return request({
      url: `/public/article/${id}`,
      method: 'get'
    })
  },

  /** 获取已发布文章总数 */
  getCount() {
    return request({
      url: '/public/article/count',
      method: 'get'
    })
  },

  /** 新建文章 */
  add(data) {
    return request({
      url: '/admin/article/new',
      method: 'post',
      data
    })
  },

  /** 更新文章 */
  update(data) {
    return request({
      url: '/admin/article/update',
      method: 'post',
      data
    })
  },

  /** 根据 id 删除文章 */
  remove(id) {
    return request({
      url: `/admin/article/delete/${id}`,
      method: 'delete'
    })
  }
}
