module.exports = {
  title: '航信商务管理系统-V1.0',
  vision: 'V1.0.0.20220508001',
  clientId: 'product', // 客户端id
  clientSecret: 'product_secret', // 客户端密钥
  tenantMode: false, // 开启租户模式
  captchaMode: false, // 开启验证码模式
  navTheme: 'dark', // theme for nav menu
  primaryColor: '#1890FF', // primary color of ant design
  layout: 'sidemenu', // nav menu position: sidemenu or topmenu
  contentWidth: 'Fluid', // layout of content: Fluid or Fixed, only works when layout is topmenu
  fixedHeader: true, // sticky header
  autoHideHeader: false, // auto hide header
  fixSiderbar: true, // sticky siderbar
  collapse: true,
  menu: {
    disableLocal: false,
  },
  pwa: true,
};
