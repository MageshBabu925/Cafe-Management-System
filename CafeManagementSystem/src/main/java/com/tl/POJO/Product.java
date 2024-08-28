package com.tl.POJO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(
	    name = "Product.getallproduct",
	    query = "SELECT new com.tl.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) FROM Product p"
	)


@NamedQuery(
		name="Product.updateproductstatus",
		query = "UPDATE Product p set p.status=:status where p.id=:id"
	)


@NamedQuery(
		name="Product.getproductbycategory",
		query = "select new com.tl.wrapper.ProductWrapper(p.id,p.name) from Product p where p.category.id=:id and p.status='true'"
	)

@NamedQuery(
		name="Product.getproductbyid",
		query = "select new com.tl.wrapper.ProductWrapper(p.id,p.name,p.description,p.price) from Product p where p.id=:id"
	)

@Data
@Entity
@Table(name="Product")

public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	private String description;
	private double price;
	private String status;

	@ManyToOne
	private Category category;

}