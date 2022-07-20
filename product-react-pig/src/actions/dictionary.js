export const DICTIONARY_NAMESPACE = 'dictionary';

export function ROOMTYPE_DICT(payload) {
  return {
    type: `${DICTIONARY_NAMESPACE}/roomtypeDict`,
    payload,
  };
}
