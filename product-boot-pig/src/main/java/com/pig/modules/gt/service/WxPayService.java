package com.pig.modules.gt.service;

import com.pig.basic.util.CommonResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface WxPayService {

    /**
     * 成为会员统一下单
     * @param params
     * @return
     * @throws Exception
     */
    CommonResult unifiedOrder(@RequestParam Map<String, Object> params) throws Exception;

    /**
     * 升级会员统一下单
     * @param params
     * @return
     * @throws Exception
     */
    CommonResult ugrunifiedOrder(@RequestParam Map<String, Object> params) throws Exception;


    CommonResult orderQuery(Map<String, Object> params) throws Exception;

    /**
     * 会员升级，更新member
     * @param params
     * @return
     * @throws Exception
     */
    CommonResult ugrorderQuery(Map<String, Object> params) throws Exception;
}
