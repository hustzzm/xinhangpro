export const ROOMINFO_NAMESPACE = 'room';

export function ROOMINFO_LIST(payload) {
  return {
    type: `${ROOMINFO_NAMESPACE}/fetchList`,
    payload,
  };
}

export function ROOMINFO_SUBMIT(payload) {
  return {
    type: `${ROOMINFO_NAMESPACE}/submit`,
    payload,
  };
}

export function ROOMINFO_UPDATE(payload) {
  return {
    type: `${ROOMINFO_NAMESPACE}/update`,
    payload,
  };
}


export function ROOMINFO_REMOVE(payload) {
  return {
    type: `${ROOMINFO_NAMESPACE}/remove`,
    payload,
  };
}

