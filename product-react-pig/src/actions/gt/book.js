export const BOOKINFO_NAMESPACE = 'book';

export function BOOKINFO_LIST(payload) {
  return {
    type: `${BOOKINFO_NAMESPACE}/fetchList`,
    payload,
  };
}

export function BOOKINFO_SUBMIT(payload) {
  return {
    type: `${BOOKINFO_NAMESPACE}/submit`,
    payload,
  };
}

export function BOOKINFO_UPDATE(payload) {
  return {
    type: `${BOOKINFO_NAMESPACE}/update`,
    payload,
  };
}


export function BOOKINFO_REMOVE(payload) {
  return {
    type: `${BOOKINFO_NAMESPACE}/remove`,
    payload,
  };
}

