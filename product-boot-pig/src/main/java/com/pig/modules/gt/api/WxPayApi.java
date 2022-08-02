package com.pig.modules.gt.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pig.basic.config.ConfigurationConfig;
import com.pig.basic.util.AesUtil;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.entity.vo.NotifyResutlVo;
import com.pig.modules.gt.service.WxPayService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/wx/pay")
public class WxPayApi {

    @Resource
    private WxPayService wxPayService;

    @Resource
    private ConfigurationConfig configurationConfig;

    /**
     * 统一下单，成功后会返回prepay_id
     *
     * @return
     */
    @RequestMapping(value = "/unifiedOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult unifiedOrder(@RequestBody Map<String, Object> params) throws Exception {
        log.info("unifiedOrder.params={}", params);
        return wxPayService.unifiedOrder(params);
    }

    /**
     * 统一下单，成功后会返回prepay_id
     *
     * @return
     */
    @RequestMapping(value = "/orderQuery", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult orderQuery(@RequestBody Map<String, Object> params) throws Exception {
        log.info("orderQuery.params={}", params);
        return wxPayService.orderQuery(params);
    }

    /**
     * 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url
     *
     * @return
     */
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.info("支付结果通知开始...");
        log.info("支付结果通知开始    111111");
        String requestString = StringUtil.getStringFromInputStream(request.getInputStream());
        log.info("微信结果通知加密字符串: " + requestString);
        NotifyResutlVo obj = JSON.parseObject(requestString, NotifyResutlVo.class);
        String associated_data = obj.getResource().getAssociated_data();
        String nonce = obj.getResource().getNonce();
        String ciphertext = obj.getResource().getCiphertext();
        AesUtil aesUtil = new AesUtil(configurationConfig.v2Key.getBytes());
        try {
            String str = aesUtil.decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext);
            log.info("微信结果通知解密字符串" + str);
            JSONObject jsonObject = JSONObject.parseObject(str);
            //订单号
            String out_trade_no = jsonObject.getString("out_trade_no");
            //微信支付订单号
            String transaction_id = jsonObject.getString("transaction_id");

            /**
             *    交易状态
             *
             *    1.SUCCESS：支付成功
             * 	  2.REFUND：转入退款
             * 	  3.NOTPAY：未支付
             * 	  4.CLOSED：已关闭
             * 	  5.REVOKED：已撤销（付款码支付）
             * 	  6.USERPAYING：用户支付中（付款码支付）
             * 	  7.PAYERROR：支付失败(其他原因，如银行返回失败)
             *
             *
             */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
