import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    searchName: '',
    searchType: '全部',
    previewImage: '',
    cropperFormData: null,
    formItem: []
  },
  mutations: {
    setFormItem (state, data) {
      state.formItem = data
    },
    setCropperFormData (state, formData) {
      state.cropperFormData = formData
    },
    setPreviewImage (state, image) {
      console.log(image)
      state.previewImage = image
    },
    setSearchName (state, name) {
      state.searchName = name
    },
    setSearchType (state, type) {
      state.searchType = type
    }
  },
  getters: {
    previewImage (state) {
      return state.previewImage + '?' + Math.random()
    },
    tabData (state) {
      return state.formItem.filter(item => item.paramType === 'image')
    }
  },
  actions: {
    setSearchName ({ commit }, name) {
      commit('setSearchName', name)
    },
    setSearchType ({ commit }, type) {
      commit('setSearchType', type)
    }
  }
})
