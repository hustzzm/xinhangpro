export default [
  // user
  {
    path: '/other',
    component: '../layouts/OtherLayout',
    routes: [{
      path: '/other/analysis',
      component: './Wes/templateMg/index.js'
    },
    {
      path: '/other/explaindetail',
      component: './Wes/templateMg/index.js'
    },
    ],
  },
  {
    path: '/productChoose',
    component: '../layouts/ProductChooseLayout',
    routes: [{
      path: '/productChoose',
      redirect: '/productChoose/productChoose'
    },
    {
      path: '/productChoose/productChoose',
      component: './ProductChoose/ProductChoose'
    }
    ],
  },

  // {
  //     path: '/explain',
  //     component: './Wes/Explain/Test'
  //   },
  // {
  //   path: '/excelError',
  //   component: '../layouts/ErrorLayout',
  //   routes: [
  //     {path: '/excelError',  component: './Wes/ChipInfo/ExcelErrorHtml'},
  //   ],
  // },
  // user
  {
    path: '/user',
    component: '../layouts/UserLayout',
    routes: [{
      path: '/user',
      redirect: '/user/login'
    },
    {
      path: '/user/login',
      component: './Login/Login'
    },
    {
      path: '/user/register',
      component: './Login/Register'
    },
    {
      path: '/user/register-result',
      component: './Login/RegisterResult'
    },
    ],
  },
  // app
  {
    path: '/',
    component: '../layouts/BasicLayout',
    Routes: ['src/pages/Authorized'],
    authority: ['administrator', 'admin', 'user', 'test'],
    routes: [
      // dashboard
      {
        path: '/',
        component: './Wes/templateMg/index.js'
      },
      {
        path: '/result',
        routes: [
          // result
          {
            path: '/result/success',
            component: './Result/Success'
          },
          {
            path: '/result/fail',
            component: './Result/Error'
          },
        ],
      },
      {
        path: '/exception',
        routes: [
          // exception
          {
            path: '/exception/403',
            component: './Exception/403'
          },
          {
            path: '/exception/404',
            component: './Exception/404'
          },
          {
            path: '/exception/500',
            component: './Exception/500'
          },
          {
            path: '/exception/trigger',
            component: './Exception/TriggerException'
          },
        ],
      },
      {
        path: '/account',
        routes: [{
          path: '/account/center',
          component: './Account/Center/Center',
          routes: [{
            path: '/account/center',
            redirect: '/account/center/articles'
          },
          {
            path: '/account/center/articles',
            component: './Account/Center/Articles'
          },
          {
            path: '/account/center/applications',
            component: './Account/Center/Applications'
          },
          {
            path: '/account/center/projects',
            component: './Account/Center/Projects'
          },
          ],
        },
        {
          path: '/account/settings',
          //component: './Account/Settings/Info',
          routes: [{
            path: '/account/settings',
            redirect: '/account/settings/base'
          },
          {
            path: '/account/settings/base',
            component: './Account/Settings/BaseView'
          },
          {
            path: '/account/settings/password',
            component: './Account/Settings/PasswordView'
          },
          //{ path: '/account/settings/security', component: './Account/Settings/SecurityView' },
          //{ path: '/account/settings/binding', component: './Account/Settings/BindingView' },
          {
            path: '/account/settings/notification',
            component: './Account/Settings/NotificationView',
          },
          ],
        },
        ],
      },
      {
        path: '/dashboard',
        routes: [{
          path: '/dashboard',
          redirect: '/dashboard/analysis'
        },
        {
          path: '/dashboard/analysis',
          component: './member/memberindex.js'
        },
        ],
      },
      {
        /**1 房间管理 */
        path: '/room',
        routes: [{
          path: '/room',
          redirect: '/room/list'
        },
        {
          path: '/room/list',
          component: './room/roomindex.js'
        },
        ],
      },
      
      {
        /**2 会员管理 */
        path: '/tester',
        routes: [
          { path: '/tester',   redirect: '/tester/list'    },
          { path: '/tester/list',   component: './member/memberindex.js'},          
        ],
      },

      {
        /**3 订单管理 */
        path: '/order',
        routes: [{
          path: '/order',
          redirect: '/order/list'
        },
        {
          path: '/order/list',
          component: './order/orderindex.js'
        },
        ],
      },
      {
        /**4 预订管理 */
        path: '/book',
        routes: [{
          path: '/book',
          redirect: '/book/list'
        },
        {
          path: '/book/list',
          component: './order/bookindex.js'
        }
        ]
      },
      {
        /**5 商家管理 */
        path: '/company',
       
        routes: [{
          path: '/company',
          redirect: '/company/list',
        },
        {
          path: '/company/list',  
          component: './company/companyindex.js'
        },
        ],
      },
      {
        /**6 用户管理 */
        path: '/hxuser',
        routes: [{
          path: '/hxuser',
          redirect: '/hxuser/list'
        },
        {
          path: '/hxuser/list',
          component: './hxuser/hxuserindex.js'
        },
        // {
        //   path: '/wesreport/reportDetail',
        //   component: './gt/GtMember.js'
        // },
        // {
        //   path: '/wesreport/reportPreview',
        //   component: './gt/GtMember.js'
        // },
        ],
      },
      {
        /**7 调度配置 */
        path: '/gotask',
        routes: [{
          path: '/gotask',
          redirect: '/gotask/list'
        },
        {
          path: '/gotask/list',
          component: './Quartz/Quartz.js'
        } ,
        { path: '/gotask/list/add', component: './Quartz/QuartzAdd' }
        ],
      },
      
    
      {
        component: '404',
      },
    ],
  },
];