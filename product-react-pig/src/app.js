
import {  removeAll,getCurrentUser } from './utils/authority';
import { dynamicRoutesByProduct } from './services/menu';
import router from "umi/router";
export const dva = {
  config: {
    onError(err) {
      err.preventDefault();
    },
  },
};

let authRoutes = {
  '/form/advanced-form': { authority: ['admin', 'user'] },
};

function ergodicRoutes(routes, authKey, authority) {
  routes.forEach(element => {
    if (element.path === authKey) {
      if (!element.authority) element.authority = []; // eslint-disable-line
      Object.assign(element.authority, authority || []);
    } else if (element.routes) {
      ergodicRoutes(element.routes, authKey, authority);
    }
    return element;
  });
}

export function patchRoutes(routes) {

  if (authRoutes !== null && authRoutes !== undefined) {
    Object.keys(authRoutes).map(authKey =>
      ergodicRoutes(routes, authKey, authRoutes[authKey].authority)
    );
    window.g_routes = routes;
  }
  
}

export function render(oldRender) {

  let current_url = window.location.hash;
  let loginurl =window.location.origin + '/#/user/login';
  
  const currentUser = getCurrentUser();

  if(!currentUser || current_url === '/#' || current_url === '/#/' || current_url ==='#/user/login'){
    removeAll();  
    window.location.href = loginurl;
    oldRender();
    
  }else{
    
  
    let productname = 'nipt-plus';
    //如果管理员，则进入管理员菜单
    if(currentUser.roleid == '1123598816738675201' || currentUser.roleid == '1123598816738675205'){
      productname = 'system-message';
    }
    dynamicRoutesByProduct(productname).then(response => {
        if (response && response.success) {
          authRoutes = response.data;
        }
        oldRender();
      });
  }
 
}
