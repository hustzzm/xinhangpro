package com.pig.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
@TableName("rd_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键
     */
      @TableId(value = "id", type = IdType.AUTO)
      private String id;

      /**
     * 父级菜单
     */

      private String parentId;

      /**
     * 菜单编号
     */
      private String code;

      /**
     * 菜单名称
     */
      private String name;

      /**
     * 菜单别名
     */
      private String alias;

      /**
     * 请求地址
     */
      private String path;

      /**
     * 菜单资源
     */
      private String source;

      /**
     * 排序
     */
      private Integer sort;

      /**
     * 菜单类型
     */
      private Integer category;

      /**
     * 操作按钮类型
     */
      private Integer action;

      /**
     * 是否打开新页面
     */
      private Integer isOpen;

      /**
     * 备注
     */
      private String remark;

      /**
     * 是否已删除
     */
      private Integer isDeleted;


}
