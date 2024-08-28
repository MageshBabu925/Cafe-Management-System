package com.tl.ControllerImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tl.Controller.Categoryrest;
import com.tl.POJO.Category;
import com.tl.service.Categoryservice;

@RestController
public class Categoryrestimpl implements Categoryrest{

	@Autowired
	Categoryservice cs;

	@Override
	public ResponseEntity<String> addcategory(Map<String, String> requestmap) {		
		return cs.addcategory(requestmap);
	}

	@Override
	public ResponseEntity<List<Category>> getcategory(String filtervalue) {
		return cs.getallcategory(filtervalue);
	}

	@Override
	public ResponseEntity<String> updatecategory(Map<String, String> requestmap) {
		return cs.updatecategory(requestmap);
	}
}
