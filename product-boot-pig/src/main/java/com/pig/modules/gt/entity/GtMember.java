package com.pig.modules.gt.entity;

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
 * 用户表
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rd_gt_member")
public class GtMember implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键
     */
      @TableId(value = "id", type = IdType.AUTO)
      private String id;

      /**
     * 用户编号
     */
      private String code;

      /**
     * 账号
     */
      private String account;

      /**
     * token
     */
      private String token;

      /**
     * 昵称
     */
      private String name;

  /**
   * 1 普通会员，2钻石会员
   */
  private String accountLevel;

      /**
     * 真名
     */
      private String realName;

      /**
     * 头像
     */
      private String avatar;

      /**
     * 邮箱
     */
      private String email;

      /**
     * 手机
     */
      private String phone;

      /**
     * 生日
     */
      private Date birthday;

      /**
     * 性别
     */
      private Integer sex;

      /**
     * 角色id
     */
      private String roleId;

      /**
     * 部门id
     */
      private String deptId;

      /**
     * 创建人
     */
      private String createUser;

      /**
     * 创建时间
     */
      private Date createTime;

      /**
     * 修改人
     */
      private String updateUser;

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
