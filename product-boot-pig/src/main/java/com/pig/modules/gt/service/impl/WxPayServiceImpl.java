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
import com.pig.modules.wxpay.WXPayConstants;
import com.pig.modules.wxpay.WXPayUtil;
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
        // 发送请求Z
        try {
            // 初始化数据
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);
            Map<String, String> resp = wxpay.unifiedOrder(data);
            log.info("unifiedOrder.resp={}", resp);
            if (resp.get("return_code").equalsIgnoreCase("FAIL")) {
                return CommonResult.failed(resp.get("return_msg"));
            }
            if (resp.get("result_code").equalsIgnoreCase("FAIL")) {
                return CommonResult.failed(resp.get("err_code"));
            }
            Map<String, String> resultMap = getResultMap(params, resp);
            // 下单成功，添加订单到order表
            saveToDb(params);
            return CommonResult.ok(resultMap);
        } catch (Exception e) {
            log.error("unifiedOrder is exception,", e.getMessage(), e);
            return CommonResult.failed("统一下单出现异常，" + e.getMessage());
        }
    }

    private Map<String, String> getResultMap(Map<String, Object> params, Map<String, String> resp) throws Exception {
        Map<String, String> resultMap = new HashMap();
        Long timeStamp = System.currentTimeMillis() / 1000; // 获取时间戳
        String prepayid = resp.get("prepay_id");
        resultMap.put("appId", configurationConfig.appId); // appId,再次签名中的appId的i要大写不能写成appid
        resultMap.put("timeStamp", timeStamp + ""); // 这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
        resultMap.put("nonceStr", StringUtil.getCheckString(params.get("nonceStr")));//随机字符串必须和统一下单接口使用的随机字符串相同
        resultMap.put("package", "prepay_id=" + prepayid);
        resultMap.put("signType", WXPayConstants.MD5);//MD5或者HMAC-SHA256
        String sign = WXPayUtil.generateSignature(resultMap, configurationConfig.v2Key);//再次签名，这个签名用于小程序端调用wx
        resultMap.put("paySign", sign);

        return resultMap;
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
        String nonceStr = WXPayUtil.generateNonceStr(); // 随机字符串，与支付保持一致
        // 商户订单号
        String outTradeNo = CheckCommon.getOrderCode();
        params.put("out_trade_no", outTradeNo); // 后面保存db使用
        params.put("nonce_str", nonceStr); // 后面保存db使用
        data.put("nonce_str", nonceStr);
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
