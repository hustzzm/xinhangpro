import { stringify } from 'qs';
import request from '../../utils/request';

// =====================参数===========================

export async function list(params) {
  return request(`/api/order/list?${stringify(params)}`);
}


export async function remove(params) {
  return request(`/api/order/remove/${params}`, {
    method: 'DELETE',   
  });
}


/**
 * 导出信息
 * @param params
 * @returns {Promise<any>}
 */
export async function doexport(params) {
  return request(`/api/order/export`, {
    method: 'POST',
    body: params
  });
  
}
