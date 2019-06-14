package com.pdatao.api.service;


import com.pdatao.api.entity.Eval;


public interface EvalService {
	
	
	/**
	 * 增加或者修改订单列表
	 * @param order
	 * @return
	 */
	public int saveEval(Eval eval);
}
