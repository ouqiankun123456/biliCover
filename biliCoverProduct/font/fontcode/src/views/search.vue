<template>
  <div class="search">
    <search-header></search-header>
    <router-view/>
  </div>
</template>

<script>
import searchHeader from '@/components/searchHeader.vue'
export default {
  components: {
    searchHeader
  },
  data() {
    return {
      message: 'search'
    }
  },
  mounted() {
    console.log('search', this.message)
  }
}
</script>
<style lang="less" scoped>
.search {
  height: 100%;
}
</style>
