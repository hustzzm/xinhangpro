export const CHARTS_NAMESPACE = 'chart';

export function CHART_DATA(payload) {
  return {
    type: `${CHARTS_NAMESPACE}/fetchData`,
    payload,
  };
}

