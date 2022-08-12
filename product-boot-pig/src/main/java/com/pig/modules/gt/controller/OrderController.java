package com.pig.modules.gt.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.core.BusinessUtil;
import com.pig.modules.gt.dao.BizOrderDao;
import com.pig.modules.gt.entity.BizOrder;
import com.pig.modules.gt.entity.BizOrderExportVO;
import com.pig.modules.gt.service.BizOrderService;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
        Page<BizOrder> orderPage = orderService.page(params);
        if(orderPage.getContent() != null && orderPage.getContent().size() > 0){

            for(int i = 0; i < orderPage.getContent().size();i++){

                orderPage.getContent().get(i).setOrderPrice(orderPage.getContent().get(i).getOrderPrice());
            }
        }

        double totalAmount = orderService.getTotalAmount(params);

        Map<String,Object> result = new HashMap<>();
        result.put("result",orderPage);
        result.put("totalAmount",totalAmount);
        return CommonResult.ok(result);
    }

    @GetMapping(value = "/totalAmount")
    public CommonResult totalAmount(@RequestParam Map<String, Object> params) {
        double totalAmount = orderService.getTotalAmount(params);

        return CommonResult.ok(totalAmount);
    }

    /**
     * 查询新的一条未语音播报的新订单
     * @return
     */
    @GetMapping(value = "/queryNewRecord")
    public CommonResult queryNewRecord() {

        BizOrder bizOrder = orderDao.findByUnSoundState();
        if(bizOrder == null || StringUtil.isNull(bizOrder.getOrderNo())){
            CommonResult.failed();
        }
        return CommonResult.ok(bizOrder);
    }

    /**
     * 声音播报完成后，更新该记录的声音状态未已播报
     */
    @GetMapping("/updateSoundState")
    public CommonResult updateSoundState(@RequestParam String orderId) {

//        orderDao.deleteById(id);
        orderDao.updateByUnSoundState(Integer.parseInt(orderId));
        return CommonResult.ok("删除成功");
    }

    /**
     * 删除
     */
    @GetMapping("/remove")
    public CommonResult remove(@RequestParam String orderId) {

//        orderDao.deleteById(id);
        orderDao.updateByOrderStatus(Integer.parseInt(orderId));
        return CommonResult.ok("删除成功");
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
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

