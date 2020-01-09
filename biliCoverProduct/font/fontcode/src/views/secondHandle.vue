<template>
  <div class="firstHandle">
    <div class="editor">
      <div class="editor-preview">
        <div class="editor-preview__title">
          <h1>已选模板：{{ templateName }}</h1>
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
            <el-input class="editor-form__item--input" v-model="formData[item.paramName]" @blur="generateCover"></el-input>
          </div>
        </div>
        <div>
          <el-button class="btn-default prev-btn" @click="prevStep">上一步</el-button>
          <el-button type="danger" class="btn-default next-btn" @click="nextStep">下一步</el-button>
        </div>
      </div>
    </div>
    <div class="footer">
      <a href="http://www.beian.miit.gov.cn" target="_blank">©2020 BiliCover 粤ICP备19032327号</a>
    </div>
  </div>
</template>

<script>
import { mapMutations } from 'vuex'
export default {
  props: ['templateKid', 'templateName'],
  data() {
    return {
      formItem: [],
      formData: {},
      coverImageSrc: ''
    }
  },
  methods: {
    ...mapMutations([
      'setFormItem',
      'setCropperFormData',
      'setPreviewImage'
    ]),
    prevStep () {
      this.$router.go(-1)
    },
    nextStep () {
      // this.$router.push('/makeCover/firstHandle')
      this.$router.push({
        name: 'firstHandle',
        params: {
          templateName: this.templateName,
          templateKid: this.templateKid
        }
      })
    },
    async bootstrap () {
      // 获取表单
      let formItem = await this.getFormItem()
      // 保存到vuex, 下一个页面会用到
      this.setFormItem(formItem)
      this.formItem.push(...formItem)
      // 设置表单的默认值
      formItem.forEach(({ paramName, defaultValue }) => { this.$set(this.formData, paramName, defaultValue) })
      // 生成封面
      this.generateCover()
    },
    getFormItem () {
      return this.$http.get({
        url: 'coverGenerator',
        params: [this.templateKid]
      })
    },
    generateCover () {
      this.$http.post({
        url: '/coverGenerator',
        filterNull: false,
        params: {
          printSize: 'bilibili',
          templateKid: this.templateKid,
          data: this.formData
        }
      }).then(res => {
        this.coverImageSrc = this.$http.baseUrl + res
        this.setPreviewImage(this.coverImageSrc)
        this.setCropperFormData(this.formData)
      })
    }
  },
  created () {
    this.bootstrap()
  }
}
</script>
<style lang="less" scoped>
.firstHandle {
  background-color: rgb(245, 245, 245);
  .editor {
    width: 960px + 64px + 340px;
    margin: 0 auto;
    overflow: hidden;
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
  .footer {
    text-align: center;
    padding: 16px 0;
    margin-top: 60px;
    margin-bottom: 20px;

    a {
      text-decoration: none;
      color: black;
    }
  }
}
</style>
