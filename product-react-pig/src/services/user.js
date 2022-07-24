import { stringify } from 'qs';
import request from '../utils/request';
import func from '../utils/Func';
import { getCaptchaKey } from '../utils/authority';
import { captchaMode } from '../defaultSettings';

// =====================用户===========================
export async function accountLogin(params) {
  const values = params;
  values.grantType = captchaMode ? 'captcha' : 'password';
  values.scope = 'all';
  return request('/api/auth/login', {
    headers: {
      'Captcha-key': getCaptchaKey(),
      'Captcha-code': values.code,
    },
    method: 'POST',
    body: func.toFormData(params),
  });
}

export async function query() {
  return request('/api/users');
}

export async function queryCurrent() {
  return request('/api/user/currentUser');
}

export async function list(params) {
  return request(`/api/user/list?${stringify(params)}`);
}

export async function grant(params) {
  return request('/api/user/grantRole', {
    method: 'POST',
    body: func.toFormData(params),
  });
}

export async function resetPassword(params) {
  return request('/api/user/reset-password', {
    method: 'POST',
    body: func.toFormData(params),
  });
}

export async function remove(params) {
  return request(`/api/user/remove?${stringify(params)}`, {
    method: 'POST',   
  });
}

export async function submit(params) {
  return request('/api/user/submit', {
    method: 'POST',
    body: params,
  });
}

export async function update(params) {
  return request('/api/user/update', {
    method: 'POST',
    body: params,
  });
}

export async function detail(params) {
  return request(`/api/user/detail?${stringify(params)}`);
}

export async function getUserInfo() {
  return request('/api/user/info');
}

export async function updatePassword(params) {
  return request('/api/user/update-password', {
    method: 'POST',
    body: func.toFormData(params),
  });
}

export async function getCaptchaImage() {
  return request('/api/user/captcha');
}
