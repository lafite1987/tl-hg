package cn.lfy.base.ctrl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.lfy.base.model.Menu;
import cn.lfy.base.model.TreeNode;
import cn.lfy.base.service.MenuService;
import cn.lfy.common.Result;
import cn.lfy.common.exception.ErrorCode;
import cn.lfy.common.exception.GlobalException;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/manager/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @RequestMapping("/api/list")
    @ResponseBody
    @ApiOperation(value = "菜单树", httpMethod = "POST", notes = "菜单树")
    public Result<List<TreeNode>> api_list() {
    	List<Menu> menus = menuService.selectList(null);
		List<TreeNode> treeList = Lists.newArrayList();
		for(Menu menu : menus) {
			treeList.add(new TreeNode(menu.getId(), menu.getName(), menu.getParentId(), false));
		}
		List<TreeNode> tree = Lists.newArrayList();
		for(TreeNode node1 : treeList){  
			tree.add(node1);   
		}
		Result<List<TreeNode>> resultDTO = Result.success();
		resultDTO.setData(tree);
    	return resultDTO;
    }
    
    @RequestMapping("/update")
    @ResponseBody
    @ApiOperation(value = "更新菜单", httpMethod = "POST", notes = "更新菜单")
    public Result<Void> update(Menu menu) {
    	Result<Void> resultDTO = Result.success();
        Menu menuDb=menuService.selectById(menu.getId());
        
        menuDb.setName(menu.getName());
        menuDb.setUrl(menu.getUrl());
        menuDb.setOrderNo(menu.getOrderNo());
        menuDb.setOnMenu(menu.getOnMenu());
        menuService.updateById(menuDb);
        return resultDTO;
    }
    
    @RequestMapping("/detail")
    @ResponseBody
    @ApiOperation(value = "菜单详情", httpMethod = "GET", notes = "获取菜单详细信息")
    public Result<Menu> detail(Long id) {
    	Result<Menu> resultDTO = Result.success();
        if(null != id) {
            Menu menu=menuService.selectById(id);
            resultDTO.setData(menu);
        }
        return resultDTO;
    }

    @RequestMapping("/add")
    @ResponseBody
    @ApiOperation(value = "添加菜单", httpMethod = "POST", notes = "添加菜单")
    public Result<Void> add(Menu menu) {
    	Result<Void> resultDTO = Result.success();
        menuService.save(menu);
        return resultDTO;
    }

    @RequestMapping("/del")
    @ResponseBody
    @ApiOperation(value = "删除菜单", httpMethod = "POST", notes = "删除菜单")
    public Result<Void> deleteTreeNode(Long id) {
    	Result<Void> resultDTO = Result.success();
        if(null == id) {
            throw GlobalException.newGlobalException(ErrorCode.PARAM_INVALID, "menuId");
        }
        menuService.deleteById(id);
        return resultDTO;
    }
}
