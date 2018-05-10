package cn.lfy.base.mapper;

import org.apache.ibatis.annotations.Param;

import cn.lfy.base.model.Menu;
import cn.lfy.common.mybatis.SuperMapper;

public interface MenuMapper extends SuperMapper<Menu> {

    /**
     * 更新节点的子节点的parentIdPath
     * @param oldParentIdPath
     * @param newParentIdPath
     */
    void updateChildParentPath(@Param("oldParentIdPath") String oldParentIdPath, @Param("newParentIdPath") String newParentIdPath);

}