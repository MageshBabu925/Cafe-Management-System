package com.tl.wrapper;

import lombok.Data;

@Data
public class ProductWrapper {
	
	int id;
	String name;
	String description;
	double price;
	String status;
	int categoryId;
	String categoryName;
	
	public ProductWrapper() {
		
	}
	
	public ProductWrapper(int id, String name, String description, double price, String status,int categoryId,String categoryName) {
		
		this.id=id;
		this.name=name;
		this.description=description;
		this.price=price;
		this.status=status;
		this.categoryId=categoryId;
		this.categoryName=categoryName;
		
	}
	
	public ProductWrapper(int id,String name) {
		this.id=id;
		this.name = name;
	}

public ProductWrapper(int id, String name, String description, double price) {
		
		this.id=id;
		this.name=name;
		this.description=description;
		this.price=price;
		
	}

}
