package com.tl.ControllerImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tl.Controller.Productrest;
import com.tl.service.Productservice;
import com.tl.wrapper.ProductWrapper;

@RestController
public class Productrestimpl implements Productrest{

	@Autowired
	Productservice ps;
	
	@Override
	public ResponseEntity<String> addproduct(Map<String, String> requestmap) {
		return ps.addproduct(requestmap);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getallproduct() {
		return ps.getallproduct();
	}

	@Override
	public ResponseEntity<String> updateproduct(Map<String, String> requestmap) {
		return ps.updateproduct(requestmap);
	}

	@Override
	public ResponseEntity<String> deleteproduct(Integer id) {		
		return ps.deleteproduct(id);
	}

	@Override
	public ResponseEntity<String> updatestatus(Map<String, String> requestmap) {
		return ps.updatestatus(requestmap);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getcategoriesbyid(Integer id) {
		return ps.getcategoriesbyid(id);
	}

	@Override
	public ResponseEntity<ProductWrapper> getbyid(Integer id) {
		return ps.getbyid(id);
	}



	

}
