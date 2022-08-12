package com.pig.modules.gt.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pig.basic.config.ConfigurationConfig;
import com.pig.basic.util.AesUtil;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.dao.BizOrderDao;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.entity.BizOrder;
import com.pig.modules.gt.entity.vo.NotifyResutlVo;
import com.pig.modules.gt.service.BizMemberService;
import com.pig.modules.gt.service.WxPayService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/wx/pay")
public class WxPayApi {

    @Resource
    private WxPayService wxPayService;
    @Resource
    private BizMemberService bizMemberService;

    @Resource
    private BizOrderDao orderDao;

    @Resource
    private ConfigurationConfig configurationConfig;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 统一下单，成功后会返回prepay_id
     *
     * @return
     */
    @RequestMapping(value = "/unifiedOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult unifiedOrder(@RequestBody Map<String, Object> params) throws Exception {
        log.info("unifiedOrder.params={}", params);
        // 执行统一下单前，进行判断，是否可以下单
        // 1 如果已存在订单生效期内不可下单
        // 2 如果已下单，未付款，不可下单

        String dateNow = sdf.format(new Date());

        if(StringUtil.isNull(params.get("openid"))){
            return CommonResult.failed("会员信息失效，请重新进入系统！");
        }

        String openid = params.get("openid").toString();

        BizMember bizMember = bizMemberService.findExistRecord(openid,dateNow);
        if(bizMember != null && !StringUtil.isNull(bizMember.getOpenid())){
            return CommonResult.failed("您已完成会员的购买，不可重复购买会员！");
        }

        BizOrder bizOrder = orderDao.findByOpenIdAndNoPay(openid);
        if(bizOrder != null && !StringUtil.isNull(bizOrder.getOpenId())){
            return CommonResult.failed("您有待支付的订单，请在我的订单中完成支付！");
        }
        return wxPayService.unifiedOrder(params);
    }

    /**
     * 续订，执行统一下单
     *
     * @return
     */
    @RequestMapping(value = "/reunifiedOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult reunifiedOrder(@RequestBody Map<String, Object> params) throws Exception {
        log.info("reunifiedOrder.params={}", params);
        // 执行续订统一下单前，进行判断，是否可以下单
        // 1 存在订单生效期内的记录
        // 2 如果已下单，未付款，不可下单

        String dateNow = sdf.format(new Date());

        if(StringUtil.isNull(params.get("openid"))){
            return CommonResult.failed("会员信息失效，请重新进入系统！");
        }

        String openid = params.get("openid").toString();

        BizMember bizMember = bizMemberService.findExistRecord(openid,dateNow);
        if(bizMember == null || StringUtil.isNull(bizMember.getOpenid())){
            return CommonResult.failed("您的会员已失效，不可续订会员！");
        }

        BizOrder bizOrder = orderDao.findByOpenIdAndNoPay(openid);
        if(bizOrder != null && !StringUtil.isNull(bizOrder.getOpenId())){
            return CommonResult.failed("您有待支付的订单，请在我的订单中完成支付！");
        }

        return wxPayService.unifiedOrder(params);
    }

    /**
     * 升级，执行统一下单
     *
     * @return
     */
    @RequestMapping(value = "/supperunifiedOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult supperunifiedOrder(@RequestBody Map<String, Object> params) throws Exception {
        log.info("supperunifiedOrder.params={}", params);
        // 执行升级为钻石会员，统一下单前，进行判断，是否可以下单
        // 1 存在订单生效期内的记录
        // 2 如果已下单，未付款，不可下单
        // 3 如果是钻石会员，不可下单

        String dateNow = sdf.format(new Date());

        if(StringUtil.isNull(params.get("openid"))){
            return CommonResult.failed("会员信息失效，请重新进入系统！");
        }

        String openid = params.get("openid").toString();

        BizMember bizMember = bizMemberService.findExistRecord(openid,dateNow);
        if(bizMember == null || StringUtil.isNull(bizMember.getOpenid())){
            return CommonResult.failed("您的会员已失效，不可续订会员！");
        }else if("2".equals(bizMember.getUserLevel())){
            return CommonResult.failed("您已是钻石会员，不可升级！");
        }

        BizOrder bizOrder = orderDao.findByOpenIdAndNoPay(openid);
        if(bizOrder != null && !StringUtil.isNull(bizOrder.getOpenId())){
            return CommonResult.failed("您有待支付的订单，请在我的订单中完成支付！");
        }
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
