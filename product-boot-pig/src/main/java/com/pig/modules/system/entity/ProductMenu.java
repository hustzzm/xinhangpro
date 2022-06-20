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
 * 
 * </p>
 *
 * @author 
 * @since 2020-04-29
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
@TableName("rd_product_menu")
public class ProductMenu implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键
     */
        @TableId(value = "id", type = IdType.AUTO)
      private String id;

      /**
     * 产品ID
     */
      private String productId;

      /**
     * 菜单ID
     */
      private String menuId;


}
