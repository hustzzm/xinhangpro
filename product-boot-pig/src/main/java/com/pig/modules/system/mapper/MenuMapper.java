package com.pig.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig.modules.system.entity.DTO.MenuDTO;
import com.pig.modules.system.entity.DTO.TreeNode;
import com.pig.modules.system.entity.Menu;
import com.pig.modules.system.entity.vo.MenuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 权限配置菜单
     *
     * @param roleId
     * @return
     */
    List<Menu> roleMenu(List<Long> roleId);

    /**
     * 权限配置菜单
     *
     * @param roleId
     * @return
     */
    List<Menu> roleMenuAndProduct(@Param("roleId")List<Long> roleId,@Param("productId") String productId);

    /**
     * 获取按钮
     * @param roleId
     * @return
     */
    List<Menu> getButtons(List<Long> roleId);


    /**
     * 获取按钮根据产品
     * @param roleId
     * @return
     */
    List<Menu> getButtonsByProduct(@Param("roleId")List<Long> roleId,@Param("productId")String productId);

    /**
     * 获取授权的路径
     * @param roleId
     * @return
     */
    List<MenuDTO> getAuthRoutes(List<Long> roleId);

    /**
     * 获取菜单树
     * @return
     */
    List<TreeNode> getTree();

    /**
     * 获取菜单树
     * @return
     */
    List<TreeNode> grantTree();

}
