package com.pig.modules.gt.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pig.basic.config.ConfigurationConfig;
import com.pig.basic.util.AesCbcUtil;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.service.BizMemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
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

    @Resource
    private ConfigurationConfig configurationConfig;

    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/WxOpenData")
    public CommonResult getWxOpenData(@RequestParam(value = "js_code",required = true) String js_code){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        StringBuffer info = appendUrl(js_code);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String strbody = restTemplate.exchange(info.toString().replace("\"", ""), HttpMethod.GET, entity, String.class)
                .getBody();
        log.info("getWxOpenData test start---------------");
        log.info("getWxOpenData:" + strbody);
        log.info("getWxOpenData test  end---------------");
        JSONObject jsonObject = JSONObject.parseObject(strbody);
        jsonObject.put("wxval",jsonObject.get("session_key"));

        return CommonResult.ok(JSON.toJSONString(jsonObject));
    }

    private StringBuffer appendUrl(String code) {
        StringBuffer info = new StringBuffer("https://api.weixin.qq.com/sns/jscode2session?");
        info.append("appid=").append(configurationConfig.appId).append("&");
        info.append("secret=").append(configurationConfig.Appsecret).append("&");
        info.append("js_code=").append(code).append("&");
        info.append("grant_type=").append("authorization_code");
        return info;
    }

    /**
     * ????????????
     *
     * @param params
     * @param session
     * @return
     */
    @RequestMapping(value = "/getPhone", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult getPhone(@RequestBody Map<String,Object> params, HttpSession session) {
        log.info("????????????");

        CommonResult commonResult = CommonResult.failed();

        String encryptedData = StringUtil.getCheckString(params.get("encryptedData"));
        String iv = StringUtil.getCheckString(params.get("iv"));
        String sessionKey = StringUtil.getCheckString(params.get("wxval"));
        String openid = StringUtil.getCheckString(params.get("openid"));

        if(StringUtil.isNull(encryptedData)){
            return CommonResult.failed("??????????????????????????????????????????????????????");
        }

        log.info("encryptedData???" + encryptedData);
        log.info("iv???" + iv);

        //2??????encryptedData??????????????????AES??????
        try {
            String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                log.info("????????????");

                JSONObject userInfoJSON = JSON.parseObject(result);

                String gender = String.valueOf(StringUtil.getCheckDouble(params.get("gender")).intValue());
                Map<String, Object> memberMap = new HashMap<String, Object>();


                //??????openid??????????????????
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
                log.info("????????????");
                commonResult = bizMemberService.insertOrUpdate(memberMap);

            } else {
                log.info("????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("?????????" + e.getMessage());
            return CommonResult.failed("???????????????????????????????????????????????????????????????");
        }

        return commonResult;
    }
}
