<template>
  <div class="makeCover">
    <div class="makeCover__top">
      <div class="makeCover__top__logo">
        BiliCover
      </div>
      <div class="makeCover__top__control">
        <el-switch v-model="value3"></el-switch>
        <span class="control__text">辅助区域</span>
        <el-button class="control__save">保存</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      message: 'makeCover',
      value3: false
    }
  },
  mounted() {
    console.log('makeCover', this.message)
  }
}
</script>
<style lang="less" scoped>
.makeCover {
  height: 60px;
  background-color: #000;
  &__top {
    height: 100%;
    display: flex;
    flex-wrap: nowrap;
    align-items: center;
    justify-content: space-between;
    &__logo {
      margin-left: 36px;
      font-size: 36px;
      font-weight: bold;
      color:#fff;
    }
    &__control {
      .el-switch {
        width: 30px;
      }
      .control__text {
        margin-left: 10px;
        font-size: 20px;
        color: #FFFFFF;
      }
      .control__save {
        margin-left: 40px;
        margin-right: 40px;
        width: 100px;
        height: 36px;
        background-color: #EE3554;
        color: #FFFFFF;
        border: 0;
        font-size: 20px;
        padding: 4px 26px 5px 30px;
        border-radius: 4px;
      }
    }
  }
}
</style>
