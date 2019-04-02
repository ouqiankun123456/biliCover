<template>
  <div class="firstHandle">
    <div class="editor">
      <div class="editor-preview">
        <div class="editor-preview__title">
          <h1>已选模板：国服最强狄仁杰</h1>
          <h4>上传自己的图片素材，在建议区域进行构图或许效果更好哦~</h4>
        </div>
        <div class="editor-preview__img">
          <img :src="coverImageSrc" />
        </div>
      </div>
      <div class="editor-edit">
        <div class="editor-edit__title">
          <h1>编辑文字</h1>
        </div>
        <div class="editor-form" v-for="item of formItem" :key="item.kid">
          <div class="editor-form__item" v-if="item.paramType === 'input'">
            <span class="editor-form__item--title">{{ item.paramName }}</span>
            <!-- <span class="editor-form__item--suggestion">建议5-7个字</span> -->
            <el-input class="editor-form__item--input" v-model="formData[item.paramName]" @change="coverGenerator"></el-input>
          </div>
        </div>
        <div>
          <el-button class="btn-default prev-btn">上一步</el-button>
          <el-button type="danger" class="btn-default next-btn" @click="nextStep">下一步</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapMutations } from 'vuex'
export default {
  data() {
    return {
      activeNames: ['1'],
      // 对比度
      contrast: 50,
      // 亮度
      brightness: 50,
      // 颜色饱和度
      saturation: 50,

      formItem: [],

      formData: {},

      coverImageSrc: ''
    }
  },
  methods: {
    ...mapMutations([
      'setCropperFormData',
      'setPreviewImage'
    ]),
    nextStep () {
      this.$router.push('/makeCover/firstHandle')
    },
    coverGenerator () {
      let { templateKid } = this.$route.params
      let data = {
        templateKid: templateKid,
        data: {
          ...this.formData,
          '图片1': ''
        },
        printSize: 'bilibili'
      }
      this.setCropperFormData(data)
      this.$axios.post('proxyUrl/coverGenerator', data).then(res => {
        this.coverImageSrc = 'proxyUrl/temp' + res.data.result
        this.setPreviewImage(this.coverImageSrc)
      })
    }
  },
  mounted() {
    let { templateKid } = this.$route.params
    // 获取要填写的表单
    this.$axios.get('proxyUrl/coverGenerator/' + templateKid)
      .then(res => {
        this.formItem = res.data.result
        res.data.result.forEach(item => {
          if (item.paramType === 'input') {
            this.$set(this.formData, item.paramName, '')
          }
        })
      })

    this.$axios.post('proxyUrl/coverGenerator', {
      templateKid: templateKid,
      data: {
        '标题内容1': '国服最强狄仁杰',
        '标题内容2': '为无辜者代言',
        '图片1': ''
      },
      printSize: 'bilibili'
    }).then(res => {
      this.$axios.get('proxyUrl/io/download', {
        responseType: 'blob',
        params: {
          name: res.data.result,
          type: 'temp'
        }
      }).then(res => {
        let blob = new Blob([res.data], { type: `${res.data.type}` })
        this.coverImageSrc = URL.createObjectURL(blob)
        this.setPreviewImage(this.coverImageSrc)
      })
    })
  }
}
</script>
<style lang="less" scoped>
.firstHandle {
  .editor {
    width: 960px + 64px + 340px;
    margin: 0 auto;
    &-preview {
      width: 960px;
      float: left;
      &__title {
        text-align: left;
        h1 {
          font-size: 60px;
          font-weight: bold;
          line-height: 80px;
          color:rgba(0,0,0,1);
          opacity: 1;
        }
        h4 {
          font-size: 24px;
          font-weight: 400;
          line-height: 32px;
          color:rgba(102,102,102,1);
          opacity: 1;
        }
      }

      &__img {
        border: 1px solid #e8e8e8;
        img {
          width: 100%;
          height: 600px;
        }
      }
    }

    &-edit {
      float: right;
      width: 340px;
      margin-top: 190px;
      .btn-default {
        width: 100%;
        margin-top: 10px;
        font-size: 24px;
        border-radius: 0;
        padding-top: 14px;
        padding-bottom: 14px;
      }
      .upload-btn {
        width: 100%;
      }
      .prev-btn {
        margin-top: 40px;
      }
      .next-btn {
        margin-left: 0;
        background-color: #EE3554;
      }
      &__title {
        h1 {
          font-size: 36px;
          font-weight: bold;
          line-height: 48px;
        }
      }
      &__tips {
        text-align: left;
        font-size: 23px;
        font-weight: 400;
        line-height: 32px;
        color:rgba(102,102,102,1);
        opacity: 1;
      }
    }

    &-form {
      &__item {
        clear: both;
        margin-top: 24px;
        &--title {
          font-size:24px;
          font-weight:400;
          line-height:32px;
          color:rgb(102,102,102);
          float: left;
        }
        &--suggestion {
          font-size:16px;
          font-weight:400;
          line-height:32px;
          color:rgb(153,153,153);
          float: right;
        }
        &--input {
          margin-top: 10px;
          /deep/ .el-input__inner {
            border-radius: 0;
            height: 36px;
            background-color: rgb(238,238,238);;
          }
        }
      }
    }
  }
}
</style>
