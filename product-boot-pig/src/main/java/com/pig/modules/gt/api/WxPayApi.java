package com.pig.modules.gt.api;

import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.service.WxPayService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/wx/pay")
public class WxPayApi {

    @Resource
    private WxPayService wxPayService;

    /**
     * 统一下单，成功后会返回prepay_id
     *
     * @return
     */
    @RequestMapping(value = "/unifiedOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult unifiedOrder(@RequestParam Map<String, Object> params) throws Exception {
        log.info("unifiedOrder.params={}", params);
        return wxPayService.unifiedOrder(params);
    }

    /**
     * 支付成功后的一个回调地址
     *
     * @return
     */
    @RequestMapping(value = "/notify", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult notifyUrl() {
        log.info("start notify...");

        return CommonResult.ok("start notify...");
    }
}
