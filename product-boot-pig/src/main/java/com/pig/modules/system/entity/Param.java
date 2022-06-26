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
 * 参数表
 * </p>
 *
 * @author 
 * @since 2020-04-27
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
@TableName("rd_param")
public class Param implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键
     */
        @TableId(value = "id", type = IdType.AUTO)
      private String id;

      /**
     * 参数名
     */
      private String paramName;

      /**
     * 参数键
     */
      private String paramKey;

      /**
     * 参数值
     */
      private String paramValue;

      /**
     * 备注
     */
      private String remark;

      /**
     * 创建人
     */
      private Long createUser;

      /**
     * 创建时间
     */
      private Date createTime;

      /**
     * 修改人
     */
      private Long updateUser;

      /**
     * 修改时间
     */
      private Date updateTime;

      /**
     * 状态
     */
      private Integer status;

      /**
     * 是否已删除
     */
      private Integer isDeleted;


}
