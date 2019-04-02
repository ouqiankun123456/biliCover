<template>
  <div class="firstHandle">
    <div class="editor">
      <div class="editor-preview">
        <div class="editor-preview__title">
          <h1>已选模板：国服最强狄仁杰</h1>
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
          <el-collapse v-model="activeNames" @change="handleChange">
            <el-collapse-item name="1">
              <template slot="title">
                <span class="editor-collapse__title">基础调整</span>
              </template>
              <el-slider v-model="contrast"></el-slider>
              <el-slider v-model="brightness"></el-slider>
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

    <!-- 上传图片 -->
    <el-dialog
      top="136px"
      width="46%"
      class="upload-dialog"
      title="提示"
      :visible.sync="dialogVisible"
      :before-close="handleClose">
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
        <el-button type="primary" @click="uploadImg">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { VueCropper } from 'vue-cropper'
export default {
  components: {
    VueCropper
  },
  data() {
    return {
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
    ...mapState([
      'cropperFormData',
      'previewImage'
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
    download() {
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
      console.log(file, fileList)
      this.blobToDataURL(file.raw, dataUrl => {
        this.option.img = dataUrl
      })
    },
    uploadImage () {
      this.dialogVisible = true
    },
    prevStep () {
      this.$router.push('/makeCover/secondHandle')
    },
    blobToDataURL(blob, callback) {
      let a = new FileReader()
      a.onload = function (e) { callback(e.target.result) }
      a.readAsDataURL(blob)
    },
    dataURLtoFile(dataurl, filename) { // 将base64转换为文件
      var arr = dataurl.split(','); var mime = arr[0].match(/:(.*?);/)[1]
      var bstr = atob(arr[1]); var n = bstr.length; var u8arr = new Uint8Array(n)
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
      }
      var reg = /.+\/(.+)$/
      let mimeArr = mime.match(reg)
      return new File([u8arr], filename + '.' + mimeArr[1], { type: mime })
    },
    uploadImg () {
      let formData = new FormData()
      formData.append('file', this.file)
      formData.append('uploadPath', 'temp')
      this.$axios.post('proxyUrl/io/chunkUpload', formData).then(res => {
        console.log('uploadimg', res)
        let data = {
          templateKid: this.cropperFormData.templateKid,
          data: {
            ...this.cropperFormData.data,
            // '图片1': encodeURI(res.data.result)
            '图片1': res.data.result
          },
          printSize: this.cropperFormData.printSize
        }
        this.$axios.post('proxyUrl/coverGenerator', data).then(res => {
          this.$axios.get('proxyUrl/io/download', {
            responseType: 'blob',
            params: {
              name: res.data.result,
              type: 'temp'
            }
          }).then(res => {
            let blob = new Blob([res.data], { type: `${res.data.type}` })
            this.previewSrc = URL.createObjectURL(blob)
            // this.setPreviewImage(this.coverImageSrc)
          })
          // this.previewSrc = 'http://238o4s4873.zicp.vip:57014/temp/' + res.data.result
          this.dialogVisible = false
        })
      })
    },
    handleClose () {

    },
    handleChange () {

    },
    realTime (data) {
      this.$refs.cropper.getCropBlob((data1) => {
        // do something
        this.blobToDataURL(data1, base64 => {
          this.file = this.dataURLtoFile(base64, '上传')
          console.log(this.file)
        })
      })
    }
  },
  created () {
    this.previewSrc = this.previewImage
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
}
</style>
