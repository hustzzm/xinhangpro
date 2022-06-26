package com.pig.modules.system.entity.vo;

import java.io.Serializable;
import java.util.List;

public interface INode extends Serializable {

    Long getId();

    Long getParentId();

    List<INode> getChildren();
}
