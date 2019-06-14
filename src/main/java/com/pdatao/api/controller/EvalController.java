package com.pdatao.api.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pdatao.api.constants.StatusConstants;
import com.pdatao.api.entity.Eval;
import com.pdatao.api.service.EvalService;
import com.pdatao.api.util.RsobjectResult;

@RestController
public class EvalController {
	
	
	@Autowired
	private EvalService evalService;
	
	
	/**
	 * 增加或者修改订单
	 * 
	 * @param request
	 * @param userCenter
	 * @return
	 */
	@RequestMapping(value = "/addEval", method = RequestMethod.POST)
	public @ResponseBody RsobjectResult<?> saveOrUpdateOrder(HttpServletResponse response,HttpServletRequest request, Eval eval) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if (eval != null) {
			Integer row = evalService.saveEval(eval);
			return RsobjectResult.define(StatusConstants.SUSSESS.getCode(), row);
		}else
		{
			return RsobjectResult.define(StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getCode(), StatusConstants.MEMBER_PARAMETERS_ARE_EMPTY.getMsg());
		}
	}
	
}
