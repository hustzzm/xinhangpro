package com.pig.modules.system.entity.vo;

import com.pig.modules.system.entity.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleVO extends Role implements Serializable {

    private List<RoleVO> children;
}
