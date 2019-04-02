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
      message: 'choose',
      items: []
    }
  },
  methods: {
    handleListItemClick (item) {
      this.$router.push({
        name: 'secondHandle',
        params: {
          templateKid: item.kid
        }
      })
    },
    getData() {
      this.items = []
      let _this = this;
      this.$axios
        .get('proxyUrl/cover', {
          params: {
            templateName: this.$store.state.searchName,
            templateType: this.$store.state.searchType === '全部' ? '' : this.$store.state.searchType
          }
        })
        .then(response => {
          this.items = response.data.result.map(item => {
            // item.imgSrc = 'http://238o4s4873.zicp.vip:57014/' + item.previewImage
            item.imgSrc = 'proxyUrl' + item.previewImage
            return item
          })
          // this.items = response.data.result
          // this.getImages()
        })
        .catch(function(error) {
          console.log(error)
        })
    },
    getImage(name, index) {
      this.$axios
        .get('proxyUrl/io/download', {
          responseType: 'blob',
          params: {
            name: name,
            type: 'uploadFile'
          }
        })
        .then(response => {
          let bold = new Blob([response.data], { type: `${response.data.type}` })
          this.$set(this.items[index], 'imgSrc', URL.createObjectURL(bold))
        })
        .catch(function(error) {
          console.log(error)
        })
    },
    getImages() {
      this.items.map((currentValue, index, arr) => {
        this.getImage(currentValue.previewImage, index)
      })
      console.log(this.items)
    }
  },
  mounted() {
    console.log('choose', this.message)
    console.log(this.$bus.$emit)
    this.$bus.$on('needSearch', this.getData)
    this.getData()
    // setInterval(this.testAddData, 2000)
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
    // .view {
    //   &:not(:nth-child(1)) {
    //     margin-left: 16px;
    //   }
    // }
  }
}
</style>
