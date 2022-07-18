package com.pig.modules.gt.api;

import com.pig.basic.util.CommonResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/wx/smoke")
public class SmokeApi {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult test() {
        String msg = "测试回调！";
        log.info(msg);

        return CommonResult.ok(msg);
    }

    @RequestMapping(value = "/test/{openId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult test2(@PathVariable String openId) {
        String msg = "测试回调！" + openId;
        log.info(msg);

        return CommonResult.ok(msg);
    }
}
