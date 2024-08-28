package com.tl.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tl.POJO.Category;

@RequestMapping(path="/category")
public interface Categoryrest {
	
	@PostMapping(path="/add")
	public ResponseEntity<String>addcategory(@RequestBody(required=true)Map<String,String>requestmap);
	
	@GetMapping(path="/get")
	public ResponseEntity<List<Category>>getcategory(@RequestParam(required=false)String filtervalue);
	
	@PostMapping(path="/update")
	public ResponseEntity<String>updatecategory(@RequestBody(required=true)Map<String,String>requestmap);

}
