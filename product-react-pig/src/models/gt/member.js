import { message } from 'antd';
import {routerRedux} from 'dva/router';
import router from 'umi/router';
import { MEMBERINFO_NAMESPACE } from '@/actions/gt/member';
import { list,remove,update } from '@/services/gt/member';

export default {
  namespace: MEMBERINFO_NAMESPACE,
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
            list: response.data.content,
            pagination: {
              total: response.data.total,
              current: response.data.pageable.page + 1 ,
              pageSize: response.data.pageable.size,
            },
          },
        });
      }
    },  

    *update({ payload }, { call }) {      
      return yield call(update, payload);  
    },
    *remove({ payload }, { call }) {
      
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
