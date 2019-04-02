<template>
  <div class="makeCover">
    <make-cover-header></make-cover-header>
    <router-view/>
  </div>
</template>

<script>
import makeCoverHeader from '@/components/makeCoverHeader.vue'
export default {
  components: {
    makeCoverHeader
  },
  data() {
    return {
      message: 'makeCover'
    }
  },
  mounted() {
    console.log('makeCover', this.message)
  }
}
</script>
<style lang="less" scoped>

</style>
