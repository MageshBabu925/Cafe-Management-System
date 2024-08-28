package com.tl.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tl.DAO.BillDao;
import com.tl.DAO.CategoryDao;
import com.tl.DAO.ProductDao;
import com.tl.service.Dashboardservice;

@Service
public class Dashboardimpl implements Dashboardservice {
	
	@Autowired
	CategoryDao cdao;
	
	@Autowired
	ProductDao pdao;
	
	@Autowired
	BillDao bdao;

	@Override
	public ResponseEntity<Map<String, Object>> getCount() {
		Map<String,Object> map=new HashMap<>();
		map.put("category",cdao.count());
		map.put("prduct", pdao.count());
		map.put("bill", bdao.count());
		return new ResponseEntity<>(map,HttpStatus.OK);
	}

}
