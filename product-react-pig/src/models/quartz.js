import { message } from 'antd';
import router from 'umi/router';
import { QUARTZ_NAMESPACE } from '../actions/quartz';
import { list, submit, resume, remove,pause } from '../services/quartz';

export default {
  namespace: QUARTZ_NAMESPACE,
  state: {
    data: {
      list: [],
      pagination: {},
    }
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
    *submit({ payload }, { call }) {
      const response = yield call(submit, payload);
      if (response.success) {
        message.success('提交成功');
        router.push('/system/quartz');
      }
    },
    *resumeJob({ payload }, { call }) {
      const response = yield call(submit, payload);
      if (response.success) {
        message.success('提交成功');
        router.push('/system/quartz');
      }
    },
    *pauseJob({ payload }, { call }) {
      const response = yield call(submit, payload);
      if (response.success) {
        message.success('提交成功');
        router.push('/system/quartz');
      }
    },
    *remove({ payload }, { call }) {
      const {
        data: { keys },
        success,
      } = payload;
      const response = yield call(remove, { ids: keys });
      if (response.success) {
        success();
      }
    },
  },
  reducers: {
    saveList(state, action) {
      return {
        ...state,
        data: action.payload,
      };
    }
  },
};
