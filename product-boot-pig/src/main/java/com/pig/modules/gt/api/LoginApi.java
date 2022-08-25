package com.pig.modules.gt.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pig.basic.config.ConfigurationConfig;
import com.pig.basic.util.AesCbcUtil;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.basic.util.utils.StringUtils;
import com.pig.modules.core.TokenUtils;
import com.pig.modules.gt.dao.BizMemberDao;
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

    @Resource
    private BizMemberDao memberDao;

    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/WxOpenData")
    public CommonResult getWxOpenData(@RequestParam(value = "js_code", required = true) String js_code) {

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
        jsonObject.put("wxval", jsonObject.get("session_key"));

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
     * 微信登录
     *
     * @param params
     * @param session
     * @return
     */
    @RequestMapping(value = "/getPhone", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult getPhone(@RequestBody Map<String, Object> params, HttpSession session) {
        log.info("微信登录");

        CommonResult commonResult = CommonResult.failed();

        String encryptedData = StringUtil.getCheckString(params.get("encryptedData"));
        String iv = StringUtil.getCheckString(params.get("iv"));
        String sessionKey = StringUtil.getCheckString(params.get("wxval"));
        String openid = StringUtil.getCheckString(params.get("openid"));

        if (StringUtil.isNull(encryptedData)) {
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

                String mobile = StringUtil.getCheckString(userInfoJSON.get("purePhoneNumber").toString());
                //根据openid获取用户信息
                memberMap.put("gender", gender);
                memberMap.put("avatar", StringUtil.getCheckString(params.get("avatar")));
                memberMap.put("nickname", StringUtil.getCheckString(params.get("nickname")));
                memberMap.put("mobile", mobile);
                memberMap.put("openid", openid);


                try {
                    BizMember member = memberDao.findByOpenidAndMobile(openid, mobile);
                    if (null == member) {
                        return CommonResult.failed("用户不存在！");
                    }
                    String token = TokenUtils.token(openid, mobile);
                    if (StringUtils.isEmpty(token)) {
                        return CommonResult.failed("token获取异常！");
                    }
                    memberMap.put("token", token);
//                    return CommonResult.ok(token);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("错误：" + e.getMessage());
                    return CommonResult.failed("token获取异常，请联系系统管理员进行处理！");
                }


                BizMember bizMember = bizMemberService.findByOpenidAndStatus(openid, "-1");
                if (bizMember != null && !StringUtil.isNull(bizMember.getOpenid())) {

                    memberMap.put("id", bizMember.getId());
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

    /**
     * 获取token
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult getToken(@RequestBody Map<String, Object> params) {
        log.info("获取token");
        String openid = StringUtil.getCheckString(params.get("openid"));
        String mobile = StringUtil.getCheckString(params.get("mobile"));

        if (StringUtils.isEmpty(openid)) {
            return CommonResult.failed("openid不能为空！");
        }
        if (StringUtils.isEmpty(mobile)) {
            return CommonResult.failed("mobile不能为空！");
        }
        try {
            BizMember member = memberDao.findByOpenidAndMobile(openid, mobile);
            if (null == member) {
                return CommonResult.failed("用户不存在！");
            }
            String token = TokenUtils.token(openid, mobile);
            if (StringUtils.isEmpty(token)) {
                return CommonResult.failed("token获取异常！");
            }
            return CommonResult.ok(token);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("错误：" + e.getMessage());
            return CommonResult.failed("token获取异常，请联系系统管理员进行处理！");
        }
    }
}
