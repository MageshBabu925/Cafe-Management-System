package com.tl.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.tl.POJO.Category;

public interface Categoryservice {

	ResponseEntity<String> addcategory(Map<String, String> requestmap);

	ResponseEntity<List<Category>> getallcategory(String filtervalue);

	ResponseEntity<String> updatecategory(Map<String, String> requestmap);

}
