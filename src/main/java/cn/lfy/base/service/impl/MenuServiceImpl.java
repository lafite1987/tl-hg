package cn.lfy.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.lfy.base.mapper.MenuMapper;
import cn.lfy.base.mapper.RoleMenuMapper;
import cn.lfy.base.model.Menu;
import cn.lfy.base.model.RoleMenu;
import cn.lfy.base.service.MenuService;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

	@Autowired
	private MenuMapper menuMapper;
	
    @Autowired
    private RoleMenuMapper roleMenuDAO;
    
    @Override
    public boolean save(Menu record) {
        if(record.getId() == null || record.getId().intValue() < 1){
        	Menu parent = selectById(record.getParentId());
        	record.setParentIdPath(parent.getParentIdPath() + parent.getId() + "$");
        	boolean ret = this.insert(record);
        	RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(1L);
            roleMenu.setMenuId(record.getId());
        	roleMenuDAO.insert(roleMenu);
            return ret;
        }else{
            Menu old=this.selectById(record.getId());
            boolean cnt=this.updateById(record);
            if(!old.getParentId().equals(record.getParentId())){
                String oldParentIdPath = old.getParentIdPath() + old.getId() + "$";
                String newParentIdPath = record.getParentIdPath() + record.getId() + "$";
                menuMapper.updateChildParentPath(oldParentIdPath, newParentIdPath);
            }
            return cnt;
        }
    }

    @Cacheable(value = "commonCache", key = "'Menu_id_' + #id")
    @Override
    public Menu getByIdInCache(Long id) {
        return this.selectById(id);
    }

    
    @Override
    public void deleteById(Long id){
    	List<Menu> list = listSubMenuByParentId(String.valueOf(id));
    	if(list != null) {
    		for(Menu menu : list) {
    			roleMenuDAO.deleteByMenuId(menu.getId());
    		}
    	}
        this.deleteById(id);
        roleMenuDAO.deleteByMenuId(id);
    }

	public List<Menu> listSubMenuByParentId(String parentId) {
		EntityWrapper<Menu> ew = new EntityWrapper<Menu>();
		Menu menu = new Menu();
		menu.setParentId(Long.parseLong(parentId));
		ew.setEntity(menu);
		return this.selectList(ew.where("parentId={0}", parentId));
	}
	
	public boolean updateIcon(Long id, String icon) {
		Menu menu = new Menu();
		menu.setId(id);
		menu.setIcon(icon);
		return this.updateById(menu);
	}

}
