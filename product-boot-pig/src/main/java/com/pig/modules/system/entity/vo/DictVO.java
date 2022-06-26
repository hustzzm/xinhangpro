package com.pig.modules.system.entity.vo;

import com.pig.modules.system.entity.Dict;

import java.io.Serializable;
import java.util.List;

public class DictVO extends Dict implements Serializable {

    private List<DictVO> children;
}
