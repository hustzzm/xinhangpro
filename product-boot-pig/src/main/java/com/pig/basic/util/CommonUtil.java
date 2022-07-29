package com.pig.basic.util;

import com.pig.basic.constant.SystemConstant;
import com.pig.basic.shiro.UserVo;
import com.pig.basic.shiro.token.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    /**
     * 生成随机序列号
     * @param headWord 开始字符
     * @return
     */
    public static String newRandomSNO(String headWord){
        //C + 当前日期时间(2022-05-09 16:40:32)时间戳转36位 + 3位随机数
        //例如：CBieNMJO8h  C+BieNMJ+O8h
        StringBuilder sb = new StringBuilder(headWord);
        sb.append(sdf.format(new Date()));
        sb.append(Long.toString(System.currentTimeMillis()/1000,36))
                .append(StringUtil.getRandomCode(3));
        return sb.toString();
    }

    public static Long[] toLongArray(String split, String str) {
        if (StringUtils.isEmpty(str)) {
            return new Long[0];
        } else {
            String[] arr = str.split(split);
            Long[] longs = new Long[arr.length];

            for (int i = 0; i < arr.length; ++i) {
                Long v = Long.parseLong(arr[i]);
                longs[i] = v;
            }
            return longs;
        }
    }

    /**
     * 字符串转long型list
     *
     * @param str
     * @return
     */
    public static List<Long> toLongList(String str) {
        return Arrays.asList(toLongArray(",", str));
    }

    /**
     * 通过请求获取当前用户信息
     *
     * @return
     */
    public static UserVo getUserVoFormToken() {
        try {
            ServletRequest servletRequest = ((WebSubject) SecurityUtils.getSubject()).getServletRequest();
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String token = req.getHeader(SystemConstant.USER_HEADER);
            // 获取当前Token的帐号信息
            String account = JWTUtil.getClaim(token, SystemConstant.ACCOUNT);
            String userName = JWTUtil.getClaim(token, "userName");
            String roleId = JWTUtil.getClaim(token, "roleId");
            UserVo vo = new UserVo();
            vo.setUserCode(account);
            vo.setUserName(userName);
            vo.setRoleId(roleId);
            return vo;
        } catch (Exception e) {
            UserVo vo = new UserVo();
//            vo.setUserCode("admin");
//            vo.setUserName("admin");
//            vo.setRoleId("1");
            return vo;
        }
    }

    /**
     * 验证手机号
     *
     * @param phoneNum
     * @return
     */
    public static boolean checkMobilePhoneNum(String phoneNum) {
        String regex = "^(1[3-9]\\d{9}$)";
        if (phoneNum.length() == 11) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phoneNum);
            if (m.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象中属性值是否全为空
     *
     * @param object
     * @return
     */
    public static boolean checkObjAllFieldsIsNull(Object object) {
        if (null == object) {
            return true;
        }
        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                // 过滤序列化字段
                if (f.getName().equals("serialVersionUID")){
                    continue;
                }
                if (f.get(object) != null && org.apache.commons.lang.StringUtils.isNotBlank(f.get(object).toString())) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
