package com.pig.modules.gt.api;

import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.dao.BizMessageDao;
import com.pig.modules.gt.entity.BizMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/message")
public class MessageApi {

    @Resource
    private BizMessageDao bizMessageDao;

    /**
     * 查看个人续费消息记录
     *
     * @return CommonResult
     */
    @GetMapping("/existmessage")
    public CommonResult existmessage(@RequestParam Map<String, Object> params) {

        String openid = params.get("openid").toString();

        List<BizMessage> list = bizMessageDao.findAllMessage(openid);
        return CommonResult.ok(list);

    }
}
