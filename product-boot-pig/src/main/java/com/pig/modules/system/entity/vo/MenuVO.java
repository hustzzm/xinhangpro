package com.pig.modules.system.entity.vo;

import com.pig.modules.system.entity.Menu;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MenuVO extends Menu implements Serializable {

    private List<MenuVO> children;

    /**
     * 上级菜单
     */
    private String parentName;

    /**
     * 菜单类型
     */
    private String categoryName;

    /**
     * 按钮功能
     */
    private String actionName;

    /**
     * 是否新窗口打开
     */
    private String isOpenName;

}
