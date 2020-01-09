<template>
  <div class="home">
    <div class="section">
      <h1 class="site-title">BiliCover</h1>
      <h2 class="caption">用简单的方式</h2>
      <h2 class="caption">完成吸引眼球的封面</h2>
      <p class="desc">我们提供专业的封面模板设计</p>
      <p class="desc">简单二步助你完成更受关注的封面</p>
      <ol class="step">
        <li>选取一个模板</li>
      </ol>
      <div class="progress-bar">
        <div class="progress-bar__item"></div>
        <div class="progress-bar__item"></div>
        <div class="progress-bar__item"></div>
      </div>
      <el-button  @click="toSearch" class="baseButton">开始制作</el-button>
    </div>
    <div class="swiper-wrap">
      <div class="swiper" ref="swiper">
        <div class="swiper-item">
          <div class="first-step"></div>
        </div>
        <div class="swiper-item">
          <div class="second-step"></div>
        </div>
        <div class="swiper-item">
          <div class="third-step"></div>
        </div>
      </div>
    </div>
    <div class="footer">
      <a href="http://www.beian.miit.gov.cn" target="_blank">©2020 BiliCover 粤ICP备19032327号</a>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      message: 'dsgfusf',
      index: 0,
      timer: null
    }
  },
  methods: {
    toSearch() {
      this.$router.push({ name: 'search' })
    },
    next () {
      this.index++
      this.$refs.swiper.style.transform = `translateX(${-23.125 * this.index}vw)`

      if (this.index > 2) {
        this.index = 0
        this.$refs.swiper.style.transform = `translateX(${-23.125 * this.index}vw)`
      }
    }
  },
  mounted() {
    this.timer = setInterval(this.next, 4000)
  },
  destroyed () {
    clearInterval(this.timer)
    this.timer = null
  }
}
</script>
<style lang="less" scoped>
.home {
  height: 100%;
  background: url(~@/assets/img/bg.jpg) left center no-repeat;
  background-size: 100%;
  .swiper-wrap {
    width: 444px;
    overflow: hidden;
    position: absolute;
    top: 280px;
    left: 1100px;
    .swiper {
      width: 444px * 3;
      height: 300px;
      transition: all 2s;
      &-item {
        width: 444px;
        height: 300px;
        display: inline-block;
        .first-step {
          height: inherit;
          background: url(~@/assets/img/选择.png) left center no-repeat;
          background-size: 100%;
        }
        .second-step {
          height: inherit;
          background: url(~@/assets/img/上传.png) left center no-repeat;
          background-size: 100%;
        }
        .third-step {
          height: inherit;
          background: url(~@/assets/img/替换文字.png) center center no-repeat;
          background-size: 304px 227px;
        }
      }
      .next {
        transform: translateX(444px);
      }
    }
  }
  .section {
    display: inline-block;
    position: relative;
    left: 250px;
    top: 80px;
    .site-title {
      font-size:48px;
      font-weight:bold;
      line-height: 64px;
      color:#fff;
      margin-top: 0;
      text-indent: 0.6em;
      &::before {
        content: '';
        border-left: 3px solid #fff;
        position: relative;
        left: -0.6em;
        top: -0.12em;
        font-size: 0.7em;
      }
    }
    .caption {
      font-size:60px;
      font-weight:bold;
      line-height:80px;
      color:#fff;
      margin: 0;
      &:nth-of-type(1) {
        margin-top: 80px;
      }
    }
    .desc,
    .step {
      margin: 0;
      font-size:24px;
      font-weight:300;
      line-height:32px;
      color:rgb(153,153,153);
    }
    .step {
      margin-top: 45px;
      padding-left: 1em;
    }
    .desc {
      &:nth-of-type(1) {
        margin-top: 26px;
      }
    }
    .progress-bar {
      &__item {
        width: 60px;
        height: 4px;
        background-color: #EE3554;
        display: inline-block;
        margin-right: 20px;
      }
    }
  }
  .baseButton {
    width: 260px;
    height: 66px;
    background-color: #EE3554;
    color: #FFFFFF;
    font-size: 24px;
    margin-top: 40px;
    border: none;
  }
  .footer {
    position: fixed;
    left: 50%;
    bottom: 20px;
    transform: translateX(-50%);
    a {
      text-decoration: none;
      color: #fff;
    }
  }
}
</style>
