import { stringify } from 'qs';
import request from '../utils/request';
import func from '../utils/Func';


export async function loginout() {
  return request('/api/auth/loginout');
}
export async function fakeNiptChartData() {
  return request('/api/statistic/statisticData');
}

export async function queryFakeList(params) {
  return request(`/api/fake_list?${stringify(params)}`);
}

export async function removeFakeList(params) {
  const { count = 5, ...restParams } = params;
  return request(`/api/fake_list?count=${count}`, {
    method: 'POST',
    body: {
      ...restParams,
      method: 'delete',
    },
  });
}

export async function addFakeList(params) {
  const { count = 5, ...restParams } = params;
  return request(`/api/fake_list?count=${count}`, {
    method: 'POST',
    body: {
      ...restParams,
      method: 'post',
    },
  });
}

export async function updateFakeList(params) {
  const { count = 5, ...restParams } = params;
  return request(`/api/fake_list?count=${count}`, {
    method: 'POST',
    body: {
      ...restParams,
      method: 'update',
    },
  });
}

export async function fakeRegister(params) {
  return request('/api/register', {
    method: 'POST',
    body: params,
  });
}

export async function queryNotices() {
  return request('/api/blade-desk/notice/my-notices');
}

export async function getFakeCaptcha(mobile) {
  return request(`/api/captcha?mobile=${mobile}`);
}

export async function queryProvince() {
  return request('/api/geographic/province');
}

export async function queryCity(province) {
  return request(`/api/geographic/city/${province}`);
}

export default async function queryError(code) {
  return request(`/api/${code}`);
}

export async function requestApi(path, params) {
  return request(path, {
    method: 'POST',
    body: func.toFormData(params),
  });
}

export async function requestGetApi(path, params) {
  return request(`${path}?${stringify(params)}`);
}

// 待解读-病例详情
export async function getRestful(path) {
  return request(`${path}`, {method: 'GET'});
}


// formData传值
export async function postRestful(path, params) {
  return request(`${path}`, {
      method: 'POST',
      body: func.toFormData(params)
  });
}

// json字符串传值
export async function postJson(path, params) {
  return request(`${path}`, {
      method: 'POST',
      body: params
  });
}

export async function putRestful(path, params) {
  return request(`${path}`, {
    method: 'PUT',
    body: func.toFormData(params),
  });
}

export async function deleteRestful(path) {
  return request(`${path}`, {method: 'DELETE'});
}
