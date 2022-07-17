package com.pig.modules.gt.service;

import com.pig.basic.util.CommonResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface WxPayService {
    CommonResult unifiedOrder(@RequestParam Map<String, Object> params) throws Exception;
}
