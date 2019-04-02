<template>
  <div style="height: 400px">
    <vueCropper
      @realTime="realTime"
      ref="cropper"
      v-bind="option"
    ></vueCropper>
  </div>
</template>
<script>
import { VueCropper } from 'vue-cropper'
import imgUrl from '@/assets/test.png'

export default {
  name: '',
  components: {
    VueCropper
  },
  props: {},
  data() {
    return {
      option: {
        img: imgUrl,
        outputSize: 1,
        outputType: 'png',
        autoCrop: true,
        canMove: false,
        full: true,
        centerBox: true
      }
    }
  },
  methods: {
    realTime (data) {
      console.log(data, 'data')
    }
  },
  computed: {}
}
</script>
<style lang='less' scoped>
</style>
