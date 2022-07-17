package com.pig.modules.gt.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pig.basic.util.AesCbcUtil;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.service.BizMemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/wx/login")
public class LoginApi {

//    protected static final String AppId= PropertiesListenerConfig.getProperty("AppId");
//    protected static final String Secret= PropertiesListenerConfig.getProperty("Secret");
//    protected static final String GrantType= PropertiesListenerConfig.getProperty("GrantType");

    @Autowired
    BizMemberService bizMemberService;

    /**
     * 微信登录
     *
     * @param params
     * @param session
     * @return
     */
    @RequestMapping(value = "/getPhone", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult getPhone(@RequestBody Map<String,Object> params, HttpSession session) {
        log.info("微信登录");

        CommonResult commonResult = CommonResult.failed();

        String encryptedData = StringUtil.getCheckString(params.get("encryptedData"));
        String iv = StringUtil.getCheckString(params.get("iv"));
        String sessionKey = StringUtil.getCheckString(params.get("sessionKey"));
        String openid = StringUtil.getCheckString(params.get("openid"));

        if(StringUtil.isNull(encryptedData)){
            return CommonResult.failed("数据异常，请联系系统管理员进行处理！");
        }

        log.info("encryptedData：" + encryptedData);
        log.info("iv：" + iv);

        //2、对encryptedData加密数据进行AES解密
        try {
            String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                log.info("解密成功");

                JSONObject userInfoJSON = JSON.parseObject(result);

                String gender = String.valueOf(StringUtil.getCheckDouble(params.get("gender")).intValue());
                Map<String, Object> memberMap = new HashMap<String, Object>();


                //根据openid获取用户信息
                memberMap.put("gender",gender);
                memberMap.put("avatar",StringUtil.getCheckString(params.get("avatar")));
                memberMap.put("nickname",StringUtil.getCheckString(params.get("nickname")));
                memberMap.put("mobile",StringUtil.getCheckString(userInfoJSON.get("purePhoneNumber").toString()));
                memberMap.put("openid",openid);
                System.out.println("openid:" + openid);
                memberMap.put("token","test123");
                BizMember bizMember = bizMemberService.findByOpenidAndStatus(openid,"-1");
                if(bizMember != null && !StringUtil.isNull(bizMember.getOpenid())){

                    memberMap.put("id",bizMember.getId());
                }
                log.info("解密成功");
                commonResult = bizMemberService.insertOrUpdate(memberMap);

            } else {
                log.info("解密失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("错误：" + e.getMessage());
            return CommonResult.failed("手机号解密失败，请联系系统管理员进行处理！");
        }

        return commonResult;
    }
}
