import { message } from 'antd';
import {routerRedux} from 'dva/router';
import router from 'umi/router';
import { GTMEMBERINFO_NAMESPACE } from '@/actions/gt/gtmember';
import { list,gthtml,fetchgthometest,submit, detail,remove,update,roleupdate } from '@/services/gt/gtmember';

export default {
  namespace: GTMEMBERINFO_NAMESPACE,
  state: {
    data: {
      list: [],
      pagination: {},
    },
    detail: {},
  },
  effects: {
    *fetchList({ payload }, { call, put }) {
      const response = yield call(list, payload);
      if (response.success) {
        yield put({
          type: 'saveList',
          payload: {
            list: response.data.records,
            pagination: {
              total: response.data.total,
              current: response.data.current,
              pageSize: response.data.size,
            },
          },
        });
      }
    },  

    *fetchGtHomeTest({ payload }, { call }) {
      const response = yield call(fetchgthometest, payload);  
      return response;      
    },

    *fetchHtml({ payload }, { call, put }) {
      const response = yield call(gthtml, payload);
      return response;
    },
   
    *fetchDetail({ payload }, { call, put }) {
      const response = yield call(detail, payload);
      if (response.success) {
        yield put({
          type: 'saveDetail',
          payload: {
            detail: response.data,
          },
        });
      }
      return response
    },
    *submit({ payload }, { call }) {
      return yield call(submit, payload);  
    },
    *roleupdate({ payload }, { call }) {      
      return yield call(roleupdate, payload);  
    },

    *update({ payload }, { call }) {      
      return yield call(update, payload);  
    },
    *remove({ payload }, { call }) {
      //return yield call(remove, { ids: payload.ids });
      console.log(payload);
      return yield call(remove, payload);
    },
   
  },
  reducers: {
    saveList(state, action) {
      return {
        ...state,
        data: action.payload,
      };
    },
   
    saveDetail(state, action) {
      return {
        ...state,
        detail: action.payload.detail,
      };
    },
    save(state, action) {
      return {
        ...state,
        ...action.payload,
      };
    },   
  },
};
