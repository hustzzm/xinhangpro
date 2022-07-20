import {DICTIONARY_NAMESPACE} from "../actions/dictionary";
import {dict} from "../services/dict";

export default {
  namespace: DICTIONARY_NAMESPACE,
  state: {
    roomtypeDicts:[]
  },
  effects: {

    *roomtypeDict({ payload }, { call, put }) {
      const response = yield call(dict, payload);
      if (response.success) {
        yield put({
          type: 'save',
          payload: {
            roomtypeDicts: response.data,
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
