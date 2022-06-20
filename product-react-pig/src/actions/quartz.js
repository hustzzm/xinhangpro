export const QUARTZ_NAMESPACE = 'quartz';

export function QUARTZ_LIST(payload) {
  return {
    type: `${QUARTZ_NAMESPACE}/fetchList`,
    payload,
  };
}

export function QUARTZ_RESUME(payload) {
  return {
    type: `${QUARTZ_NAMESPACE}/resumeJob`,
    payload
  };
}

export function QUARTZ_PAUSE(payload) {
  return {
    type: `${QUARTZ_NAMESPACE}/pauseJob`,
    payload
  };
}

export function QUARTZ_SUBMIT(payload) {
  return {
    type: `${QUARTZ_NAMESPACE}/submit`,
    payload,
  };
}

export function QUARTZ_REMOVE(payload) {
  return {
    type: `${QUARTZ_NAMESPACE}/remove`,
    payload,
  };
}
