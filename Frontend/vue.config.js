// 从 @vue/cli-service 导入 defineConfig 工具
const { defineConfig } = require('@vue/cli-service')

// 导出配置(用 defineConfig 包裹可以获得 IDE 智能提示)
module.exports = defineConfig({
  // 让 babel 编译 node_modules 中的依赖(某些依赖需要转译)
  transpileDependencies: true,

  // 通过链式 API 修改 webpack 配置
  chainWebpack: config => {
    // 修改 html-webpack-plugin 插件
    config.plugin('html').tap(args => {
      args[0].template = 'public/app.html'  // 模板文件路径
      args[0].title = 'StLforlove'          // 页面标题
      return args
    })
  }
})