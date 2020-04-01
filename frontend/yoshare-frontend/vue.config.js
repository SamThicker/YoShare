// vue.config.js
module.exports = {
  devServer: {
    host: "127.0.0.1",
    port: "8081",
    disableHostCheck: true,
    proxy: {
      "/api": {
        target: "http://127.0.0.1:9012", //对应自己的接口
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          "^/api": ""
        }
      },
      "/res": {
        target: "http://127.0.0.1:9000", //对应自己的接口
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          "^/res": ""
        }
      },
      "/raw": {
        target: "http://127.0.0.1", //对应自己的接口
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          "^/raw": ""
        }
      }
    }
  }
};
