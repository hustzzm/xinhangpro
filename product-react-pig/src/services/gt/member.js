import { stringify } from 'qs';
import request from '../../utils/request';

// =====================参数===========================

export async function list(params) {
  return request(`/api/bizMember/list?${stringify(params)}`);
}


export async function remove(params) {
  return request(`/api/bizMember/remove/${params}`, {
    method: 'DELETE',   
  });
}

/**
 * 更新信息
 * @param params
 * @returns {Promise<any>}
 */
export async function update(params) {
  return request(`/api/bizMember/update`, {
    method: 'POST',
    body: params
  });
}
