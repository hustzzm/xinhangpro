package com.pig.basic.aop;

import com.pig.basic.annotation.AuthModify;
import com.pig.basic.annotation.AuthUser;
import com.pig.basic.annotation.Authority;
import com.pig.basic.constant.AuthorityConstant;
import com.pig.basic.exception.BusinessException;
import com.pig.basic.shiro.UserVo;
import com.pig.basic.util.CommonUtil;
import com.pig.modules.system.entity.Role;
import com.pig.modules.system.mapper.RoleMapper;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author
 */
@Aspect
@Component
public class AuthorityAspect {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private ConcurrentHashMap<String, String> roleMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, String> modeAuthMap = new ConcurrentHashMap<String, String>(){
        {
            put("SAMPLE_MANAGER",      "analysisuser,devuser,master");
            put("WES_CASE",            "devuser,analysisuser");
            put("DATA_ANALYSE",        "analysisuser,devuser,master");
            put("WAITFOR_EXPLAIN",     "analysisuser,devuser");
            put("FIRST_GENERATION",    "analysisuser,devuser,master");
            put("WES_REPORT",          "analysisuser,devuser");
        }
    };

    @Pointcut("execution(* com.pig.modules.wes.controller..*Controller.*(..))")
    private void pointcut(){

    }
    /**
     * 仅拦截 @Authority 注解的方法
     * @param point
     */
    @Before(value="pointcut() && (@annotation(com.pig.basic.annotation.Authority) || @annotation(com.pig.basic.annotation.AuthModify))")
    public void authority(JoinPoint point){
        Method method = ((MethodSignature)point.getSignature()).getMethod();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            UserVo userVo = CommonUtil.getUserVoFormToken();
            if(userVo.getUserCode() == null){
                userVo.setUserCode("");
            }
            //给查询接口添加createUserId和roleAlias入参
            AuthUser annotation = parameters[i].getAnnotation(AuthUser.class);
            if(annotation != null){
                Authority authority = method.getAnnotation(Authority.class);
                String mode = authority.mode();
                Object arg = point.getArgs()[i];
                //默认controller是以map作为查询入参，往里面添加查询条件
                if(arg != null && arg instanceof Map){
                    Map<String, Object> map = (Map<String, Object>) arg;
                    //未登录用户禁止访问
                    if(StringUtils.isBlank(userVo.getUserCode())){
                        permissionDenied(map);
                        return;
                    }
                    //查询用户角色并缓存
                    queryRoleAlias(userVo);
                    //往查询参数里设置createUserId字段
                    map.put(AuthorityConstant.CREATE_USER_ID.getValue(),userVo.getUserCode());
                    if(StringUtils.isEmpty(mode)){
                        permissionDenied(map);
                        return;
                    }
                    //往查询参数里设置roleAlias
                    setRoleAlias(map, userVo);
                    //判断角色在该菜单是否只能看自己创造的数据
                    boolean auth = isForbidByMode(mode, userVo);
                    map.put(AuthorityConstant.AUTH.getValue(), auth);
                    return;
                }
            }
            //判断修改
            AuthModify authModify = parameters[i].getAnnotation(AuthModify.class);
            //获取注解上的表名
            Authority authority = method.getAnnotation(Authority.class);
            String tableName = authority.tableName();
            if(StringUtils.isNotBlank(tableName) && authModify != null){
                Object arg = point.getArgs()[i];
                //id以list方式传进来
                if(arg != null && arg instanceof List){
                    List list = (List) arg;
                    List<Map<String, Object>> maps =
                            jdbcTemplate.queryForList(String.format("select * from %s where id in (?)", tableName),StringUtils.join(list,","));
                    boolean forbid = false;
                    for (int j = 0; j < maps.size(); j++) {
                        Map<String, Object> map = maps.get(j);
                        if(!userVo.getUserCode().equals(map.get(AuthorityConstant.CREATE_USER_ID_COLUMN.getValue()))){
                            forbid = true;
                            break;
                        }
                    }
                    if(forbid){
                        throw new BusinessException(AuthorityConstant.FORBIDDEN.getValue());
                    }
                }
                try {
                    //调用实体类getId方法，没有该方法的一律禁止访问
                    Method getIdMethod = arg.getClass().getDeclaredMethod(AuthorityConstant.GETID.getValue());
                    Object id = getIdMethod.invoke(arg, new Object[0]);
                    List<Map<String, Object>> maps = jdbcTemplate.queryForList(String.format("select * from %s where id = ?", tableName), id);
                    if(maps.size() > 0){
                        if(!userVo.getUserCode().equals(maps.get(0).get(AuthorityConstant.CREATE_USER_ID_COLUMN.getValue()))){
                            throw new BusinessException(AuthorityConstant.FORBIDDEN.getValue());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BusinessException(AuthorityConstant.FORBIDDEN.getValue());
                }
            }
        }
    }

    /**
     * 查询角色别名
     * @param userVo
     */
    private void queryRoleAlias(UserVo userVo){
        if(!roleMap.containsKey(userVo.getRoleId()) && !StringUtils.isEmpty(userVo.getRoleId())){
            Role role = roleMapper.selectById(userVo.getRoleId());
            if(role != null){
                roleMap.put(role.getId(), role.getRoleAlias());
            }
        }
    }

    /**
     * 设置禁止访问
     */
    private void permissionDenied(Map<String, Object> map){
        map.put(AuthorityConstant.CREATE_USER_ID.getValue(), AuthorityConstant.NOT_AUTHORITY.getValue());
        map.put(AuthorityConstant.AUTH.getValue(), true);
    }

    /**
     * 判断角色权限
     * @return
     */
    private boolean isForbidByMode(String mode, UserVo userVo){
        boolean forbid = modeAuthMap.get(mode) == null ? true :
                modeAuthMap.get(mode).contains(roleMap.get(userVo.getRoleId())) ? false : true;
        return forbid;
    }

    /**
     * 给查询参数设置角色名
     */
    private void setRoleAlias(Map<String, Object> map, UserVo userVo){
        String roleAlias = roleMap.get(userVo.getRoleId());
        if(StringUtils.isNotBlank(roleAlias)){
            map.put("roleAlias", roleAlias);
        }
    }

}
