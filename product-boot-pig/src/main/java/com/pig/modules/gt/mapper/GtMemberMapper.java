package com.pig.modules.gt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig.modules.gt.entity.GtMember;
import com.pig.modules.system.entity.User;
import com.pig.modules.system.entity.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
public interface GtMemberMapper extends BaseMapper<GtMember> {

    /**
     * 根据ID更新role_id,使用role_id字段，用作是否绑定，1绑定，其他解除绑定
     * @param params
     * @return
     */
    void doroleupdateById(@Param("params") Map<String,Object> params);
    /**
     * 根据ID更新token
     * @param params
     * @return
     */
    void doupdateById(@Param("params") Map<String,Object> params);

    void doinsert(@Param("params") Map<String,Object> params);
}
