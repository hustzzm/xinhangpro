import { stringify } from 'qs';
import request from '../utils/request';
import func from '../utils/Func';

// =====================菜单===========================

export async function dynamicRoutes() {
  return request('/api/menu/routes');
}

export async function dynamicRoutesByProduct(productId) {
  return request(`/api/menu/routesByProduct?id=${productId}`);
}

export async function dynamicButtons() {
  return request('/api/menu/buttons');
}

export async function dynamicButtonsByProduct(productId) {
  return request(`/api/menu/buttonsByProduct?id=${productId}`);
}

export async function list(params) {
  return request(`/api/menu/list?${stringify(params)}`);
}

export async function tree(params) {
  return request(`/api/menu/tree?${stringify(params)}`);
}

export async function grantTree(params) {
  return request(`/api/menu/grant-tree?${stringify(params)}`);
}

export async function roleTreeKeys(params) {
  return request(`/api/menu/role-tree-keys?${stringify(params)}`);
}

export async function remove(params) {
  return request('/api/menu/remove', {
    method: 'POST',
    body: func.toFormData(params),
  });
}

export async function submit(params) {
  return request('/api/menu/submit', {
    method: 'POST',
    body: params,
  });
}

export async function detail(params) {
  return request(`/api/menu/detail?${stringify(params)}`);
}

export async function routesAuthority() {
  return request('/api/menu/authRoutes');
}
