package com.pdatao.api.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "tb_users")
@Entity
public class TbUser {
	
	@Id
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    private String utdid;
    private String umt;
    private String sid;
    private String hid;
    private String apdid;
    @Column(name = "umid_token")
    private String umidToken;
    @Column(name = "device_id")
    private String deviceId;
    @Column(name = "android_id")
    private String androidId;
    private String imei;
    private String imsi;
    private String mac;
    private String manufacturer;
    @Column(name = "device_name")
    private String deviceName;
    private String osVersion;
    private String apiVersion;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "json1")
    private String json1;
    @Column(name = "json2")
    private String json2;
    @Column(name = "json3")
    private String json3;
    private int active;
    @Column(name = "device_token_key")
    private String deviceTokenKey;
    @Column(name = "head_pic_link")
    private String headPicLink;
    private String mobile;
    @Column(name = "show_login_id")
    private String showLoginId;
    @Column(name = "taobao_nick")
    private String taobaoNick;
    @Column(name = "auto_login_token")
    private String autoLoginToken;
    @Column(name = "status")
    private int status;
    @Column(name = "login_ip")
    private String loginIp;
    @Column(name = "umidToken")
    private String umidToken1;

    @Column(name = "province")
    private String province;
    @Column(name = "city")
    private String city;
    @Column(name ="alipay_real_name_auth")
    private int alipayRealNameAuth;
    @Column(name = "wap_cookie")
    private String wapCookie;
    @Column(name = "tao_score")
    private int taoScore;

    public String toString() {
        return "User: (" + id + ")" + username;
    }
}
