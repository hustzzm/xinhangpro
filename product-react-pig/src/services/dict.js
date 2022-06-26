import { stringify } from 'qs';
import func from '../utils/Func';
import request from '../utils/request';

// =====================字典===========================

export async function dict(params) {
  return request(`/api/dict/dictionary?${stringify(params)}`);
}

export async function list(params) {
  return request(`/api/dict/list?${stringify(params)}`);
}

export async function tree(params) {
  return request(`/api/dict/tree?${stringify(params)}`);
}

export async function remove(params) {
  return request('/api/dict/remove', {
    method: 'POST',
    body: func.toFormData(params),
  });
}

export async function submit(params) {
  return request('/api/dict/submit', {
    method: 'POST',
    body: params,
  });
}

export async function detail(params) {
  return request(`/api/dict/detail?${stringify(params)}`);
}
