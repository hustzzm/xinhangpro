export const ORDERINFO_NAMESPACE = 'order';

export function ORDERINFO_LIST(payload) {
  return {
    type: `${ORDERINFO_NAMESPACE}/fetchList`,
    payload,
  };
}

export function ORDERINFO_EXPORT(payload) {
  return {
    type: `${ORDERINFO_NAMESPACE}/doexport`,
    payload,
  };
}



export function ORDERINFO_REMOVE(payload) {
  return {
    type: `${ORDERINFO_NAMESPACE}/remove`,
    payload,
  };
}

