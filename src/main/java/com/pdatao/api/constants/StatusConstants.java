package com.pdatao.api.constants;


public enum StatusConstants {
	SUSSESS("200","SUSSESS"),
	MEMBER_PARAMETERS_ARE_EMPTY("22001","Parameters are empty!"),
	MEMBER_USER_IS_NOT_FOUND("22002","user is not find"),
	MEMBER_USER_IS_NO_LOGIN("22003","user is no login"),
	GIFT_GET_GIFT_LIST_NULL("22004","gift list is null"),
	GIFT_GET_GIFT_NULL("22005","get gift is null"),
	MEMBER_USER_IS_EXIST("22006","user is exist"),
	MEMBER_USER_REGIST_IS_ERROR("22007","user regist is error"),
	MEMBER_USER_ACCOUNT_IS_LOCK("22008","user account is locked"),
	MEMBER_TBACCOUNT_IS_EMPT("22009","taobao account is empt"),
	MEMBER_TBACCOUNT_TOTAL_NUM_ERROR("22010","taobao total num error"),
	MEMBER_TBACCOUNT_DATE_IS_ERROR("22011","taobao registd time low 10"),
	MEMBER_USER_IS_NULL("220012","user is null"),
	MEMBER_TRUE_NUM_IS_NULL("220013","true num is null"),
	MEMBER_USER_ORDER_IS_RUNNING("220014","user order is running"),
	MEMBER_WWACCOUNT_IS_EXISTS("220015","wwaccount is exists"),
	MEMBER_WXACCOUNT_IS_EXISTS("220016","wxaccount is exists"),
	MEMBER_CODE_IS_TIMEOUT("220017","code is timeout"),
	MEMBER_STATUA_IS_NOT_USER("220018","byuer is not internal user"),
	MEMBER_TAOBAOORDER_IS_NOT_FOUNT("220019","taobaoorder is not found"),
	MEMBER_ORDER_IS_ERROR("220020","order is error"),
	MEMBER_CASHAMOUNT_LESS("220021","cash mount less"),
	MEMBER_PHONE_NUM_IS_EXIST("220022","phone num is exist"),
	MEMBER_PHONE_NUM_NOT_RULE("220023","phone num not rule"),
	MEMBER_IP_COUNT_TOO_LONG("220024","ip > 50"),
	MEMBER_UPDATE_PASSWORD_IS_ERROR("220025","update password is error"),
	MEMBER_OLD_PASSWORD_IS_ERROR("220026","old password is error"),
	MEMBER_USER_IS_BIND_PHONE("220027","user is bind phoneno"),
	MEMBER_UPDATE_PHONE_NUM_IS_ERROR("220028","update user phone num is error"),
	GOODS_UPDATE_NOTFOND_IS_ERROR("330001","update goods not fond is error"),
	
	
	
	
	
	
	
	
	
	ORDER_CANCEL_ORDER_IS_ERROR("55001","cancel order is error"),
	;
	private String code;
	private String msg;

	private StatusConstants(String code,String msg){
		this.code=code;
		this.msg=msg;
	}
	
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
}
