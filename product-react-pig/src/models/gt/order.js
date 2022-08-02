import { message } from 'antd';
import {routerRedux} from 'dva/router';
import router from 'umi/router';
import { ORDERINFO_NAMESPACE } from '@/actions/gt/order';
import { list,doexport,remove,queryNewRecord,updateSoundState } from '@/services/gt/order';

export default {
  namespace: ORDERINFO_NAMESPACE,
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
            list: response.data.result.content,
            pagination: {
              total: response.data.result.total,
              current: response.data.result.pageable.page + 1 ,
              pageSize: response.data.result.pageable.size,
            },
            totalAmount:response.data.totalAmount
          },
        });
      }
    },  

    *queryNewRecord({ payload }, { call }) {      
      return yield call(queryNewRecord, payload);  
    },
   
    *doexport({ payload }, { call }) {      
      return yield call(doexport, payload);  
    },

    *updateSoundState({ payload }, { call }) {
      //return yield call(remove, { ids: payload.ids });     
      return yield call(updateSoundState, payload);
    },
   

    *remove({ payload }, { call }) {
      //return yield call(remove, { ids: payload.ids });     
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
