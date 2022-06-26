package com.pig.modules.system.entity.vo;

import com.pig.modules.system.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserVO extends User implements Serializable {

    private List<UserVO> children;

    private String roleName;

    private String deptName;

}
