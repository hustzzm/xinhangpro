export const MEMBERINFO_NAMESPACE = 'member';

export function MEMBERINFO_LIST(payload) {
  return {
    type: `${MEMBERINFO_NAMESPACE}/fetchList`,
    payload,
  };
}


export function MEMBERINFO_UPDATE(payload) {
  return {
    type: `${MEMBERINFO_NAMESPACE}/update`,
    payload,
  };
}


export function MEMBERINFO_REMOVE(payload) {
  return {
    type: `${MEMBERINFO_NAMESPACE}/remove`,
    payload,
  };
}

