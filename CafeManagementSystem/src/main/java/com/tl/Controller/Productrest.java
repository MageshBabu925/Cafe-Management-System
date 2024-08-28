package com.tl.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tl.wrapper.ProductWrapper;

@RequestMapping(path="/product")
public interface Productrest {	

	@PostMapping(path="/add")
	public ResponseEntity<String>addproduct(@RequestBody(required=true)Map<String,String>requestmap);
	
	@GetMapping(path="/get")
	public ResponseEntity<List<ProductWrapper>>getallproduct();
	
	@PostMapping(path="/update")
	public ResponseEntity<String>updateproduct(@RequestBody(required=true)Map<String,String>requestmap);
	
	@PostMapping(path="/delete/{id}")
	public ResponseEntity<String>deleteproduct(@PathVariable Integer id);
	
	@PostMapping(path="/updatestatus")
	public ResponseEntity<String>updatestatus(@RequestBody(required=true)Map<String,String>requestmap);
	
	@GetMapping(path="/getcategories/{id}")
	public ResponseEntity<List<ProductWrapper>>getcategoriesbyid(@PathVariable Integer id);
	
	@GetMapping(path="/getbyid/{id}")
	public ResponseEntity<ProductWrapper>getbyid(@PathVariable Integer id);
}
