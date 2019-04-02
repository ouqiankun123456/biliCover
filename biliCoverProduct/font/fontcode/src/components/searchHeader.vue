<template>
  <div class="searchHeader">
    <div class="searchHeader__top">
      <div class="searchHeader__top__logo" @click="backIndex">
        BiliCover
      </div>
      <div class="searchHeader__top__input">
        <el-input prefix-icon="el-icon-search" placeholder="你可以尝试通过主题、风格等关键词搜索你想要的模板" @change="changeSearchName" v-model="localInput"></el-input>
      </div>
    </div>
    <div class="searchHeader__selectType">
      <div class="searchHeader__selectType__main">模板类型</div>
      <div class="searchHeader__selectType__divide">|</div>
      <div :class="{'searchHeader__selectType__item': true, 'searchHeader__selectType__item-active': item === $store.state.searchType}" v-for="item in selectTypes" :key="item" @click="changeType(item)">{{item}}</div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      localInput: '',
      message: 'searchHeader',
      selectTypes: ['全部', '动画', '音乐']
    }
  },
  methods: {
    changeType(type) {
      if (type === this.$store.state.searchType) {
        return
      }
      this.$store.dispatch('setSearchType', type)
    },
    changeSearchName(name) {
      this.$store.dispatch('setSearchName', name)
    },
    backIndex() {
      this.$router.push({ name: 'home' })
    }
  },
  watch: {
    '$store.state.searchType'(newVal, oldVal) {
      this.$bus.$emit('needSearch')
    },
    '$store.state.searchName'(newVal, oldVal) {
      this.$bus.$emit('needSearch')
    }
  },
  mounted() {
    console.log('searchHeader', this.message)
  }
}
</script>
<style lang="less">
.searchHeader {
  height: 120px;
  background-color: #000000;
  &__top {
    height: 60px;
    display: flex;
    flex-wrap: nowrap;
    align-items: center;
    &__logo {
      margin-left: 36px;
      color: #ffffff;
      font-size: 36px;
      font-weight: bold;
    }
    &__input {
      width: 1100px;
      margin-left: 20px;
      /deep/.el-input__inner {
        background-color: #333333;
        border: 0;
        height: 36px;
        line-height: 36px;
      }
      /deep/.el-input__icon {
        line-height: 36px;
      }
    }
  }
  &__selectType {
    display: flex;
    flex-wrap: nowrap;
    align-items: center;
    height: 60px;
    &__main {
      margin-left: 40px;
      font-size: 20px;
      color: #999999;
    }
    &__divide {
      margin-left: 30px;
      margin-right: 30px;
      color: #ffffff;
      font-size: 20px;
    }
    &__item {
      margin-left: 40px;
      color: #999999;
      font-size: 20px;
      cursor: pointer;
      &:nth-child(3) {
        margin-left: 0;
      }
    }
    &__item-active {
      color: #ffffff;
    }
  }
}
</style>
