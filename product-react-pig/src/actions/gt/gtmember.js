export const GTMEMBERINFO_NAMESPACE = 'gtmember';

export function GTMEMBERINFO_LIST(payload) {
  return {
    type: `${GTMEMBERINFO_NAMESPACE}/fetchList`,
    payload,
  };
}


export function GTHOMETEST_LIST(payload) {
  return {
    type: `${GTMEMBERINFO_NAMESPACE}/fetchGtHomeTest`,
    payload,
  };
}


export function GTHTMLINFO_DETAIL(payload) {
  return {
    type: `${GTMEMBERINFO_NAMESPACE}/fetchHtml`,
    payload,
  };
}

export function GTMEMBERINFO_DETAIL(id) {
  return {
    type: `${GTMEMBERINFO_NAMESPACE}/fetchDetail`,
    payload: { id },
  };
}

export function GTMEMBERINFO_SUBMIT(payload) {
  return {
    type: `${GTMEMBERINFO_NAMESPACE}/submit`,
    payload,
  };
}

export function GTMEMBERINFO_UPDATE(payload) {
  return {
    type: `${GTMEMBERINFO_NAMESPACE}/update`,
    payload,
  };
}


export function GTROLEINFO_UPDATE(payload) {
  return {
    type: `${GTMEMBERINFO_NAMESPACE}/roleupdate`,
    payload,
  };
}

export function GTMEMBERINFO_REMOVE(payload) {
  return {
    type: `${GTMEMBERINFO_NAMESPACE}/remove`,
    payload,
  };
}

