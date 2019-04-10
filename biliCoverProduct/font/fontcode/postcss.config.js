module.exports = {
  plugins: {
    autoprefixer: {},
    'postcss-px-to-viewport': {
      unitToConvert: 'px',
      viewportWidth: 1920,
      unitPrecision: 5,
      propList: ['*', '!font'],
      viewportUnit: 'vw',
      fontViewportUnit: 'vw',
      minPixelValue: 1,
      mediaQuery: false,
      replace: true,
      selectorBlackList: ['el-', '.input__title'],
      exclude: [],
      landscape: false,
      landscapeUnit: 'vw',
      landscapeWidth: 568
    }
  }
}
