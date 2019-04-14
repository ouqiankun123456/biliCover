import http from './index'

/**
 * 生成封面接口
 * @author chwech
 * @date 2019-04-10
 * @export
 * @param {Object} params
 * @returns
 */
export function generateCover (params) {
  return http.post({
    url: 'coverGenerator',
    filterNull: false,
    params: params
  })
}

/**
 * 下载接口
 * @author chwech
 * @date 2019-04-10
 * @export
 * @param {Object} params
 * @returns
 */
export function downloadFile (params, axiosConfig) {
  return http.get({
    url: 'io/download',
    params: params,
    ...axiosConfig
  })
}

/**
 * 上传接口
 * @author chwech
 * @date 2019-04-10
 * @export
 * @param {Object} params
 * @returns
 */
export function uploadFile (params) {
  return http.post({
    url: 'io/chunkUpload',
    params: params
  })
}
