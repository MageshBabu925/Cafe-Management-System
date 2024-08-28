
package com.tl.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.tl.wrapper.UserWrapper;

public interface Userservice {

	ResponseEntity<String> Signup(Map<String,String>requestMap);

	ResponseEntity<String> login(Map<String, String> requestmap);
	
	ResponseEntity<List<UserWrapper>> getallusers();

	ResponseEntity<String> update(Map<String, String> requestmap);

	ResponseEntity<String> checktoken();

	ResponseEntity<String> changepassword(Map<String, String> requestmap);

	ResponseEntity<String> forgotpassword(Map<String, String> requestmap);


}
