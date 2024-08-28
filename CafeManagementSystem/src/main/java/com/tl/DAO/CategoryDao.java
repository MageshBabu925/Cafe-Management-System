package com.tl.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tl.POJO.Category;

public interface CategoryDao extends JpaRepository<Category,Integer> {
	
	List<Category> getallcategory();

}
