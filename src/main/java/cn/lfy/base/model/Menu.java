package cn.lfy.base.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableName;
import com.google.common.collect.Lists;

import lombok.Data;

@Data
@TableName("menu")
public class Menu {
	
    private Long id;
    /**
     * 菜单名称
     */
    private String name = "";

    /**
     * 菜单所属分类，1为内勤人员功能菜单，2为外勤人员功能菜单
     */
    private Integer type = 1;

    /**
     * 父级菜单ID
     */
    private Long parentId;

    /**
     * 父级菜单ID串联，便于查询，格式：$1$2$
     */
    private String parentIdPath = "";

    /**
     * 菜单链接
     */
    private String url = "";

    /**
     * 排序
     */
    private Integer orderNo = 0;

    /**
     * 备注
     */
    private String remark = "";

    /**
     * 数据状态
     */
    private Integer state = 1;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    private Integer onMenu = 0;
    
    private String icon;

    private transient List<Menu> childList;
    
    private transient List<Menu> children = Lists.newArrayList();

    private transient Boolean checked;
    
    private transient Boolean disabled;
    

}
