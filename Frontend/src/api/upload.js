/**
 * 文件上传相关接口
 */
import request from '@/utils/request'

export const uploadApi = {
  /**
   * 上传图片
   * @param {FormData} formData - 包含 image 字段的表单数据
   * @param {Function} [onUploadProgress] - 上传进度回调（可选）
   */
  uploadImage(formData, onUploadProgress) {
    return request({
      url: '/admin/upload',
      method: 'post',
      data: formData,
      onUploadProgress
    })
  }
}
