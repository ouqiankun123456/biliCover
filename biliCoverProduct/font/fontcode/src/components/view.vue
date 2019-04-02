<template>
  <div class="view" @click="click">
    <div class="view__img">
      <img :src="imgSrc" width='100%' height='100%'>
    </div>
    <div class="view__name">
      {{name}}
    </div>
    <div class="view__type">
      {{type}}
    </div>
  </div>
</template>

<script>
export default {
  props: {
    imgSrc: {
      type: String
    },
    name: {
      type: String
    },
    type: {
      type: String
    },
    width: {
      type: String,
      default: '260px'
    },
    height: {
      type: String,
      default: '163px'
    }
  },
  data() {
    return {
      message: 'view'
    }
  },
  methods: {
    click () {
      this.$emit('itemClick')
    }
  },
  mounted() {
    console.log('view', this.message)
  }
}
</script>
<style lang="less" scoped>
.view {
  &__img {
    width: 100%;
    height: calc(100% - 70px);
  }
  &__name {
    margin-top: 14px;
    text-align: left;
    font-size: 24px;
    color: #333333;
  }
  &__type {
    text-align: left;
    font-size: 18px;
    color: #999999;
  }
}
</style>
