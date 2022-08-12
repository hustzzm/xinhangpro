package com.pig.modules.gt.api;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.core.BusinessUtil;
import com.pig.modules.gt.dao.BizOrderDao;
import com.pig.modules.gt.entity.BizBooking;
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
import java.text.SimpleDateFormat;
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
@RequestMapping("/wx/order")
public class OrderApi {

    @Resource
    private BizOrderDao orderDao;

    @Resource
    private BizOrderService orderService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 查看会员已预约的记录
     *
     * @return CommonResult
     */
    @GetMapping("/myorder")
    public CommonResult myorder(@RequestParam Map<String, Object> params) {
        String openid = StringUtil.getCheckString(params.get("openid"));
        List<BizOrder> bizOrderList = orderDao.findByOpenId(openid);
        for(BizOrder bizOrder : bizOrderList){
//            String content = bizOrder.getOrderStart() == null ? "" : sdf.format(bizOrder.getOrderStart());
            //暂时使用nickname代替订单生成日期
            bizOrder.setNickName(bizOrder.getOrderStart());
        }
        return CommonResult.ok(bizOrderList);
    }

    @GetMapping(value = "/list")
    public CommonResult list(@RequestParam Map<String, Object> params) {
        Page<BizOrder> usersPage = orderService.page(params);

        return CommonResult.ok(usersPage);
    }

    @GetMapping(value = "/totalAmount")
    public CommonResult totalAmount(@RequestParam Map<String, Object> params) {
        double totalAmount = orderService.getTotalAmount(params);

        return CommonResult.ok(totalAmount);
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

    /**、
     * 购买会员产生订单
     *
     * @param params params
     * @return CommonResult
     */
    @PostMapping(value = "/insert")
    public CommonResult insert(@RequestBody Map<String, Object> params) {

        return orderService.insert(params);
    }
}

