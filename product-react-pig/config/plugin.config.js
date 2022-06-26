// Change theme plugin

import MergeLessPlugin from 'antd-pro-merge-less';
import AntDesignThemePlugin from 'antd-theme-webpack-plugin';
import CompressWebpackPlugin from 'compression-webpack-plugin';
import path from 'path';

export default config => {
  // pro 和 开发环境再添加这个插件
  if (process.env.APP_TYPE === 'site' || process.env.NODE_ENV !== 'production') {
    // 将所有 less 合并为一个供 themePlugin使用
    const outFile = path.join(__dirname, '../.temp/ant-design-pro.less');
    const stylesDir = path.join(__dirname, '../src/');

    config.plugin('merge-less').use(MergeLessPlugin, [{
      stylesDir,
      outFile,
    },]);


    config.plugin('ant-design-theme').use(AntDesignThemePlugin, [{
      antDir: path.join(__dirname, '../node_modules/antd'),
      stylesDir,
      varFile: path.join(__dirname, '../node_modules/antd/lib/style/themes/default.less'),
      mainLessFile: outFile, //     themeVariables: ['@primary-color'],
      indexFileName: 'index.html',
      generateOne: true,
      // resolve:{
      //   alias:{
      //     'utils': path.resolve(__dirname, '../src/utils'),
      //   }
      // },
      lessUrl: 'zos/less.min.js',
    },]);
  }

  // if (process.env.NODE_ENV === 'production') {
  //   config.plugin('compression-webpack-plugin').use(
  //     new CompressWebpackPlugin({
  //       algorithm: 'gzip', //压缩算法
  //       test: new RegExp('\\.(' + ['js', 'css','less','ts'].join('|') + ')$'), //处理所有匹配此 {RegExp} 的资源
  //       threshold: 5120, //只处理比这个值大的资源。按字节计算
  //       minRatio: 0.8, //只有压缩率比这个值小的资源才会被处理,
  //     }),
  //   );
  // }
};