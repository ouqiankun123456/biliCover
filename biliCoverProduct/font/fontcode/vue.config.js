module.exports = {
  devServer: {
    port: 9090,
    proxy: {
      '/proxyUrl': {
        // target: 'http://238o4s4873.zicp.vip:57014',
        target: 'http://120.79.0.225/back',
        changeOrigin: true,
        pathRewrite: {
          '^/proxyUrl': ''
        }
      }
    }
  }
}
