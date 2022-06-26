import {stringify} from 'qs';
import request from '../utils/request';

// =====================参数===========================

export async function list(params) {
  return request(`/api/quartz/page?${stringify(params)}`);
}

export async function remove(params) {
  return request('/api/quartz/removeJob', {
    method: 'POST',
    body: params,
  });
}

export async function submit(params) {
  return request('/api/quartz/addJob', {
    method: 'POST',
    body: params,
  });
}

export async function resume(params) {
  return request('/api/quartz/resumeJob', {
    method: 'POST',
    body: params,
  });
}

export async function pause(params) {
  return request('/api/quartz/pauseJob', {
    method: 'POST',
    body: params,
  });
}

