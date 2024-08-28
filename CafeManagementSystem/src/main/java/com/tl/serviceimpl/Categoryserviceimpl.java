package com.tl.serviceimpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.tl.Constents.CafeConstants;
import com.tl.DAO.CategoryDao;
import com.tl.JWT.Jwtfilter;
import com.tl.POJO.Category;
import com.tl.service.Categoryservice;
import com.tl.utils.CafeUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Categoryserviceimpl implements Categoryservice{
	
	@Autowired
	CategoryDao cdao;
	
	@Autowired
	Jwtfilter jfilter;

	@Override
	public ResponseEntity<String> addcategory(Map<String, String> requestmap) {
		try {
			if(jfilter.isAdmin()) {
				if(validatecategory(requestmap,false)) {
					cdao.save(getcategory(requestmap, false));
					return CafeUtils.getResponseEntity("Category added successfully", HttpStatus.OK);
				}
				else {
					return CafeUtils.getResponseEntity("Failed to add category", HttpStatus.UNAUTHORIZED);
				}
			}
			
		}catch(Exception e) {
			
		}
		return CafeUtils.getResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validatecategory(Map<String, String> requestmap, boolean validateid) {
		if(requestmap.containsKey("name")) {
			if(requestmap.containsKey("id")&& validateid) {
				return true;
			}else if(!validateid){
				return true;
			}
		}
		return false;
	}
	
	private Category getcategory(Map<String,String>requestmap,boolean isadd) {
		Category c =new Category();
		if(isadd) {
			c.setId(Integer.parseInt(requestmap.get("id")));
		}
		c.setName(requestmap.get("name"));
		return c;
	}

	@Override
	public ResponseEntity<List<Category>> getallcategory(String filtervalue) {
		try {
			if(!Strings.isNullOrEmpty(filtervalue)&&filtervalue.equalsIgnoreCase("true")) {
				log.info("Inside if {}");
				return new ResponseEntity<List<Category>>(cdao.getallcategory(),HttpStatus.OK);
			}
			return new ResponseEntity<>(cdao.findAll(),HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<String> updatecategory(Map<String, String> requestmap) {
		try {
			if(jfilter.isAdmin()) {
				if(validatecategory(requestmap, true)) {
					Optional<Category> opt=cdao.findById(Integer.parseInt(requestmap.get("id")));
					if(!opt.isEmpty()) {
						cdao.save(getcategory(requestmap, true));
						return CafeUtils.getResponseEntity("Category Updated Successfully",HttpStatus.OK);
					}
					else {
						return CafeUtils.getResponseEntity("id for category does not exist",HttpStatus.NOT_FOUND);
					}
				}return CafeUtils.getResponseEntity(CafeConstants.InvalidData, HttpStatus.BAD_REQUEST);
				
			}else {
				return CafeUtils.getResponseEntity(CafeConstants.SomethingWentWrong, HttpStatus.UNAUTHORIZED);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SomethingWentWrong, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
