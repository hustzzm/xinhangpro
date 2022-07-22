import { stringify } from 'qs';
import request from '../../utils/request';

// =====================参数===========================

export async function list(params) {
  return request(`/api/bizCompany/list?${stringify(params)}`);
}


export async function remove(params) {
  return request(`/api/bizCompany/remove/${params}`, {
    method: 'DELETE',   
  });
}

export async function submit(params) {
  return request('/api/bizCompany/save', {
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
  return request(`/api/bizCompany/update`, {
    method: 'POST',
    body: params
  });
}
