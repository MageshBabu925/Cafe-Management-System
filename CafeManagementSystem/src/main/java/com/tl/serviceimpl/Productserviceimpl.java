package com.tl.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tl.Constents.CafeConstants;
import com.tl.DAO.ProductDao;
import com.tl.JWT.Jwtfilter;
import com.tl.POJO.Category;
import com.tl.POJO.Product;
import com.tl.service.Productservice;
import com.tl.utils.CafeUtils;
import com.tl.wrapper.ProductWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Productserviceimpl implements Productservice{
	
	@Autowired
	ProductDao pdao;
	
	@Autowired
	Jwtfilter jfilter;

	@Override
	public ResponseEntity<String> addproduct(Map<String, String> requestmap) {
		try {
			if(jfilter.isAdmin()) {
				if(validateproductmap(requestmap,false)) {
					pdao.save(getproductmap(requestmap,false));
					return CafeUtils.getResponseEntity("Product Added Successfully", HttpStatus.OK);
				}
				return CafeUtils.getResponseEntity(CafeConstants.InvalidData, HttpStatus.BAD_REQUEST);
			}else
				return CafeUtils.getResponseEntity(CafeConstants.Unauthorized_access, HttpStatus.UNAUTHORIZED);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SomethingWentWrong, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	

	private boolean validateproductmap(Map<String, String> requestmap, boolean validateid) {
		if(requestmap.containsKey("name")) {
			if(requestmap.containsKey("id")&& validateid) {
				return true;
			}else if(!validateid){
				return true;
			}
		}
		return false;
	}
	
	private Product getproductmap(Map<String, String> requestmap, boolean isAdd) {
		
		Category c = new Category();
		c.setId(Integer.parseInt(requestmap.get("categoryId")));
		
		Product p = new Product();
		if(isAdd) {
			p.setId(Integer.parseInt(requestmap.get("id")));
		}else 
		{
			p.setStatus("true");
		}
		p.setCategory(c);
		p.setName(requestmap.get("name"));
		p.setDescription(requestmap.get("description"));
		p.setPrice(Double.parseDouble(requestmap.get("price")));
		return p;
	}

	


	@Override
	public ResponseEntity<List<ProductWrapper>> getallproduct() {
	    try {
	        List<ProductWrapper> products = pdao.getallproduct();
	        return new ResponseEntity<>(products, HttpStatus.OK);
	    } catch (Exception e) {
	        log.error("Unexpected error while fetching products: {}", e.getMessage(), e);
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public ResponseEntity<String> updateproduct(Map<String, String> requestmap) {
		try {
			if(jfilter.isAdmin()) {
				if(validateproductmap(requestmap, true)) {
					Optional<Product> opt=pdao.findById(Integer.parseInt(requestmap.get("id")));
					if(!opt.isEmpty()){
						Product pro=getproductmap(requestmap, true);
						pro.setStatus(opt.get().getStatus());
						pdao.save(pro);
						return CafeUtils.getResponseEntity("Product Updated Successfully",HttpStatus.OK);
					}else {
						return CafeUtils.getResponseEntity("Product Id Does Not Exist",HttpStatus.NOT_FOUND);
					}
				}
				return CafeUtils.getResponseEntity(CafeConstants.InvalidData, HttpStatus.BAD_REQUEST);
				
			}else {
				return CafeUtils.getResponseEntity(CafeConstants.Unauthorized_access, HttpStatus.UNAUTHORIZED);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SomethingWentWrong, HttpStatus.INTERNAL_SERVER_ERROR);
	}



	@Override
	public ResponseEntity<String> deleteproduct(Integer id) {
		try {
			if(jfilter.isAdmin()) {
				Optional<Product> opt=pdao.findById(id);
				if(!opt.isEmpty()) {
					pdao.deleteById(id);
					return CafeUtils.getResponseEntity("Product Delete Successfully", HttpStatus.OK);
				}
				return CafeUtils.getResponseEntity("Product Id Does Not exist", HttpStatus.NOT_FOUND);
			}
			else {
				return CafeUtils.getResponseEntity(CafeConstants.Unauthorized_access, HttpStatus.UNAUTHORIZED);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SomethingWentWrong, HttpStatus.INTERNAL_SERVER_ERROR);
	}



	@Override
	public ResponseEntity<String> updatestatus(Map<String, String> requestmap) {
		try {
			if(jfilter.isAdmin()) {
				Optional<Product> opt=pdao.findById(Integer.parseInt(requestmap.get("id")));
				if(!opt.isEmpty()) {
					pdao.updateproductstatus(requestmap.get("status"),Integer.parseInt(requestmap.get("id")));
					return CafeUtils.getResponseEntity("Product Status updated Successfully", HttpStatus.OK);
				}
				return CafeUtils.getResponseEntity("Product Id Does Not exist", HttpStatus.NOT_FOUND);
			}
			else {
				return CafeUtils.getResponseEntity(CafeConstants.Unauthorized_access, HttpStatus.UNAUTHORIZED);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SomethingWentWrong, HttpStatus.INTERNAL_SERVER_ERROR);
	}



	@Override
	public ResponseEntity<List<ProductWrapper>> getcategoriesbyid(Integer id) {
		try {
			return new ResponseEntity<>(pdao.getproductbycategory(id),HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}



	@Override
	public ResponseEntity<ProductWrapper> getbyid(Integer id) {
		try {
			return new ResponseEntity<>(pdao.getproductbyid(id),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
