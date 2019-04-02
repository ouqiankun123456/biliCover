import axios from 'axios'
import { filterNull } from '@/assets/js/utils.js'

// 设置允许跨域 这样cookie就能传送到后台
axios.defaults.withCredentials = true

/**
 * 获取axios baseURL
 * @returns {String}
 */
function getBaseUrl() {
  if (process.env.NODE_ENV === 'development') {
    return 'proxyUrl'
  } else {
    // return 'http://120.79.0.225/back'
    return location.host + '/back'
  }
}

/**
 * 封装axios的接口处理函数, RESTful风格
 * @param {String} method
 * @param {String} url
 * @param {Object|Array} params 请求参数
 * @param {Boolean} saySuccess 是否调用提示
 * @param {Boolean} [filterN=true] 过滤参数中有null的值
 * @returns {Promise}
 */
function apiAxios(method, url, params, saySuccess, filterN = true) {
  if (params && filterN) {
    params = filterNull(params)
  }
  return axios({
    method: method,
    url: encodeURI(url),
    data: method === 'POST' || method === 'PUT' ? params : null,
    params: method === 'GET' || method === 'DELETE' ? params : null,
    baseURL: getBaseUrl()
  }).then(function (res) {
    if (res.data.code === 200) {
      if (res.data.message && saySuccess) {
        // vm.$weui.toast(res.data.message)
      }
      return res.data.result
    } else {
      if (res.data.message) {
        // vm.$weui.topTips(res.data.message)
      }
      console.log('http error code is not 200 and detail message is: ' + JSON.stringify(res.data))
      return Promise.reject(res.data)
    }
  }).catch(function (err) {
    console.log('I have catched http error:' + err)
    return Promise.reject(err)
  })
}

// 返回在vue模板中的调用接口
export default {
  baseUrl: getBaseUrl(),
  get ({ url, params, saySuccess = true, filterNull }) {
    if (params instanceof Array && params.length > 0) {
      for (let param of params) {
        url = url + '/' + param
      }
      params = null
    }
    return apiAxios('GET', url, params, saySuccess, filterNull)
  },
  post ({ url, params, saySuccess = true, filterNull }) {
    return apiAxios('POST', url, params, saySuccess, filterNull)
  },
  put ({ url, params, saySuccess = true, filterNull }) {
    return apiAxios('PUT', url, params, saySuccess, filterNull)
  },
  delete ({ url, params, saySuccess = true, filterNull }) {
    if (params instanceof Array && params.length > 0) {
      for (let param of params) {
        url = url + '/' + param
      }
      params = null
    }
    return apiAxios('DELETE', url, params, saySuccess, filterNull)
  }
}
