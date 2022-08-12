package com.pig.modules.gt.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单信息表(BizOrder)实体类
 *
 * @author makejava
 * @since 2022-07-09 22:19:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BizOrderExportVO implements Serializable {
    private static final long serialVersionUID = -19081977913135102L;

    @ColumnWidth(20)
    @ExcelProperty(value = "订单号")
    private String orderNo;

    @ColumnWidth(15)
    @ExcelProperty(value = "姓名")
    private String orderAccount;

    @ColumnWidth(20)
    @ExcelProperty(value = "下单时间")
    private Date createTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "商品")
    private String orderName;

    @ColumnWidth(12)
    @ExcelProperty(value = "金额")
    private String orderPrice;

    @ColumnWidth(12)
    @ExcelProperty(value = "状态")
    private String orderStatus;

}

