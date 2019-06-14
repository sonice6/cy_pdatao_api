package com.pdatao.api.dao;



import com.pdatao.api.entity.Eval;


public interface EvalMapper {
	
		
	/**
     * 增加订单信息
     * @param order
     * @return
     */
    public int saveEval(Eval eval);
}
