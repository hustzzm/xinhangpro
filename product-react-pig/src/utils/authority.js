import cookie from 'react-cookies'
// use localStorage to store the authority info, which might be sent from server in actual project.
export function getAuthority(str) {
  // return localStorage.getItem('antd-pro-authority') || ['admin', 'user'];
  str = 'administrator'
  const authorityString =
    typeof str === 'undefined' ? cookie.load('berry-authority') : str;
  // authorityString could be admin, "admin", ["admin"]
  let authority;
  try {
    authority = JSON.parse(authorityString);
  } catch (e) {
    authority = authorityString;
  }
  if (typeof authority === 'string') {
    return [authority];
  }
  return authority || ['guest'];
}

export function setAuthority(authority) {
  const proAuthority =
    typeof authority === 'string'
      ? authority.split(',')
      : typeof authority === 'undefined'
      ? null
      : authority;
  cookie.save('berry-authority', encodeURIComponent(JSON.stringify(proAuthority)))
  return sessionStorage.setItem('berry-authority', JSON.stringify(proAuthority));
}

export function getExcelError() {
  return sessionStorage.getItem('excelError');
}

export function setExcelError(excelError) {
  sessionStorage.setItem('excelError', excelError);
}

export function getToken() {
  if(sessionStorage.getItem('berry-token')){
    return sessionStorage.getItem('berry-token')
  }
  if(decodeURIComponent(cookie.load('berry-token'))){
    sessionStorage.setItem('berry-token', decodeURIComponent(cookie.load('berry-token')));
    return decodeURIComponent(cookie.load('berry-token'))
  }
  return null;
}

export function setToken(token) {
  cookie.save('berry-token', encodeURIComponent(token))
  sessionStorage.setItem('berry-token', token);
}

export function getAccessToken() {
  return sessionStorage.getItem('berry-access-token') || '';
}

export function setAccessToken(accessToken) {
  sessionStorage.setItem('berry-access-token', accessToken);
}

export function getProduct() {
  return sessionStorage.getItem('berry-product') || [];
}

export function setProduct(productList) {
  sessionStorage.removeItem('berry-product');
  sessionStorage.setItem('berry-product', productList);
}

export function getRoutes() {
  return JSON.parse(sessionStorage.getItem('berry-niptplus-routes')) || [];
}

export function setRoutes(routes) {
  sessionStorage.removeItem('berry-niptplus-routes');
  sessionStorage.setItem('berry-niptplus-routes', JSON.stringify(routes));
}

export function getButtons() {
  return JSON.parse(sessionStorage.getItem('berry-niptplus-buttons')) || [];
}

export function getButton(code) {
  const buttons = getButtons();
  const data = buttons.filter(d => {
    return d.code === code;
  });
  return data.length === 0 ? [] : data[0].buttons;
}

export function setButtons(buttons) {
  sessionStorage.removeItem('berry-niptplus-buttons');
  sessionStorage.setItem('berry-niptplus-buttons', JSON.stringify(buttons));
}

export function getCurrentUser() {
  let berryCurrentUser = sessionStorage.getItem('berry-current-user')
  if(!berryCurrentUser){
    return
  }else{
    return JSON.parse(sessionStorage.getItem('berry-current-user'))
  }

  if(decodeURIComponent(cookie.load('berry-current-user'))){
    sessionStorage.setItem('berry-current-user', decodeURIComponent(cookie.load('berry-current-user')));
    return JSON.parse(decodeURIComponent(cookie.load('berry-current-user')));
  }

}

export function setCurrentUser(account) {
  cookie.save('berry-current-user', encodeURIComponent(JSON.stringify(account)))
  sessionStorage.setItem('berry-current-user', JSON.stringify(account));
}

export function setCaptchaKey(key) {
  sessionStorage.removeItem('berry-captcha-key');
  sessionStorage.setItem('berry-captcha-key', key);
}

export function getCaptchaKey() {
  return sessionStorage.getItem('berry-captcha-key');
}

export function removeAll() {
  sessionStorage.removeItem('berry-authority');
  sessionStorage.removeItem('berry-token');
  sessionStorage.removeItem('berry-niptplus-routes');
  sessionStorage.removeItem('berry-niptplus-buttons');
  sessionStorage.removeItem('berry-current-user');
  sessionStorage.removeItem('berry-captcha-key');
  sessionStorage.removeItem('berry-product');
  sessionStorage.removeItem('berry-select-product');
}
