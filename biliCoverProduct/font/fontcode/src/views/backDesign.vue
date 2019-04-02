<template>
  <div class="backDesign">
    <div class="backDesign__header">
      <div class="backDesign__header__top">
        <div class="backDesign__header__top__logo">
          BiliCover
        </div>
        <div class="backDesign__header__top__control">
          <el-switch v-model="isModifyParameter"  inactive-color="#ff4949" active-color="#13ce66"></el-switch>
          <span class="control__text">调整动态参数</span>
          <div class="control__type">
            <span :class="{ 'control__type__text': true, 'control__type__active': chosenType.type === item.type}" v-for="(item, index) in typeText" :key="item.name + index" @click="selectType(item)">{{item.name}}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="backDesign__content">
      <div class="backDesign__content__left">
        <el-popover
          v-for="(layer, index) in layers" :key="layer.name + index"
          placement="right-start"
          trigger="hover">
          <el-button-group>
            <el-button type="info" icon="el-icon-plus" size="mini" @click="addLayer(index)"></el-button>
            <el-button type="info" icon="el-icon-minus" size="mini" @click="deleteLayer(index)" v-if="layers.length !==1"></el-button>
          </el-button-group>
          <div slot="reference">
            <span :class="{ 'backDesign__content__left__spot': true, 'hover__spot': index === chosenIndex}">&#8226;</span>
            <div :class="{ 'backDesign__content__left__layer': true, 'hover__layer': index === chosenIndex}" @click="selectLayer(index)">{{layer.title}}</div>
            <div class="layer-line" v-if="index !== layers.length - 1"></div>
          </div>
        </el-popover>
      </div>
      <div class="backDesign__content__middle">
        <div class="backDesign__content__middle__showImageButtons">
          <el-button type="text" @click="toggle" :class="{ showImageButton: true, activeShowImageButton: nowImage === '实时效果'}">实时效果</el-button>
          <el-button type="text" @click="toggle" :class="{ showImageButton: true, activeShowImageButton: nowImage === '展示效果'}">展示效果</el-button>
        </div>
        <img v-if="nowImage === '实时效果'" :src="nowImgSrc" />
        <img v-if="nowImage === '展示效果'" :src="showImgSrc" />
        <div class="backDesign__content__middle__saveButtons">
          <el-button @click="save">保存</el-button>
          <el-button @click="setPreview">设为预览效果</el-button>
        </div>
      </div>
      <div class="backDesign__content__right">
        <div class="backDesign__content__right__inputarea" v-if="chosenType.type !== 'introduce'">
          <div class="backDesign__content__right__inputarea__name">
            <span class="input__title">图层名字</span>
            <el-input v-model="chosenType.title"></el-input>
          </div>
          <div class="backDesign__content__right__inputarea__item" v-for="(value, key) in chosenType.data" :key="key">
            <el-popover
              v-if="isModifyParameter"
              placement="right"
              title="填写变量名字"
              trigger="hover">
              <el-input v-model="chosenType.data[key].paramName" required></el-input>
               <el-checkbox slot="reference" v-model="chosenType.data[key].isDynamic"><span :class="{'input__title': true, 'dynamic__prop': chosenType.data[key].isDynamic}">{{key}}</span></el-checkbox>
            </el-popover>
            <span v-else :class="{'input__title': true, 'dynamic__prop': chosenType.data[key].isDynamic}">{{key}}</span>
            <template v-if="chosenType.data[key].inputType === 'input'" >
              <el-input v-model="chosenType.data[key].defaultValue"></el-input>
            </template>
            <div class="input__content" v-if="chosenType.data[key].inputType === 'inputNumber'" >
              <el-input-number v-model="chosenType.data[key].defaultValue" :controls="false"></el-input-number>
            </div>
            <template v-if="chosenType.data[key].inputType === 'image'" >
              <el-input v-model="chosenType.data[key].defaultValue" :hidden="true"></el-input>
              <el-upload
                ref="uploadImage"
                class="upload-demo"
                :data="{uploadPath: 'uploadFile'}"
                action="proxyUrl/io/chunkUpload"
                :file-list="chosenType['data'][key][`fileList`]"
                :limit="1"
                :before-remove="beforeRemove(key)"
                :on-change="changeImage(key)"
                :on-success="imageUploadSuccess(key)"
                :auto-upload="true"
                :on-exceed="handleExceed"
                list-type="picture">
                <el-button size="small" type="primary" slot="trigger">选择图片</el-button>
              </el-upload>
            </template>
            <template v-if="chosenType.data[key].inputType === 'color'" >
              <el-color-picker v-model="chosenType.data[key].defaultValue" show-alpha></el-color-picker>
            </template>
            <template v-if="chosenType.data[key].inputType === 'select'" >
              <el-select v-model="chosenType.data[key].defaultValue" placeholder="请选择">
                <el-option
                  v-for="item in inputOptions[chosenType.type][key]"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </template>
          </div>
        </div>
        <div v-else class="backDesign__content__right__inputarea">
          <div class="backDesign__content__right__inputarea__item" v-for="(value, key) in introduce" :key="key">
            <span :class="{'input__title': true}">{{key}}</span>
            <el-input v-model="introduce[key]"></el-input>
          </div>
        </div>
        <el-button @click="saveLayer">保存</el-button>
      </div>
    </div>
  </div>
</template>

<script >
import inputType from './backDesignInputType.js'
import inputOptions from './backDesignInputOptions.js'

export default {

  data() {
    return {
      previewImageUrl: '',
      nowImage: '实时效果',
      nowImgSrc: 'http://www.w3school.com.cn/i/eg_tulip.jpg',
      showImgSrc: 'https://www.baidu.com/img/bd_logo1.png?where=super',
      isModifyParameter: false,
      inputOptions: inputOptions,
      introduce: {
        templateName: '测试模板',
        templateType: '测试类型',
        introduction: '我是简介'
      },
      typeText: [
        {
          title: '图片',
          name: '图片',
          type: 'image',
          data: {
            imagePath: '', // 图片路径
            x: '0', // 坐标x
            y: '0', // 坐标y
            width: '1600', // 宽度
            height: '1000' // 高度
          }
        },
        {
          title: '文字',
          name: '文字',
          type: 'text',
          data: {
            verticalText: 'false', // 是否垂直文字
            fontName: '田氏颜体大字库', // 字体名
            fontStyle: '2', // 字体样式
            fontSize: '100', // 字体尺寸
            textColor: 'rgba(0, 0, 0, 1)', // 字体颜色，格式例子rgba(19, 206, 102, 0.8)
            borderColor: 'rgba(0, 0, 0, 0)', // 描边颜色，格式例子rgba(19, 206, 102, 0.8)
            borderWidth: '10', // 描边宽度
            positionX: '0', // 文本框x坐标
            positionY: '300', // 文本框y坐标
            textAreaWidth: '800', // 文本框宽度，用于计算对齐
            textAreaHeight: '100', // 文本框高度，用于计算对齐
            horizontalAlign: 'center', // 水平对齐，left:左对齐 right:右对齐 center: 居中
            verticalAlign: 'center', // 垂直对齐, top:上对齐 bottom:下对齐 center: 居中
            text: '', // 文字内容
            gradientPaintX1: '300', // 渐变点1X坐标，如果不需要渐变则为null
            gradientPaintY1: '300', // 渐变点1Y坐标，如果不需要渐变则为null
            gradientPaintColor1: 'rgba(0, 0, 0, 0)', // 渐变点1颜色，如果不需要渐变则为null
            gradientPaintX2: '400', // 渐变点2X坐标，如果不需要渐变则为null
            gradientPaintY2: '400', // 渐变点2Y坐标，如果不需要渐变则为null
            gradientPaintColor2: 'rgba(0, 0, 0, 0)' // 渐变点2颜色，如果不需要渐变则为null
          }
        },
        {
          title: '遮罩',
          name: '遮罩',
          type: 'generalPath',
          data: {
            type: 'moveTo', // 根据操作类型来进行不同的方法 moveTo:移动到某点 lineTo:连线到某点 quadTo: 二次曲线片段 curveTo：三次曲线片段 closePath：闭合遮罩 reset: 重置遮罩，恢复整个页面
            pointX: '500', // 点的X坐标
            pointY: '500', // 点的Y坐标
            bezierX1: '500', // 二次曲线片段扭曲点X
            bezierY1: '500', // 二次曲线片段扭曲点Y
            bezierX2: '500', // 三次曲线片段扭曲点X
            bezierY2: '500'// 三次曲线片段扭曲点Y
          }
        },
        {
          title: '旋转',
          name: '旋转',
          type: 'rotate',
          data: {
            x: '0', // 围绕x坐标旋转
            y: '0', // 围绕y坐标旋转
            theta: '-10' // 旋转角度，可负数
          }
        },
        {
          title: '信息',
          name: '信息',
          type: 'introduce'
        }
      ],
      chosenType: {
        title: '图片',
        name: '图片',
        type: 'image',
        data: {
          imagePath: '', // 图片路径
          x: '0', // 坐标x
          y: '0', // 坐标y
          width: '1600', // 宽度
          height: '1000' // 高度
        }
      },
      chosenIndex: 0,
      layers: [],
      layersDynamicParameter: []
    }
  },
  methods: {
    toggle() {
      this.nowImage = this.nowImage === '实时效果' ? '展示效果' : '实时效果'
    },
    changeImage(key) {
      return (file, fileList) => {
        console.log(file, fileList, this.chosenType)
        this.chosenType.data[key].fileList = fileList
      }
    },
    beforeRemove(key) {
      let that = this
      return (file) => {
        if (!that.chosenType.data[key].defaultValue) {
          return
        }
        that.$axios
          .delete('proxyUrl/io', {
            params: {
              delKids: that.chosenType.data[key].defaultValue
            }
          })
          .then(response => {
            console.log(response)
          })
          .catch(function(error) {
            console.log(error)
          })
      }
    },
    imageUploadSuccess(key) {
      let that = this
      return (response, file, fileList) => {
        if (response.code !== 200) {
          that.$refs.uploadImage[0].clearFiles()
          that.$message.warning(`${response.message}`)
          return
        }
        that.chosenType.data[key].defaultValue = response.result
      }
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    returnInputType(type, key) {
      return inputType[type][key]
    },
    deepClone(obj) {
      return JSON.parse(JSON.stringify(obj))
    },
    selectType(item) {
      if (item.type === 'introduce') {
        this.chosenType = this.typeText[4]
        return
      }
      if (item.type === this.layers[this.chosenIndex].type) {
        this.chosenType = this.layers[this.chosenIndex]
      } else {
        this.chosenType = this.handleLayerObj(item)
      }
      // console.log(this.chosenType)
    },
    saveLayer() {
      if (this.chosenType.type === 'introduce') {
        return
      }
      this.layers.splice(this.chosenIndex, 1, this.chosenType)
      this.preview()
    },
    getDynamicParameterObj(obj, type) {
      let cloneObj = this.deepClone(obj)
      let dynamicParameter = {}
      for (let[key, value] of Object.entries(cloneObj)) {
        dynamicParameter[key] = {
          isDynamic: false,
          inputType: this.returnInputType(type, key),
          paramType: key === 'imagePath' ? 'image' : 'input',
          defaultValue: value,
          paramName: ''
        }
        // 上传图片类型加入文件选择属性
        if (this.returnInputType(type, key) === 'image') {
          dynamicParameter[key].fileList = []
        }
      }
      return dynamicParameter
    },
    handleLayerObj(obj) {
      let cloneObj = this.deepClone(obj)
      let handleObj = {}
      handleObj.title = cloneObj.title
      handleObj.name = cloneObj.name
      handleObj.type = cloneObj.type
      handleObj.data = this.getDynamicParameterObj(cloneObj.data, cloneObj.type)
      return handleObj
    },
    handlePreviewLayersObj(obj) {
      let coverCodeObj = []
      let coverTemplateParams = []
      // eslint-disable-next-line
      for (let [index, value] of obj.entries()) {
        let tempalteObj = {}
        tempalteObj.title = value.title
        tempalteObj.name = value.name
        tempalteObj.type = value.type
        tempalteObj.data = {}
        for (let [key, item] of Object.entries(value.data)) {
          tempalteObj.data[key] = item.defaultValue
        }
        coverCodeObj.push(tempalteObj)
      }
      return {
        templateCode: coverCodeObj,
        ...this.introduce
      }
    },
    handleResultLayersObj(obj) {
      let coverCodeObj = []
      let coverTemplateParams = []
      // eslint-disable-next-line
      for (let [index, value] of obj.entries()) {
        let tempalteObj = {}
        tempalteObj.title = value.title
        tempalteObj.name = value.name
        tempalteObj.type = value.type
        tempalteObj.data = {}
        for (let [key, item] of Object.entries(value.data)) {
          // tempalteObj.data[key] = item.defaultValue
          if (item.isDynamic && !!item.paramName.trim()) {
            coverTemplateParams.push(
              {
                paramName: item.paramName,
                paramType: item.paramType,
                defaultValue: item.defaultValue
              }
            )
            tempalteObj.data[key] = `$\{${item.paramName}}`
          } else {
            tempalteObj.data[key] = item.defaultValue
          }
        }
        coverCodeObj.push(tempalteObj)
      }
      return {
        coverTemplate: {
          templateCode: coverCodeObj,
          ...this.introduce
        },
        coverTemplateParams: coverTemplateParams
      }
    },
    addLayer(index) {
      this.layers.splice(index + 1, 0, this.handleLayerObj(this.typeText[0]))
      this.selectLayer(index + 1)
    },
    deleteLayer(index) {
      this.layers.splice(index, 1)
      if (index === 0) {
        this.selectLayer(0)
      } else {
        this.selectLayer(index - 1)
      }
    },
    selectLayer(index) {
      this.chosenType = this.layers[index]
      this.chosenIndex = index
    },
    save() {
      console.log(this.layers)
      console.log(this.handleResultLayersObj(this.layers))
      let submitObj = this.handleResultLayersObj(this.layers)
      this.$axios
        .post('proxyUrl/cover', submitObj)
        .then(response => {
          console.log(response)
        })
        .catch(function(error) {
          console.log(error)
        })
    },
    setPreview() {
      let that = this
      this.$axios
        .put('proxyUrl/cover/previewImage', { previewImage: this.previewImageUrl })
        .then(response => {
          console.log(response)
          that.nowImgSrc = 'proxyUrl' + response.data.result
          // that.$set(that, 'showImgSrc', 'proxyUrl' + response.data.result)
        })
        .catch(function(error) {
          console.log(error)
        })
    },
    preview() {
      let submitObj = this.handlePreviewLayersObj(this.layers)
      let that = this
      this.$axios
        .post('proxyUrl/cover/preview', {templateCode: submitObj.templateCode})
        .then(response => {
          that.previewImageUrl = response.data.result
          that.showImgSrc = 'proxyUrl/temp/预览.png'
          // that.showImgSrc = 'proxyUrl' + response.data.result
        })
        .catch(function(error) {
          console.log(error)
        })
    }
  },
  mounted() {
    this.addLayer(-1)
  }
}
</script>
<style lang="less" scoped>
.backDesign {
  height: 100%;
  &__header {
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
          // width: 30px;
          height: 20px;
        }
        .control__text {
          margin-left: 10px;
          margin-right: 40px;
          font-size: 14px;
          color: #CCCCCC;
        }
        .control__type {
          display: inline-flex;
          width: 300px;
          justify-content: center;
          &__text {
            color: #CCCCCC;
            font-size: 14px;
            margin-left: 30px;
            position: relative;
            cursor: pointer;
            &:nth-child(1) {
              margin-left: 0;
            }
            &:hover {
              &:after {
                content: '';
                height: 2px;
                background: rgba(255,255,255,0.7);
                position: absolute;
                width: 100%;
                top: 100%;
                margin-top: 7px;
                left: 0;
              }
            }
          }
          &__active.control__type__text {
            &:after {
              content: '';
              height: 2px;
              background: rgba(255,255,255,1);
              position: absolute;
              width: 100%;
              top: 100%;
              margin-top: 7px;
              left: 0;
            }
          }
        }
      }
    }
  }
  &__content {
    height: calc(100% - 60px);
    display: flex;
    &__left {
      box-sizing: border-box;
      width: 100px;
      height: 100%;
      padding-top: 20px;
      background-color: #F5F5F5;
      // padding-left: 23px;
      &__spot {
        margin-left: 23px;
        color: #999999;
        font-size: 14pt
      }
      .hover__spot {
        color: #333333
      }
      &__layer {
        margin-left: 5px;
        color: #999999;
        display: inline-block;
        font-size: 14pt;
      }
      .hover__layer {
        color: #333333
      }
      .layer-line {
        width: 0;
        height: 30px;
        border: 1px solid #CCCCCC;
        margin-left: 25px;
        margin-top: 5px;
        margin-bottom: 5px;
      }
    }
    &__middle {
      width: calc(100% - 400px);
      height: 100%;

      background-color: #E4E4E4;
      &__showImageButtons {
        display: flex;
        flex-wrap: nowrap;
        .showImageButton {
          color: #333333;
        }
        .activeShowImageButton {
          color: #999999;
        }
      }
    }
    &__right {
      width: 300px;
      height: 100%;
      display: flex;
      flex-direction: column;
      background-color: #F5F5F5;
    }
  }
}
.input__title {
  display: block;
  font-size: 12pt;
  color: #999999;
  margin-left: 20px;
  margin-top: 15px;
}
.dynamic__prop {
  color: red;
}
.input__content {
  // display: inline-block;
  margin-left: 20px;
}
</style>
