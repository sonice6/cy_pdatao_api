package com.pdatao.api.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pdatao.api.dao.EvalMapper;
import com.pdatao.api.entity.Eval;
import com.pdatao.api.service.EvalService;

@Component
public class EvalServiceImpl implements EvalService {

	
	@Autowired
	private EvalMapper evalDao;

	@Override
	public int saveEval(Eval eval) {
		// TODO Auto-generated method stub
		return evalDao.saveEval(eval);
	}

}
