package cn.lfy.base.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

import cn.lfy.base.model.Menu;

public interface MenuService extends IService<Menu> {
    /**
     * 插入或更新记录
     */
    boolean save(Menu record);

    /**
     * 根据主键查询记录
     */
    Menu getByIdInCache(Long id);
    
    /**
     * 删除节点
     * @param id
     */
    void deleteById(Long id);
    /**
     * 根据parentId查询查子菜单
     * @param parentId
     * @return
     */
    List<Menu> listSubMenuByParentId(String parentId);
    /**
     * 更新图标
     * @param id
     * @param icon
     * @return
     */
    boolean updateIcon(Long id, String icon);
}