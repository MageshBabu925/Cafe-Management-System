package com.tl.POJO;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(
			name="Bill.getAllBills",
			query="select b from Bill b order by b.id desc"
			)

@NamedQuery(
		name="Bill.getBillByUser",
		query="select b from Bill b where b.createdBy=:username order by b.id desc"
		)

@Data
@Entity
@Table(name="Bill")
public class Bill {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String uuid;
	private String name;
	private String email;
	private long contactNumber;
	private String paymentMethod;
	private int totalAmount;
	@Column(name = "product_details", length = 5000)
	private String productDetails;
	private String createdBy;
	

}


