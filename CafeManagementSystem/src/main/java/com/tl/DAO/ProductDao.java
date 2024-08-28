package com.tl.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tl.POJO.Product;
import com.tl.wrapper.ProductWrapper;

import jakarta.transaction.Transactional;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {


	List<ProductWrapper> getallproduct();

	@Modifying
	@Transactional
	Integer updateproductstatus(@Param("status") String status, @Param("id") int id);

	List<ProductWrapper> getproductbycategory(@Param("id") Integer id);

	ProductWrapper getproductbyid(@Param("id") Integer id);

}
