<template>
  <div class="choose">
    <div class="choose__title">选取一个模板</div>
    <div class="choose__suggest">选择合适的模板，会让你的作品受到更多目标观众看到哦~</div>
    <div class="choose__block">
      <img-view
        @itemClick="handleListItemClick(item)"
        v-for="item in items"
        :key="item.kid"
        :imgSrc="item.imgSrc"
        :name="item.templateName"
        :type="item.templateType"
      ></img-view>
    </div>
  </div>
</template>

<script >
import imgView from '@/components/view.vue'
export default {
  components: {
    imgView
  },
  data() {
    return {
      items: []
    }
  },
  methods: {
    handleListItemClick (item) {
      this.$router.push({
        name: 'secondHandle',
        params: {
          templateName: item.templateName,
          templateKid: item.kid
        }
      })
    },
    getData() {
      this.$http.get({
        url: '/cover',
        params: {
          templateName: this.$store.state.searchName,
          templateType: this.$store.state.searchType === '全部' ? '' : this.$store.state.searchType
        }
      }).then(result => {
        this.items = result.map(item => {
          item.imgSrc = this.$http.baseUrl + item.previewImage
          return item
        })
      })
    }
  },
  mounted() {
    this.$bus.$on('needSearch', this.getData)
    this.getData()
  }
}
</script>
<style lang="less" scoped>
.choose {
  height: calc(100% - 120px);
  overflow: auto;
  display: flex;
  flex-direction: column;
  padding-right: 278px;
  padding-left: 278px;
  background-color: rgb(245, 245, 245);
  &__title {
    margin-top: 100px;
    font-size: 60px;
    color: #000000;
    text-align: left;
  }
  &__suggest {
    margin-top: 20px;
    text-align: left;
    font-size: 24px;
    color: #666666;
  }
  &__block {
    margin-top: 60px;
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    grid-column-gap: 15px;
    grid-row-gap: 60px;
    justify-items: stretch;
  }
}
</style>
