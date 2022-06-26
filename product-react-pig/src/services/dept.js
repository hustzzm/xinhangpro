import { stringify } from 'qs';
import func from '../utils/Func';
import request from '../utils/request';

// =====================部门===========================

export async function list(params) {
  return request(`/api/dept/list?${stringify(params)}`);
}

export async function tree(params) {
  return request(`/api/dept/tree?${stringify(params)}`);
}

export async function remove(params) {
  return request('/api/dept/remove', {
    method: 'POST',
    body: func.toFormData(params),
  });
}

export async function submit(params) {
  return request('/api/dept/submit', {
    method: 'POST',
    body: params,
  });
}

export async function detail(params) {
  return request(`/api/dept/detail?${stringify(params)}`);
}
