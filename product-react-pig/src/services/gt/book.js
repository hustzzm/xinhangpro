import { stringify } from 'qs';
import request from '../../utils/request';

// =====================参数===========================

export async function list(params) {
  return request(`/api/booking/list?${stringify(params)}`);
}


export async function remove(params) {
  return request(`/api/booking/remove/${params}`, {
    method: 'POST',   
  });
}

export async function submit(params) {
  return request('/api/booking/save', {
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
  return request(`/api/booking/finished/${params}`, {
    method: 'POST'  
  });
}
