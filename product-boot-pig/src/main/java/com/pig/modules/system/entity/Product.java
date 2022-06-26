package com.pig.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2020-04-27
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
@TableName("rd_product")
public class Product implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键
     */
        @TableId(value = "id", type = IdType.AUTO)
      private String id;

      /**
     * 产品编码
     */
      private String productCode;

      /**
     * 产品名称
     */
      private String productName;

      /**
     * 产品图片路径(或者class样式)
     */
      private String productImg;

      /**
     * 点击后跳转的路径(新开一个页面)
     */
      private String productPath;

      /**
     * 创建时间
     */
      private Date createTime;

      /**
     * 修改时间
     */
      private Date updateTime;

    /**
     * 排序
     */
    private Integer sort;


}
