package com.pig.modules.system.entity.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TreeNode implements Serializable {

    private String id;

    private String parentId;

    private String title;

    private String value;

    private String key;

    private List<TreeNode> children;
}
