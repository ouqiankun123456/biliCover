<template>
  <div class="picked">
    <div>picked</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      message: 'picked'
    }
  },
  mounted() {
    console.log('picked', this.message)
  }
}
</script>
<style lang="less" scoped>

</style>
