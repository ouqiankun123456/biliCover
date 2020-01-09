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
    return 'https://www.cocover.cn/'
  }
}

function getRealBaseUrl() {
  return location.host
}

/**
 * 封装axios的接口处理函数, RESTful风格
 * @param {Object} axios config object
 * @param {Boolean} saySuccess 是否调用提示
 * @param {Boolean} [filterN=true] 过滤参数中有null的值
 * @returns {Promise}
 */
function apiAxios({ method, url, params, responseType = 'json' }, saySuccess, filterN = true) {
  if (params && filterN) {
    params = filterNull(params)
  }
  return axios({
    method: method,
    url: encodeURI(url),
    data: method === 'POST' || method === 'PUT' ? params : null,
    params: method === 'GET' || method === 'DELETE' ? params : null,
    baseURL: getBaseUrl(),
    responseType: responseType
  }).then(function (res) {
    console.log(res)
    if (res.data.code === 200) {
      if (res.data.message && saySuccess) {
        // vm.$weui.toast(res.data.message)
      }
      return res.data.result
    } else if (res.data instanceof Blob) {
      return res.data
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
  realBaseUrl: getRealBaseUrl(),
  baseUrl: getBaseUrl(),
  get ({ url, params, saySuccess = true, filterNull, responseType }) {
    if (params instanceof Array && params.length > 0) {
      for (let param of params) {
        url = url + '/' + param
      }
      params = null
    }
    return apiAxios({
      method: 'GET',
      url: url,
      params: params,
      responseType: responseType
    }, saySuccess, filterNull)
  },
  post ({ url, params, saySuccess = true, filterNull, responseType }) {
    return apiAxios({
      method: 'POST',
      url: url,
      params: params,
      responseType: responseType
    }, saySuccess, filterNull)
  },
  put ({ url, params, saySuccess = true, filterNull, responseType }) {
    return apiAxios({
      method: 'PUT',
      url: url,
      params: params,
      responseType: responseType
    }, saySuccess, filterNull)
  },
  delete ({ url, params, saySuccess = true, filterNull, responseType }) {
    if (params instanceof Array && params.length > 0) {
      for (let param of params) {
        url = url + '/' + param
      }
      params = null
    }
    return apiAxios({
      method: 'DELETE',
      url: url,
      params: params,
      responseType: responseType
    }, saySuccess, filterNull)
  }
}
