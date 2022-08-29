import { stringify } from 'qs';
import request from '../../utils/request';

// =====================参数===========================

export async function list(params) {
  return request(`/api/booking/list?${stringify(params)}`);
}

/**
 * 查询新的一条未语音播报的新订单
 * @return
 */
 export async function queryNewBooking(params) {
  return request(`/api/booking/queryNewBooking?${stringify(params)}`);
}

/**
 * 声音播报完成后，更新该记录的声音状态未已播报
 * @param {*} params 
 * @returns 
 */
export async function updateSoundState(params) {
  return request(`/api/booking/updateSoundState?${stringify(params)}`);
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
