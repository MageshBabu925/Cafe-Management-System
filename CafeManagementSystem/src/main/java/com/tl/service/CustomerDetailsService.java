package com.tl.service;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tl.DAO.UserDao;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CustomerDetailsService implements UserDetailsService {

	@Autowired
	UserDao userdao;
	
	private com.tl.POJO.User userdetail;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		userdetail = userdao.findbyEmailId(username);
		
		log.info("Inside findbyEmailId {}",username);
		
		if(!Objects.isNull(userdetail)) 
			return new User(userdetail.getEmail(),userdetail.getPassword(),new ArrayList<>());
		else
			throw new UsernameNotFoundException("User name is not found");
		
	}
	public com.tl.POJO.User getuserdetails() {
		return userdetail;
	}
	
	

}
