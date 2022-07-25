import { message } from 'antd';
import {routerRedux} from 'dva/router';
import router from 'umi/router';
import { BOOKINFO_NAMESPACE } from '@/actions/gt/book';
import { list,submit,remove,update } from '@/services/gt/book';

export default {
  namespace: BOOKINFO_NAMESPACE,
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

    *submit({ payload }, { call }) {
      return yield call(submit, payload);  
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
