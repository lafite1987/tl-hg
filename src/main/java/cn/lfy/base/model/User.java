package cn.lfy.base.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
@Data
@TableName("user")
public class User {
	
    private Long id;
    private String username;
    private String password;
    private String salt;
    private String nickname;
    private String email;
    private String phone;
    private Integer state;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
    @TableLogic
    private Integer deleted;
}
