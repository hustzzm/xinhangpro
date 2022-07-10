package com.pig.modules.gt.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.dao.BizOrderDao;
import com.pig.modules.gt.entity.BizOrder;
import com.pig.modules.gt.entity.BizOrderExportVO;
import com.pig.modules.gt.service.BizOrderService;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 房间管理(RoomManage)表控制层
 *
 * @author makejava
 * @since 2022-06-28 21:53:13
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private BizOrderDao orderDao;

    @Resource
    private BizOrderService orderService;

    @GetMapping(value = "/list")
    public CommonResult list(@RequestParam Map<String, Object> params) {
        Page<BizOrder> usersPage = orderService.page(params);
        if (!ObjectUtils.isEmpty(usersPage.getContent())) {
            List<BizOrder> content = usersPage.getContent();
            content.stream().forEach(x -> {
                x.setCreateTime(x.getCreateTime().substring(0, 19));
            });
        }
        return CommonResult.ok(usersPage);
    }

    @GetMapping(value = "/totalAmount")
    public CommonResult totalAmount(@RequestParam Map<String, Object> params) {
        double totalAmount  = orderService.getTotalAmount(params);

        return CommonResult.ok(totalAmount);
    }

    /**
     * 删除
     */
    @DeleteMapping("/remove")
    public CommonResult remove(@RequestBody List<Integer> orderIds) {

        orderDao.deleteByOrderIds(orderIds);

        return CommonResult.ok("删除成功");
    }

    @GetMapping("/export")
    public void export4(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String filenames = "订单列表";
        response.addHeader("Content-Disposition", "filename=" +
                java.net.URLEncoder.encode(filenames, "UTF-8") + ".xlsx");
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), BizOrderExportVO.class).build();
        WriteSheet[] writeSheet = new WriteSheet[]{
                EasyExcel.writerSheet(0, "sheet").build()
        };

        orderService.exportData(s -> {
            BizOrderExportVO resultObject = s;
            ArrayList arrayList = new ArrayList<BizOrderExportVO>();
            arrayList.add(resultObject);
            excelWriter.write(arrayList, writeSheet[0]);
        });

        excelWriter.finish();

    }
}

