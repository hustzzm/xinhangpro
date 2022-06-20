/*
 * @Descripttion: 
 * @version: 
 * @Author: Eugene
 * @Date: 2022-02-24 15:15:56
 * @LastEditors: gn
 * @LastEditTime: 2022-02-25 10:35:06
 */
import request from '@/utils/request';
import { stringify } from 'qs';

export async function uploadTemplate(params) {
  return request('/api/rdCnwTemplate/template', {
    method: 'POST',
    body: params
  });
};

export async function getList(params) {

  return request(`/api/rdCnwTemplate/list?${stringify(params)}`);
  // return request('/api/rdCnwTemplate/list', {
  //   method: 'POST',
  //   body: params
  // });
};

export async function deleteTemplateApi(params) {
  return request('/api/rdCnwTemplate/delete', {
    method: 'POST',
    body: params
  });
};

export async function downloadTemplateApi(params) {
  return request('/api/rdCnwTemplate/download', {
    method: 'POST',
    body: params,
    responseType: "blob"
  });
};