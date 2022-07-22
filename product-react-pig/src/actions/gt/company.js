export const COMPANYINFO_NAMESPACE = 'company';

export function COMPANYINFO_LIST(payload) {
  return {
    type: `${COMPANYINFO_NAMESPACE}/fetchList`,
    payload,
  };
}

export function COMPANYINFO_SUBMIT(payload) {
  return {
    type: `${COMPANYINFO_NAMESPACE}/submit`,
    payload,
  };
}

export function COMPANYINFO_UPDATE(payload) {
  return {
    type: `${COMPANYINFO_NAMESPACE}/update`,
    payload,
  };
}


export function COMPANYINFO_REMOVE(payload) {
  return {
    type: `${COMPANYINFO_NAMESPACE}/remove`,
    payload,
  };
}

