package com.pig.modules.gt.service.impl;

import com.pig.basic.config.ConfigurationConfig;
import com.pig.basic.util.CheckCommon;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.constant.HomeEnum;
import com.pig.modules.gt.dao.BizOrderDao;
import com.pig.modules.gt.entity.BizOrder;
import com.pig.modules.gt.service.WxPayService;
import com.pig.modules.wxpay.MyConfig;
import com.pig.modules.wxpay.WXPay;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class WxPayServiceImpl implements WxPayService {

    @Resource
    private ConfigurationConfig configurationConfig;

    @Resource
    private BizOrderDao orderDao;

    @Override
    public CommonResult unifiedOrder(Map<String, Object> params) throws Exception {
        // 参数检查
        String msg = CheckCommon.checkParams(params);
        if (!StringUtils.isEmpty(msg)) {
            return CommonResult.failed(msg);
        }
        // 封装请求参数
        Map<String, String> data = getData(params);
        // 发送请求
        try {
            // 初始化数据
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);
            Map<String, String> resp = wxpay.unifiedOrder(data);
            if (resp.get("return_code").equalsIgnoreCase("FAIL")) {
                return CommonResult.failed(resp.get("return_msg"));
            }
            if (resp.get("result_code").equalsIgnoreCase("FAIL")) {
                return CommonResult.failed(resp.get("err_code"));
            }
            // 下单成功，添加订单到order表
            saveToDb(params);
            return CommonResult.ok(resp);
        } catch (Exception e) {
            log.error("unifiedOrder is exception,", e.getMessage(), e);
            return CommonResult.failed("统一下单出现异常，" + e.getMessage());
        }
    }

    private void saveToDb(Map<String, Object> params) {
        BizOrder bizOrder = new BizOrder();
        bizOrder.setOpenId(StringUtil.getCheckString(params.get("openid")));
        bizOrder.setOrderNo(StringUtil.getCheckString(params.get("out_trade_no")));
        bizOrder.setOrderType(StringUtil.getCheckString(params.get("orderType")));
        bizOrder.setOrderPrice(StringUtil.getCheckDouble(params.get("orderPrice")));
        bizOrder.setUserLevel(StringUtil.getCheckString(params.get("userLevel")));
        orderDao.save(bizOrder);
    }

    private Map<String, String> getData(Map<String, Object> params) throws UnknownHostException {
        Map<String, String> data = new HashMap<>();
        data.put("body", StringUtil.getCheckString(params.get("body")));
        // 商户订单号
        String outTradeNo = CheckCommon.getOrderCode();
        params.put("out_trade_no", outTradeNo); //后面保存db使用
        data.put("out_trade_no", outTradeNo);
        data.put("openid", StringUtil.getCheckString(params.get("openid")));
        // 交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。对账单中的交易金额单位为【元】。
        // 外币交易的支付金额精确到币种的最小单位，参数值不能带小数点。
        data.put("total_fee", StringUtil.getCheckString(params.get("orderPrice"))); // 标价金额
        // 设备号
        data.put("device_info", HomeEnum.CommonEnum.DEVICE_INFO.getKey());
        // 标价币种
        data.put("fee_type", HomeEnum.CommonEnum.FEE_TYPE.getKey());
        // 支持IPV4和IPV6两种格式的IP地址。用户的客户端IP
        data.put("spbill_create_ip", InetAddress.getLocalHost().getHostAddress());
        data.put("notify_url", configurationConfig.notifyUrl);
        // 交易类型
        data.put("trade_type", HomeEnum.CommonEnum.TRADE_TYPE.getValue());
        return data;
    }

}
