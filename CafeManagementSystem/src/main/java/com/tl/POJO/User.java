package com.tl.POJO;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="User")

@NamedQuery(name="User.findbyEmailId", query="select u from User u where u.email=:email")

@NamedQuery(name="User.findAllUsers",query="select new com.tl.wrapper.UserWrapper(u.id,u.name,u.mobilenumber,u.email,u.password,u.status) from User u where u.role='user'")

@NamedQuery(name="User.updatestatus",query="update User u set u.status=:status where u.id=:id")

@NamedQuery(name="User.findAllAdminEmails",query="select u.email from User u where u.role='Admin'")

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	private String mobilenumber;
	private String email;
	private String password;
	private String status;	
	private String role;

}
