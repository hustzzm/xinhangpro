import {fakeNiptChartData} from '@/services/api';
import {CHARTS_NAMESPACE} from "@/actions/charts";

export default {
  namespace: CHARTS_NAMESPACE,
  state: {
    visitData: [],
    loading: false,
  },

  effects: {
    *fetchData({ payload }, { call, put }) {
      const response = yield call(fakeNiptChartData, payload);
      yield put({
        type: 'save',
        payload: {
          visitData: response.data,
        },
      });
    },
  },

  reducers: {
    save(state,action) {
      return {
        ...state,
        ...action.payload,
      };
    },
    clear() {
      return {
        visitData: []
      };
    },
  },
};
