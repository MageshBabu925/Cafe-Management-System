package com.tl.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tl.POJO.User;

import jakarta.transaction.Transactional;
@Repository
public interface UserDao extends JpaRepository<User,Integer> {

	User findbyEmailId(@Param ("email") String Email);
	
	User findByEmail(String email);
	
	List<User> findAll();
	
	 @Transactional
	    @Modifying
	    @Query(name="User.updatestatus")
	    int updateStatus(@Param("status") String status, @Param("id") Integer id);
	
	@Query(name="User.findAllAdminEmails")
    List<String> getAllAdminEmails();
	
}
