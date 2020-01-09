<template>
  <div class="firstHandle">
    <div class="editor">
      <div class="editor-preview">
        <div class="editor-preview__title">
          <h1>已选模板：{{ this.templateName }}</h1>
          <h4>上传自己的图片素材，在建议区域进行构图或许效果更好哦~</h4>
        </div>
        <div class="editor-preview__img">
          <img :src="previewSrc" :style="filterObject" ref="img" />
        </div>
      </div>
      <div class="editor-edit">
        <div>
          <el-button type="danger" class="upload-btn btn-default" @click="uploadImage">上传或替换图片</el-button>
          <p class="editor-edit__tips">如有需要可在下方调整图片色调</p>
        </div>
        <div>
          <el-collapse v-model="activeNames" @change="handleChange" class="el-collapse-root">
            <el-collapse-item name="1">
              <template slot="title">
                <span class="editor-collapse__title">基础调整</span>
              </template>
              <div class="editor-collapse-item">
                <span>对比度</span>
                <span>{{ contrast }}</span>
              </div>
              <el-slider v-model="contrast"></el-slider>
              <div class="editor-collapse-item">
                <span>亮度</span>
                <span>{{ brightness }}</span>
              </div>
              <el-slider v-model="brightness"></el-slider>
              <div class="editor-collapse-item">
                <span>饱和度</span>
                <span>{{ saturation }}</span>
              </div>
              <el-slider v-model="saturation"></el-slider>
            </el-collapse-item>
            <el-collapse-item name="2">
              <template slot="title">
                <span class="editor-collapse__title">滤镜效果</span>
              </template>
            </el-collapse-item>
          </el-collapse>
        </div>
        <div>
          <el-button class="btn-default prev-btn" @click="prevStep">上一步</el-button>
          <el-button type="danger" class="btn-default next-btn" @click="download">保存</el-button>
        </div>
      </div>
    </div>
    <div class="tabbar">
      <p class="tabbar-title">选择制定图片进行编辑</p>
      <div class="tabbar-tab">
        <div class="tabbar-tab__item" :class="{ currentTab: index === activeIndex }" v-for="(tabItem, index) of tabData" :key="index" @click="handleTabClick(tabItem, index)">
          <div></div>
          <div>{{ tabItem.paramName }}</div>
        </div>
      </div>
    </div>
    <!-- 上传图片 -->
    <el-dialog
      top="136px"
      width="46%"
      class="upload-dialog"
      title="提示"
      :visible.sync="dialogVisible"
      :before-close="beforeClose">
      <div>
        <el-upload
          :auto-upload="false"
          v-if="option.img === ''"
          class="upload-image"
          drag
          :on-change="uploadChange"
          action="https://jsonplaceholder.typicode.com/posts/"
          multiple>
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
        <div v-else class="upload-cropper">
          <vueCropper
            ref="cropper"
            :img="option.img"
            :outputSize="option.size"
            :outputType="option.outputType"
            :info="true"
            :full="option.full"
            :canMove="option.canMove"
            :canMoveBox="option.canMoveBox"
            :original="option.original"
            :autoCrop="option.autoCrop"
            :autoCropWidth="option.autoCropWidth"
            :autoCropHeight="option.autoCropHeight"
            :fixedBox="option.fixedBox"
            @realTime="realTime"
            @imgLoad="imgLoad"
          ></vueCropper>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleUpload">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex'
import { VueCropper } from 'vue-cropper'
import { blobToDataUrl, dataURLtoFile } from '@/assets/js/utils'
import { uploadFile, generateCover, downloadFile } from '@/api/api'
export default {
  components: {
    VueCropper
  },
  props: ['templateKid', 'templateName'],
  data() {
    return {
      tabLen: -1,
      coverFormData: {},
      activeIndex: -1,
      currentTab: null,
      file: '',
      previewSrc: '',
      option: {
        img: '',
        outputSize: 1, // 剪切后的图片质量（0.1-1）
        full: false, // 输出原图比例截图 props名full
        outputType: 'png',
        canMove: true,
        original: false,
        canMoveBox: true,
        autoCrop: true,
        autoCropWidth: 960,
        autoCropHeight: 600,
        fixedBox: true
      },
      dialogVisible: false,

      activeNames: ['1'],
      // 对比度
      contrast: 50,
      // 亮度
      brightness: 50,
      // 颜色饱和度
      saturation: 50
    }
  },
  computed: {
    ...mapGetters([
      'previewImage',
      'tabData'
    ]),
    ...mapState([
      'cropperFormData'
    ]),
    filterObject () {
      return {
        filter: `brightness(${this.brightness / 50})
          contrast(${this.contrast / 50})
          saturate(${this.saturation / 50})
        `
      }
    }
  },
  methods: {
    handleTabClick(tab, index) {
      this.activeIndex = index
      this.currentTab = tab
    },
    download() {
      if (this.tabLen > 0) {
        this.$message.warning('你还有图片未上传编辑，不能保存！')
        return
      }
      // const maxWidth = 1080
      // const maxHeight = 960
      // let targetWidth = 0
      // let targetHeight = 0
      const filterImg = this.$refs.img
      filterImg.setAttribute('crossOrigin', 'anonymous')
      const { naturalWidth, naturalHeight } = filterImg
      const canvas = document.createElement('canvas')
      // if (naturalWidth >= maxWidth || naturalHeight >= maxHeight) {
      //   if (naturalWidth / naturalHeight > maxWidth / maxHeight) {
      //     targetWidth = maxWidth
      //     targetHeight = Math.round(maxWidth * (naturalHeight / naturalWidth))
      //   } else {
      //     targetHeight = maxHeight
      //     targetWidth = Math.round(maxHeight * (naturalWidth / naturalHeight))
      //   }
      // }
      canvas.width = naturalWidth
      canvas.height = naturalHeight
      const ctx = canvas.getContext('2d')
      const filterStr = `brightness(${this.brightness / 100})
        contrast(${this.contrast / 100})
        saturate(${this.saturation / 100})
      `
      ctx.filter = filterStr.trim()
      ctx.drawImage(this.$refs.img, 0, 0, canvas.width, canvas.height)
      const link = document.createElement('a')
      link.href = canvas.toDataURL('', 1)
      link.download = Date.now()
      link.click()
    },
    uploadChange (file, fileList) {
      blobToDataUrl(file.raw, dataUrl => {
        this.option.img = dataUrl
      })
    },
    uploadImage () {
      if (this.activeIndex >= 0) {
        this.dialogVisible = true
      } else {
        this.$message.error('请选择要编辑的图片上传！')
      }
    },
    prevStep () {
      this.$router.go(-1)
    },
    beforeClose (done) {
      console.log('close')

      done()
    },
    async handleUpload () {
      // 上传图片
      let formData = new FormData()
      formData.append('file', this.file)
      formData.append('uploadPath', 'temp')
      let a = await uploadFile(formData)
      // 生成封面
      let formData1 = {
        templateKid: this.templateKid,
        printSize: 'bilibili',
        data: {
          ...this.coverFormData,
          [this.currentTab.paramName]: a
        }
      }
      let b = await generateCover(formData1)
      // 下载回来显示，因为直接显示b会有canvas跨域问题
      let c = await downloadFile({
        name: b,
        type: 'temp'
      }, { responseType: 'blob' })
      console.log(c)
      let blob = new Blob([c], { type: `${c.type}` })
      this.previewSrc = URL.createObjectURL(blob)
      this.coverFormData = Object.assign({}, formData1.data)
      this.dialogVisible = false
      this.file = ''
      this.option.img = ''
      this.tabLen--
    },
    handleChange () {

    },
    realTime (data) {
      this.$refs.cropper.getCropBlob((data1) => {
        blobToDataUrl(data1, base64 => {
          this.file = dataURLtoFile(base64, '上传')
          console.log(this.file)
        })
      })
    }
  },
  watch: {
    previewImage (newImage) {
      this.previewSrc = newImage
    }
  },
  created () {
    this.previewSrc = this.previewImage
    this.coverFormData = this.cropperFormData
    this.tabLen = this.tabData.length
  }
}
</script>
<style lang="less" scoped>
.firstHandle {
  padding-bottom: 36px + 90px + 20px;
  background-color: rgb(245, 245, 245);
  .editor {
    width: 960px + 64px + 340px;
    margin: 0 auto;
    &::after {
      content: '';
      display: table;
      clear: both;
    }
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
        img {
          width: 100%;
          height: 600px;
        }
      }
    }

    &-edit {
      float: right;
      width: 340px;
      margin-top: 56px;
      font-size: 24px;
      .el-collapse-root {
        /deep/ .el-collapse-item__wrap,
        /deep/ .el-collapse-item__header {
          background-color: #f5f5f5;
        }

        /deep/ .el-collapse-item__header {
          padding: 23px 0;
        }
      }
      .btn-default {
        width: 100%;
        margin-top: 10px;
        font-size: 24px;
        border-radius: 0;
        padding-top: 14px;
        padding-bottom: 14px;
      }
      .upload-btn {
        background-color: #EE3554;
      }
      .prev-btn {
        margin-top: 40px;
      }
      .next-btn {
        margin-left: 0;
        background-color: #EE3554;
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

    &-collapse {
      &-item {
        line-height: 32px;
        font-size: 24px;
        display: flex;
        justify-content: space-between;
        span {
          color: #666666;
        }
      }
      &__title {
        font-size:36px;
        font-weight:bold;
        line-height:48px;
        color: #000;
      }
    }
  }
  .upload-dialog {
    .upload-image {
      text-align: center;
    }
    .upload-cropper {
      height: 400px;
    }
  }
  .tabbar {
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    background-color: #fff;
    &-title {
      height: 36px;
      line-height: 1;
      font-size:12px;
      color: #003333;
      text-indent: 1em;
      margin-top: 10px;
      margin-bottom: 0;
    }
    &-tab {
      height: 90px;
      display: flex;
      &__item {
        margin-left: 12px;
        div:first-child {
          border: 1px solid #ccc;
          width: 80px;
          height: 50px;
        }
        div:last-child {
          border-left: 1px solid #ccc;
          border-right: 1px solid #ccc;
          border-bottom: 1px solid #ccc;
          width: 80px;
          height: 20px;
          text-align: center;
          color: rgb(153, 153, 153);
          font-size: 12px;
          line-height: 16px;
        }
      }
      .currentTab {
        div {
          color: red;
          border: 1px solid red;
        }
      }
    }
  }

}

</style>
