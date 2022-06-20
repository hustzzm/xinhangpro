export const DICTIONARY_NAMESPACE = 'dictionary';

export function WES_CASEINFO_STATUS_DICT(payload) {
  return {
    type: `${DICTIONARY_NAMESPACE}/wessampleStatusDict`,
    payload,
  };
}

export function WES_SAMPLEPROGRESS_DICT(payload) {
  return {
    type: `${DICTIONARY_NAMESPACE}/wessampleProgressDict`,
    payload,
  };
}

export function WES_SAMPLEPTYPETYPE_DICT(payload) {
  return {
    type: `${DICTIONARY_NAMESPACE}/wessampleTypeDict`,
    payload,
  };
}
export function WES_SAMPLEPTYPETYPE_DICT_FULL(payload) {
  return {
    type: `${DICTIONARY_NAMESPACE}/wessampleTypeDictFull`,
    payload,
  };
}
export function WES_SAMPLECLASS_DICT(payload) {
  return {
    type: `${DICTIONARY_NAMESPACE}/wessampleClassDict`,
    payload,
  };
}

export function CHIPCELL_DICT(payload) {
  return {
    type: `${DICTIONARY_NAMESPACE}/dpchipCellDict`,
    payload,
  };
}

export function CASEINFO_SIGN_DICT(payload) {
  return {
    type: `${DICTIONARY_NAMESPACE}/selectSignDict`,
    payload,
  };
}

export function CHIP_DICT(payload) {
  return {
    type: `${DICTIONARY_NAMESPACE}/chipStatusDict`,
    payload,
  };
}
