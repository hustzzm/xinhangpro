package com.pig.modules.system.controller;


import com.pig.basic.config.PublicInterface;
import com.pig.basic.listener.PropertiesListenerConfig;
import com.pig.basic.shiro.UserVo;
import com.pig.basic.shiro.token.JWTUtil;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.modules.system.entity.Dept;
import com.pig.modules.system.entity.Product;
import com.pig.modules.system.entity.User;
import com.pig.modules.system.entity.vo.AuthInfo;
import com.pig.modules.system.service.DeptService;
import com.pig.modules.system.service.ParamService;
import com.pig.modules.system.service.ProductService;
import com.pig.modules.system.service.UserService;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping(value = "/auth")
public class LoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private DeptService deptService;

	@Autowired
	private ParamService paramService;
	/**
	 * 登录
	 *
	 * @param account
	 * @param password
	 * @return
	 */
	@PostMapping(value = "/login")
	@PublicInterface
	public CommonResult<AuthInfo> login(@RequestParam(required = false) String account,
										@RequestParam(required = false) String password, HttpServletRequest request) {
		User user = userService.getUserByUserName(account);
		if (user == null) {
			return CommonResult.failed("用户名不存在!");
		}
		if (user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
			// 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
			String currentTimeMillis = String.valueOf(System.currentTimeMillis());
			// 从Header中Authorization返回AccessToken，时间戳为当前时间戳
			String token = JWTUtil.sign(user.getAccount(), user.getAccount(),user.getRoleId(), currentTimeMillis);
			AuthInfo authInfo = new AuthInfo();
			authInfo.setAccount(user.getAccount());
			authInfo.setUserName(user.getRealName());
			authInfo.setRoleid(user.getRoleId());
			Dept byId = deptService.getById(user.getDeptId());
			if(byId!=null){
				authInfo.setDeptName(byId.getDeptName());
			}
//			authInfo.setImgpath(paramService.getPath("cnvimgPath"));
			authInfo.setAuthority("administrator");
			authInfo.setAccessToken(token);
			authInfo.setExpiresIn(Long.parseLong(PropertiesListenerConfig.getProperty("accessTokenExpireTime")) * 1000);
			authInfo.setRefreshToken("");
			authInfo.setTokenType("");
			/**
			 * 查询授权的列表(暂时没有修改菜单配置以及授权 先查询全部的产品列表)
			 */
			List<Product> list = productService.getUserProduct(CommonUtil.toLongList(user.getRoleId()));
			Gson gson = new Gson();
			String s = gson.toJson(list);
			authInfo.setProductList(s);

			Map<String,Object> logMap = new HashMap<>();
			logMap.put("操作内容","登录页米->用户登录");
			logMap.put("输入参数","account:" + account);
			logMap.put("操作账号",account);
			log.info(logMap);

			request.getSession().setAttribute("currentUser",user);
			return CommonResult.ok(authInfo);
		} else {
			return CommonResult.failed("用户名或密码错误");
		}
	}

	@PublicInterface
	@GetMapping(value = "/loginout")
	public CommonResult loginout(HttpServletRequest request){
		Subject subject = SecurityUtils.getSubject();
		UserVo userVo = CommonUtil.getUserVoFormToken();
		Map<String,Object> logMap = new HashMap<>();
		logMap.put("操作内容","登录页米->用户注销");
		logMap.put("输入参数","");
		logMap.put("操作账号",userVo.getUserCode());

		subject.logout();

		request.getSession().removeAttribute("currentUser");
		return CommonResult.ok();
	}
}
