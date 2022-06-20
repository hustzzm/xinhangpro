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
 * 角色菜单表
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
@TableName("rd_role_menu")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键
     */
        @TableId(value = "id", type = IdType.AUTO)
      private String id;

      /**
     * 菜单id
     */
      private String menuId;

      /**
     * 角色id
     */
      private String roleId;


}
