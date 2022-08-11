package com.pig.modules.gt.service.impl;

import com.pig.basic.config.ConfigurationConfig;
import com.pig.basic.util.CheckCommon;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.constant.HomeEnum;
import com.pig.modules.gt.dao.BizMemberDao;
import com.pig.modules.gt.dao.BizOrderDao;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.entity.BizOrder;
import com.pig.modules.gt.entity.OrderEnum;
import com.pig.modules.gt.service.WxPayService;
import com.pig.modules.wxpay.MyConfig;
import com.pig.modules.wxpay.WXPay;
import com.pig.modules.wxpay.WXPayConstants;
import com.pig.modules.wxpay.WXPayUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class WxPayServiceImpl implements WxPayService {

    @Resource
    private ConfigurationConfig configurationConfig;

    @Resource
    private BizOrderDao orderDao;

    @Resource
    private BizMemberDao memberDao;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private DecimalFormat df = new DecimalFormat("######0.00");

    @Override
    public CommonResult unifiedOrder(Map<String, Object> params) throws Exception {
        // 参数检查
        String msg = CheckCommon.checkParams(params);
        if (!StringUtils.isEmpty(msg)) {
            return CommonResult.failed(msg);
        }
        // 封装请求参数
        Map<String, String> data = getData(params);
        log.info("unifiedOrder.data={}", data);
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

    @Override
    public CommonResult orderQuery(Map<String, Object> params) throws Exception {
        // 参数检查
        String msg = CheckCommon.checkParams(params);
        if (!StringUtils.isEmpty(msg)) {
            return CommonResult.failed(msg);
        }
        // 封装请求参数
        Map<String, String> data = getOrderQueryData(params);
        log.info("orderQuery.data={}", data);
        // 发送请求
        try {
            // 初始化数据
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);
            Map<String, String> resp = wxpay.orderQuery(data);
            log.info("orderQuery.resp={}", resp);
            if (resp.get("return_code").equalsIgnoreCase("FAIL")) {
                return CommonResult.failed(resp.get("return_msg"));
            }
            if (resp.get("result_code").equalsIgnoreCase("FAIL")) {
                return CommonResult.failed(resp.get("err_code_des"));
            }
            if (resp.get("trade_state").equals("SUCCESS")) {

                log.info("orderQuery step 111 ...");
                String openid = resp.get("openid");
                BizMember member = memberDao.findByOpenidAndStatus(openid, "-1");
                if (ObjectUtils.isEmpty(member)) {
                    return CommonResult.failed("会员不存在或失效！");
                }
                String outTradeNo = resp.get("out_trade_no");
                log.info("orderQuery step 112 ...outTradeNo:" + outTradeNo);
                BizOrder order = orderDao.findByOrderNo(outTradeNo);
                if (ObjectUtils.isEmpty(order)) {
                    return CommonResult.failed("订单不存在！");
                }
                String timeEnd = resp.get("time_end"); // 支付完成时间
                // 当订单使用了免充值型优惠券后返回该参数，应结订单金额=订单金额-免充值优惠券金额。
                String cashFee = resp.get("cash_fee");

                // 保存order表
                order.setPayPrice(Double.valueOf(cashFee));
                order.setPayTime(getPayTime(timeEnd));
                order.setOrderStatus(OrderEnum.PAYMENT_RECEIVED.getCode());
                orderDao.save(order);
                log.info("orderQuery step 113 ...orderDao.save:");
                // 保存member表
                member.setOrderNo(outTradeNo);
                String startDate = member.getStartDate();
                if (StringUtils.isEmpty(startDate)) {
                    startDate = sdf.format(new Date());
                    member.setStartDate(startDate);
                }
                String endDate = member.getEndDate();
                if (StringUtils.isEmpty(endDate)) {
                    endDate = sdf.format(new Date());
                }


                Date orderEnd = getOrderEnd(sdf.parse(endDate), order.getOrderType());

                log.info("orderQuery step 114 ...orderEnd:" + orderEnd);
                member.setEndDate(sdf.format(orderEnd));
                member.setUserLevel(order.getUserLevel());
                memberDao.save(member);

                log.info("orderQuery step 115 ...memberDao.save end");

                return CommonResult.ok(member);
            } else {
                return CommonResult.failed(resp.get("trade_state_desc"));
            }
        } catch (Exception e) {
            log.error("unifiedOrder is exception,", e.getMessage(), e);
            return CommonResult.failed("查询订单出现异常，" + e.getMessage());
        }
    }

    private String getPayTime(String timeEnd) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime ldt = LocalDateTime.parse(timeEnd, dtf);
        DateTimeFormatter fa = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ldt.format(fa);
    }


    private Map<String, String> getOrderQueryData(Map<String, Object> params) throws Exception {
        Map<String, String> data = new HashMap<>();
        String nonceStr = WXPayUtil.generateNonceStr(); // 随机字符串，与支付保持一致
        // 商户订单号
        // 订单编号、预定编号生成规则统一使用 newRandomSNO
        params.put("out_trade_no", StringUtil.getCheckString(params.get("out_trade_no"))); // 后面保存db使用
        params.put("nonce_str", nonceStr); // 后面保存db使用
        data.put("out_trade_no", StringUtil.getCheckString(params.get("out_trade_no")));
        data.put("nonce_str", nonceStr);
        return data;
    }

    private Map<String, String> getResultMap(Map<String, Object> params, Map<String, String> resp) throws Exception {
        Map<String, String> resultMap = new HashMap();
        Long timeStamp = System.currentTimeMillis() / 1000; // 获取时间戳
        String prepayid = resp.get("prepay_id");
        resultMap.put("appId", configurationConfig.appId); // appId,再次签名中的appId的i要大写不能写成appid
        resultMap.put("timeStamp", timeStamp + ""); // 这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
        resultMap.put("nonceStr", StringUtil.getCheckString(params.get("nonce_str"))); // 随机字符串必须和统一下单接口使用的随机字符串相同
        resultMap.put("package", "prepay_id=" + prepayid);
        resultMap.put("signType", WXPayConstants.MD5);//MD5或者HMAC-SHA256
        String sign = WXPayUtil.generateSignature(resultMap, configurationConfig.v2Key); // 再次签名，这个签名用于小程序端调用wx
        resultMap.put("paySign", sign);
        resultMap.put("out_trade_no", StringUtil.getCheckString(params.get("out_trade_no")));// 订单号
        return resultMap;
    }

    private void saveToDb(Map<String, Object> params) {
        BizOrder bizOrder = new BizOrder();
        String openid = StringUtil.getCheckString(params.get("openid"));
        bizOrder.setOpenId(openid);
        bizOrder.setOrderNo(StringUtil.getCheckString(params.get("out_trade_no")));
        String orderType = StringUtil.getCheckString(params.get("orderType")); // 订单类型
        bizOrder.setOrderType(orderType);
        Double orderPrice = StringUtil.getCheckDouble(params.get("orderPrice")); // 订单金额
        String orderPriceFormat = df.format(orderPrice / 100);
        bizOrder.setOrderPrice(Double.parseDouble(orderPriceFormat));

        bizOrder.setUserLevel(StringUtil.getCheckString(params.get("userLevel")));
        BizMember member = memberDao.findByOpenidAndStatus(openid, "-1");
        if (null != member) {
            bizOrder.setName(member.getName());
        }
        // 根据订单类型，算出订单结束日期
        bizOrder.setOrderStart(sdf.format(new Date()));
        bizOrder.setOrderEnd(sdf.format(getOrderEnd(new Date(), orderType)));
        orderDao.save(bizOrder);
    }

    private Date getOrderEnd(Date sourceDate, String orderType) {
        Date end = null;
        switch (orderType) {
            case "1":
                end = stepMonth(sourceDate, 1);
                break;
            case "2":
                end = stepMonth(sourceDate, 3);
                break;
            case "3":
                end = stepMonth(sourceDate, 6);
                break;
            case "4":
                end = stepMonth(sourceDate, 9);
                break;
            case "5":
            case "6":
                end = stepMonth(sourceDate, 12);
                break;
            default:
                end = sourceDate;
                break;
        }
        return end;
    }

    public static Date stepMonth(Date sourceDate, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(Calendar.MONTH, month);

        return c.getTime();
    }

    private Map<String, String> getData(Map<String, Object> params) throws UnknownHostException {
        Map<String, String> data = new HashMap<>();
        data.put("body", StringUtil.getCheckString(params.get("body")));
        String nonceStr = WXPayUtil.generateNonceStr(); // 随机字符串，与支付保持一致
        // 商户订单号
        // 订单编号、预定编号生成规则统一使用 newRandomSNO
        String outTradeNo = CommonUtil.newRandomSNO("O");
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
