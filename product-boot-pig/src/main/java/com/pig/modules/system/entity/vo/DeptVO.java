package com.pig.modules.system.entity.vo;

import com.pig.modules.system.entity.Dept;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DeptVO extends Dept implements Serializable {

    private List<DeptVO> children;
}
