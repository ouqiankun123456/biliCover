import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
// 使用自定义颜色主题
import './assets/theme/index.css'
import '@/assets/css/base.less'
import http from '@/api'

Vue.use(ElementUI)
Vue.config.productionTip = false
Vue.prototype.$http = http
Vue.prototype.$bus = new Vue()

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
