import {routerRedux} from 'dva/router';
import {stringify} from 'qs';
import {getFakeCaptcha,loginout} from '../services/api';
import {accountLogin} from '../services/user';
import {removeAll, setAccessToken, setAuthority, setCurrentUser, setProduct, setToken} from '../utils/authority';
import {reloadAuthorized} from '../utils/Authorized';
import avatar_img from '../../public/BiazfanxmamNRoxxVxka.png';
import { getProduct } from '@/utils/authority';
import { delAllCookie } from "@/utils/utils";

export default {
  namespace: 'login',

  state: {
    status: undefined,
  },

  effects: {
    *login({ payload }, { call, put }) {
      const response = yield call(accountLogin, payload);
      if (response.success) {
        const { success, data } = response;
        yield put({
          type: 'changeLoginStatus',
          payload: {
            status: success,
            type: 'login',
            data: { ...data },
          },
        });
        // const responseRoutes = yield call(dynamicRoutes);
        // const responseButtons = yield call(dynamicButtons);
        // yield put({
        //   type: 'saveMenuData',
        //   payload: {
        //     routes: responseRoutes.data,
        //     buttons: responseButtons.data,
        //   },
        // });
        reloadAuthorized();
        // const urlParams = new URL(window.location.href);
        // const params = getPageQuery();
        // let { redirect } = params;
        // if (redirect) {
        //   const redirectUrlParams = new URL(redirect);
        //   if (redirectUrlParams.origin === urlParams.origin) {
        //     redirect = redirect.substr(urlParams.origin.length);
        //     if (redirect.match(/^\/.*#/)) {
        //       redirect = redirect.substr(redirect.indexOf('#') + 1);
        //     }
        //   } else {
        //     // window.location.href = redirect;
        //     return;
        //   }
        // }
       
        if(data && (data.roleid == '1123598816738675201' || data.roleid == '1123598816738675205')){
          //管理员角色调整至管理界面
          let productList = JSON.parse(getProduct());
          
          productList.map(item=>{            
            if(item.productCode == 'system-message'){              
              window.open(item.productPath,'_self');
            }
          })        
        }else if(data.roleid == '1123598816738675202'){
          //医生、分析人员  
          yield put(routerRedux.replace(  '/wessample'));
        // yield put(routerRedux.replace(  '/productChoose'));
        }else{
            //实验人员角色            
          yield put(routerRedux.replace(  '/tester'));
          // yield put(routerRedux.replace(  '/productChoose'));
        }
        
      }
    },

    *getCaptcha({ payload }, { call }) {
      yield call(getFakeCaptcha, payload);
    },

    // 退出登录
    *logout(_, { call, put }) {
      const response = yield call(loginout);
      if (response.success) {  
        yield put({
          type: 'changeLoginStatus',
          payload: {
            status: false,
            type: 'logout',
            data: {
              authority: 'guest',
              logout: true,
            },
          },
        });
        reloadAuthorized();
        // 清空sessionStorage
        sessionStorage.clear();
        // 清除cookie
        delAllCookie()
        // window.location.href = `${location.origin}`;
        yield put(
          routerRedux.push({
            pathname: '/user/login',
            search: stringify({
              redirect: window.location.href,
            }),
          })
        );
      }
    },
  },

  reducers: {
    changeLoginStatus(state, { payload }) {
      const { status, type } = payload;

      if (status) {
        const {
          data: { tokenType, accessToken, authority, account, userName, avatar,roleid,imgpath,productList },
        } = payload;
        const token = `${tokenType} ${accessToken}`;
        const newavatar = {avatar_img};
        setToken(token);
        setAccessToken(accessToken);
        setAuthority(authority);
        setProduct(productList);
        
        setCurrentUser({ avatar, account, name: userName, authority,roleid,imgpath });
      } else {
        removeAll();
      }

      return {
        ...state,
        status: type === 'login' ? (status ? 'ok' : 'error') : '',
        type: payload.type,
      };
    },
    saveMenuData(state, { payload }) {
      const { routes, buttons } = payload;
      // setRoutes(formatRoutes(routes));
      // setButtons(formatButtons(buttons));
      return {
        ...state,
      };
    },
  },
};
