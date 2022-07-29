package com.pig.modules.gt.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.google.gson.annotations.JsonAdapter;
import com.pig.basic.adapter.DateTimeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 * 不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)实体类
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BizMemberVO implements Serializable {

    @ColumnWidth(20)
    @ExcelProperty(value = "会员姓名")
    private String name;

    @ColumnWidth(30)
    @ExcelProperty(value = "会员卡号")
    private String orderNo;

    @ColumnWidth(16)
    @ExcelProperty(value = "性别")
    private String gender;

    @ColumnWidth(16)
    @ExcelProperty(value = "年龄")
    private String age;

    @ColumnWidth(20)
    @ExcelProperty(value = "入会时间")
    private String startDate;

    @ColumnWidth(20)
    @ExcelProperty(value = "会员到期时间")
    private String endDate;

    @ColumnWidth(20)
    @ExcelProperty(value = "联系电话")
    private String mobile;

    @ColumnWidth(16)
    @ExcelProperty(value = "会员等级")
    private String userLevel;

    @ColumnWidth(40)
    @ExcelProperty(value = "备注")
    private String remark;


}

