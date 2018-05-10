package cn.lfy.base.model;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;

@Data
@TableName("role")
public class Role implements Comparable<Role> {

	private Long id;
    /**
     * 父角色ID
     */
    private Long parentId;
    /**
     * 父節點路徑
     */
    private String parentIdPath;

    /**
     * 员工管理角色名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;
    /**
     * 角色級別
     */
    private Integer level;
    /**
     * 数据状态
     */
    private Integer state;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
    @TableLogic
    private Integer deleted;
    
    private transient List<Role> children = new ArrayList<Role>();
    
    private transient Boolean checked;
    
    private transient Boolean disabled;
    
	@Override
	public int compareTo(Role o) {
		if(this.getId() == o.getId()) {
			return 0;
		} else if(this.getId() > o.getId()) {
			return 1;
		}
		return -1;
	}
}
