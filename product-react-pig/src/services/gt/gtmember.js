import { stringify } from 'qs';
import request from '../../utils/request';

// =====================参数===========================

export async function list(params) {

  return request(`/api/gtmember/list?${stringify(params)}`);
}


export async function fetchgthometest(params) {
  return request(`/api/gtapi/gthometest?${stringify(params)}`);
}

export async function gthtml(params) {
  return request('/api/gtlogin/login', {
    method: 'POST',
    body: params,
  });  
  // return request(`/api/gttest/gthtml?${stringify(params)}`);
}

export async function detail(params) {
  return request(`/api/gtmember/detail?${stringify(params)}`);
}

export async function remove(params) {
  return request('/api/gtmember/remove', {
    method: 'POST',
    body: params,
  });
}

export async function submit(params) {
  return request('/api/gtmember/submit', {
    method: 'POST',
    body: params,
  });
}

/**
 * 更新信息
 * @param params
 * @returns {Promise<any>}
 */
export async function update(params) {
  return request(`/api/gtmember/update`, {
    method: 'POST',
    body: params
  });
}
/**
 * 更新信息
 * @param params
 * @returns {Promise<any>}
 */
export async function roleupdate(params) {
  return request(`/api/gtmember/roleupdate`, {
    method: 'POST',
    body: params
  });
}