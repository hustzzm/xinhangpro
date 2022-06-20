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
 * 字典表
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
@TableName("rd_dict")
public class Dict implements Serializable {

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
     * 字典码
     */
      private String code;

      /**
     * 字典值
     */
      private Integer dictKey;

      /**
     * 字典名称
     */
      private String dictValue;

      /**
     * 排序
     */
      private Integer sort;

      /**
     * 字典备注
     */
      private String remark;

      /**
     * 是否已删除
     */
      private Integer isDeleted;


}
