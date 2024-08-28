package com.tl.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.tl.wrapper.ProductWrapper;

public interface Productservice {

	ResponseEntity<String> addproduct(Map<String, String> requestmap);

	ResponseEntity<List<ProductWrapper>> getallproduct();

	ResponseEntity<String> updateproduct(Map<String, String> requestmap);

	ResponseEntity<String> deleteproduct(Integer id);

	ResponseEntity<String> updatestatus(Map<String, String> requestmap);

	ResponseEntity<List<ProductWrapper>> getcategoriesbyid(Integer id);

	ResponseEntity<ProductWrapper> getbyid(Integer id);

	

}
