package com.pdatao.api.util;

public class RsobjectResult <T> {

	    private static final String CODE_SUCCESS = "200";

	    private static final String CODE_FAIL = "250";

	    private String code;
	    private T data;
	    private String msg;
	    private String total;

	    public RsobjectResult(){

	    }

	    public RsobjectResult(String code){
	        this.code = code;
	    }

	    public RsobjectResult(String code, T data){
	        this.code = code;
	        this.data = data;
	    }

	    public RsobjectResult(String code, String msg){
	        this.code = code;
	        this.msg = msg;
	    }
	    
	    public RsobjectResult(String code, T data,String total){
	        this.code = code;
	        this.data = data;
	        this.total = total;
	    }

	    public static RsobjectResult success(){
	        return new RsobjectResult(CODE_SUCCESS);
	    }

	    public static RsobjectResult success(Object data){
	        return new RsobjectResult(CODE_SUCCESS, data);
	    }

	    public static RsobjectResult fail(String msg){
	        return new RsobjectResult(CODE_FAIL, msg);
	    }

	    public static RsobjectResult widthCode(String errorCode) {
	        return new RsobjectResult(errorCode);
	    }
	    
	    public static RsobjectResult define(String code,Object data){
	    	return new RsobjectResult(code,data);
	    }
	    
	    public static RsobjectResult defines(String code,Object data,String total){
	    	return new RsobjectResult(code,data,total);
	    }

	    public String getMsg() {
	        return msg;
	    }

	    public void setMsg(String msg) {
	        this.msg = msg;
	    }

	    public String getCode() {
	        return code;
	    }

	    public void setCode(String code) {
	        this.code = code;
	    }

	    public T getData() {
	        return data;
	    }

	    public void setData(T data) {
	        this.data = data;
	    }
	    
	    public String getTotal()
	    {
	    	return total;
	    }
	    
	    public void setTotal(String total)
	    {
	    	this.total= total;
	    }
	

}
