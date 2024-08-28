package com.tl.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWrapper {

	private int id;
	private String name;
	private String mobilenumber;
	private String email;
	private String password;
	private String status;
	
	public UserWrapper(int id,String name,String mobilenumber,String email,String password,String status) {
		
		this.id = id;
		this.name = name;
		this.mobilenumber = mobilenumber;
		this.email = email;
		this.password = password;
		this.status = status;
	}
}
