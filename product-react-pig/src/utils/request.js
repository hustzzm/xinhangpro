import fetch from 'dva/fetch';
import { notification } from 'antd';
import router from 'umi/router';
import hash from 'hash.js';
import { Base64 } from 'js-base64';
import { clientId, clientSecret } from '../defaultSettings';
import { getToken, removeAll } from './authority';
import RequestForm from '@/utils/RequestForm';

const codeMessage = {
  200: '服务器成功返回请求的数据。',
  201: '新建或修改数据成功。',
  202: '一个请求已经进入后台排队（异步任务）。',
  204: '删除数据成功。',
  400: '发出的请求有错误，服务器没有进行新建或修改数据的操作。',
  401: '用户没有权限（令牌、用户名、密码错误）。',
  403: '用户得到授权，但是访问是被禁止的。',
  404: '发出的请求针对的是不存在的记录，服务器没有进行操作。',
  406: '请求的格式不可得。',
  410: '请求的资源被永久删除，且不会再得到的。',
  422: '当创建一个对象时，发生一个验证错误。',
  500: '服务器发生错误，请检查服务器。',
  502: '网关错误。',
  503: '服务不可用，服务器暂时过载或维护。',
  504: '网关超时。',
  999: '请求超时，请进入登录页面重新登录系统。'
};

const checkStatus = response => {
  
  if (
    (response.status >= 200 && response.status < 300) ||
    // 针对于要显示后端返回自定义详细信息的status, 配置跳过
    (response.status === 400 || response.status === 500 )
  ) {
    return response;
  }else if(response.status === 999 ){
    if (window.location.hash.endsWith('/user/login')) return false;
    notification.error({
      message: codeMessage[response.status] 
    });
    removeAll();
    router.push('/user/login');
    
  }else{
    const errortext = codeMessage[response.status] || response.statusText;
    notification.error({
      message: `请求错误 ${response.status}: ${response.url}`,
      description: errortext,
    });
    const error = new Error(errortext);
    error.name = response.status;
    error.response = response;
    throw error;
  } 
};

notification.config({
    maxCount: 1
  });

const checkServerCode = response => {
  /**
   * 判断是否存在token不存在就登录
   */
  const token = getToken();
  if (!token || token === 'undefined') {
    removeAll();
    router.push('/user/login');
  }
  if (response.code >= 200 && response.code < 300) {
    return response;
  }
  if (response.code === 400) {
    notification.error({
      message: response.msg || codeMessage[response.code],
    });
  } else if (response.code === 401 || response.status === 999) {
    if (window.location.hash.endsWith('/user/login')) return false;
    notification.error({
      message: response.msg || codeMessage[response.status],
    });
    removeAll();
    router.push('/user/login');
  } else if (response.code === 404) {
    notification.error({
      message: response.msg || codeMessage[response.code],
    });
  } else if (response.code === 500) {
    notification.error({
      message: response.msg || codeMessage[response.code],
    });
  }
  return response;
};

const cachedSave = (response, hashcode) => {
  /**
   * Clone a response data and store it in sessionStorage
   * Does not support data other than json, Cache only json
   */
  const contentType = response.headers.get('Content-Type');
  if (contentType && contentType.match(/application\/json/i)) {
    // All data is saved as text
    response
      .clone()
      .text()
      .then(content => {
        sessionStorage.setItem(hashcode, content);
        sessionStorage.setItem(`${hashcode}:timestamp`, Date.now());
      });
  }
  return response;
};

/**
 * Requests a URL, returning a promise.
 *
 * @param  {string} url       The URL we want to request
 * @param  {object} [option] The options we want to pass to "fetch"
 * @return {object}           An object containing either "data" or "err"
 */
export default function request(url, option) {
  const options = {
    ...option,
  };
  /**
   * Produce fingerprints based on url and parameters
   * Maybe url has the same parameters
   */
  const fingerprint = url + (options.body ? JSON.stringify(options.body) : '');
  const hashcode = hash
    .sha256()
    .update(fingerprint)
    .digest('hex');

  const defaultOptions = {
    credentials: 'include',
  };
  const newOptions = { ...defaultOptions, ...options };

  newOptions.headers = {
    ...newOptions.headers,
    // 客户端认证
    Authorization: `Basic ${Base64.encode(`${clientId}:${clientSecret}`)}`,
  };

  const token = getToken();
  if (token) {
    newOptions.headers = {
      ...newOptions.headers,
      // token鉴权
      'Berry-Auth': token,
    };
  }

  if (
    newOptions.method === 'POST' ||
    newOptions.method === 'PUT' ||
    newOptions.method === 'DELETE'
  ) {
    if (!(newOptions.body instanceof FormData) && !(newOptions.body instanceof RequestForm)) {
      newOptions.headers = {
        Accept: 'application/json',
        'Content-Type': 'application/json;charset=utf-8',
        ...newOptions.headers,
      };
      newOptions.body = JSON.stringify(newOptions.body);
    } else if (newOptions.body instanceof RequestForm) {
      newOptions.headers = {
        Accept: 'application/json',
        'Content-Type': 'application/x-www-form-urlencoded',
        ...newOptions.headers,
      };
      newOptions.body = newOptions.body.parse();
    } else {
      // newOptions.body is FormData
      newOptions.headers = {
        Accept: 'application/json',
        ...newOptions.headers,
      };
    }
  }

  //update by zzm 20201110 
  //设置超时时间 60 ->120
  const expirys = options.expirys && 120;
  // options.expirys !== false, return the cache,
  if (options.expirys !== false) {
    const cached = sessionStorage.getItem(hashcode);
    const whenCached = sessionStorage.getItem(`${hashcode}:timestamp`);
    if (cached !== null && whenCached !== null) {
      const age = (Date.now() - whenCached) / 1000;
      if (age < expirys) {
        const response = new Response(new Blob([cached]));
        return response.json();
      }
      sessionStorage.removeItem(hashcode);
      sessionStorage.removeItem(`${hashcode}:timestamp`);
    }
  }
  return fetch(url, newOptions)
    .then(checkStatus)
    .then(response => cachedSave(response, hashcode))
    .then(response => {
      // DELETE and 204 do not return data by default
      // using .json will report an error.
      if (response.status === 204) {
        return response.text();
      }
      return response.json();
    })
    .then(checkServerCode)
    .catch(e => {
      const status = e.name;
      if (status === 401) {
        // @HACK
        /* eslint-disable no-underscore-dangle */
        window.g_app._store.dispatch({
          type: 'login/logout',
        });
        return;
      }
      // environment should not be used
      if (status === 403) {
        router.push('/exception/403');
        return;
      }
      if (status <= 504 && status >= 500) {
        router.push('/exception/500');
      }
    });
}
