package com.pig.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
@TableName("rd_role")
public class Role implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键
     */
        @TableId(value = "id", type = IdType.AUTO)
      private String id;

      /**
     * 父主键
     */
      private String parentId;

      /**
     * 角色名
     */
      private String roleName;

      /**
     * 排序
     */
      private Integer sort;

      /**
     * 角色别名
     */
      private String roleAlias;

      /**
     * 是否已删除
     */
      private Integer isDeleted;


}
