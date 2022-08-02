import { stringify } from 'qs';
import request from '../../utils/request';

// =====================参数===========================

export async function list(params) {
  return request(`/api/order/list?${stringify(params)}`);
}

/**
 * 查询新的一条未语音播报的新订单
 * @return
 */
export async function queryNewRecord(params) {
  return request(`/api/order/queryNewRecord?${stringify(params)}`);
}

/**
 * 声音播报完成后，更新该记录的声音状态未已播报
 * @param {*} params 
 * @returns 
 */
export async function updateSoundState(params) {
  return request(`/api/order/updateSoundState?${stringify(params)}`);
}


export async function remove(params) {
  return request(`/api/order/remove?${stringify(params)}`);
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
