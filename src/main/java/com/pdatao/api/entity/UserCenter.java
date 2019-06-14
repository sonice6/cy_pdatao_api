package com.pdatao.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_user_center")
@Entity
public class UserCenter {
	
	private Long id;
	@Column(name = "user_name")
	private String userName;        //用户真实姓名
	@Column(name = "nick_name")
	private String nickName;       //用户昵称
	@Column(name = "password")
	private String password;       //密码
	@Column(name = "email")
	private String email;     //邮箱
	@Column(name = "userphoto")
	private String userPhoto;      //头像
	@Column(name = "phoneno")
	private String phoneno;      //手机
	@Column(name = "mobile")
	private String mobile;          //座机号码
	@Column(name = "deptid") 
	private Long deptId;     //部门ID
	@Column(name = "roleid")
	private Long roleId;        //角色id
	@Column(name = "status")
	private Integer status;        //	账号状态
	@Column(name = "sex")
	private Integer sex;        //	性别
	@Column(name = "wxopenid")
	private String wxopenid;        //	关联微信ID
	@Column(name = "qqopenid")
	private String qqopenid;        //	关联qqid
	@Column(name = "dr")
	private Integer dr;        //	删除标志
	@Column(name = "create_time")
	private Date createTime;        //	删除标志
	@Column(name = "create_id")
	private long createId;
	@Column(name = "update_time")
	private Date updateTime;
	@Column(name = "update_id")
	private Long updateId;
	@Column(name = "note")
	private String note;
	@Column(name = "ischange")
	private Integer isChange;
	@Column(name = "deptgroup")
	private Integer deptGroup;
	@Column(name = "coins_count")
	private Double coinsCount;
	
}
