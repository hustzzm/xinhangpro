import {DICTIONARY_NAMESPACE} from "../actions/dictionary";
import {dict} from "../services/dict";

export default {
  namespace: DICTIONARY_NAMESPACE,
  state: {
    wessampleStatus:[],
    wessampleProgress:[],
    wessampleType:[],
    wessampleClass:[],
    dpchipCell:[],
    singleMultiples:[],
    chipStatus:[]
  },
  effects: {
    *wessampleStatusDict({ payload }, { call, put }) {
      const response = yield call(dict, payload);
      if (response.success) {
        yield put({
          type: 'save',
          payload: {
            wessampleStatus: response.data,
          },
        });
      }
    },
    *wessampleProgressDict({ payload }, { call, put }) {
      const response = yield call(dict, payload);
      if (response.success) {
        yield put({
          type: 'save',
          payload: {
            wessampleProgress: response.data,
          },
        });
      }
    },
    *wessampleTypeDict({ payload }, { call, put }) {
      const response = yield call(dict, payload);
      if (response.success) {
        yield put({
          type: 'save',
          payload: {
            // 过滤掉参考品和质控品
            wessampleType: response.data.filter(item=>item.dictValue!=='参考品'&&item.dictValue!=='质控品'),
          },
        });
      }
    },
    *wessampleTypeDictFull({ payload }, { call, put }) {
      const response = yield call(dict, payload);
      if (response.success) {
        yield put({
          type: 'save',
          payload: {
            // 过滤掉参考品和质控品
            wessampleType: response.data,
          },
        });
      }
    },
    *wessampleClassDict({ payload }, { call, put }) {
      const response = yield call(dict, payload);
      if (response.success) {
        yield put({
          type: 'save',
          payload: {
            wessampleClass: response.data,
          },
        });
      }
    },
    *dpchipCellDict({ payload }, { call, put }) {
      const response = yield call(dict, payload);
      if (response.success) {
        yield put({
          type: 'save',
          payload: {
            dpchipCell: response.data,
          },
        });
      }
    },
    *chipStatusDict({ payload }, { call, put }) {
      const response = yield call(dict, payload);
      if (response.success) {
        yield put({
          type: 'save',
          payload: {
            chipStatus: response.data,
          },
        });
      }
    },
    *selectSignDict({ payload }, { call, put }) {
      const response = yield call(dict, payload);
      if (response.success) {
        yield put({
          type: 'save',
          payload: {
            singleMultiples: response.data,
          },
        });
      }
    },
  },
  reducers: {
    save(state, action) {
      return {
        ...state,
        ...action.payload,
      };
    }
  },
};
