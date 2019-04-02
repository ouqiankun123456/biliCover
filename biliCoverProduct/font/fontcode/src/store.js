import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    searchName: '',
    searchType: '全部',
    previewImage: '',
    cropperFormData: null
  },
  mutations: {
    setCropperFormData (state, formData) {
      state.cropperFormData = formData
    },
    setPreviewImage (state, image) {
      state.previewImage = image
    },
    setSearchName (state, name) {
      state.searchName = name
    },
    setSearchType (state, type) {
      state.searchType = type
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
