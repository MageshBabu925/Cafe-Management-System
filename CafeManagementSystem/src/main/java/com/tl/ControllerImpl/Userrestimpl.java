package com.tl.ControllerImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tl.Controller.Userrest;
import com.tl.service.Userservice;
import com.tl.wrapper.UserWrapper;

@RestController
public class Userrestimpl implements Userrest{
	
	

	@Autowired
	Userservice us;
	
	@Override
	public ResponseEntity<String>signup(@RequestBody(required=true)Map<String,String>requestmap){
		return us.Signup(requestmap);
			}
	
	@Override
	public ResponseEntity<String>login(@RequestBody(required=true)Map<String,String>requestmap){
		return us.login(requestmap);
			}

	@Override
	public ResponseEntity<List<UserWrapper>> getalluser() {
		return us.getallusers();
	}
	@Override
	public ResponseEntity<String>update(@RequestBody(required=true)Map<String,String>requestmap){
		return us.update(requestmap);
			}

	@Override
	public ResponseEntity<String> checktoken() {
		
		return us.checktoken();
	}

	@Override
	public ResponseEntity<String> changepassword(@RequestBody(required=true)Map<String, String> requestmap) {
		return us.changepassword(requestmap);
	}

	@Override
	public ResponseEntity<String> forgotpassword(Map<String, String> requestmap) {
		// TODO Auto-generated method stub
		return us.forgotpassword(requestmap);
	}

}
