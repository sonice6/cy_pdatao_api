package com.pdatao.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PhoneUtils {

	

public static boolean isPhone(String phone) {
    String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
    if (phone.length() != 11) {
    	System.out.println("长度不够");
        return false;//
    } else {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isMatch = m.matches();
        if (!isMatch) {
        	System.out.println("手机号不符合规则");
        	return false;//请填入正确的手机号
        }
        return isMatch;
    }
}

public static void main(String[] args) {
	System.out.println(isPhone("1340710534"));
}

}
