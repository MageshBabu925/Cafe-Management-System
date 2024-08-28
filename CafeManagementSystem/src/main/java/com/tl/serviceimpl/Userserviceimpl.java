package com.tl.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.tl.Constents.CafeConstants;
import com.tl.DAO.UserDao;
import com.tl.JWT.JwtUtils;
import com.tl.JWT.Jwtfilter;
import com.tl.POJO.User;
import com.tl.service.CustomerDetailsService;
import com.tl.service.Userservice;
import com.tl.utils.CafeUtils;
import com.tl.utils.Emailutils;
import com.tl.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Userserviceimpl implements Userservice {

    @Autowired
    private UserDao userdao;

    @Autowired
    private AuthenticationManager am;

    @Autowired
    private CustomerDetailsService cds;

    @Autowired
    private JwtUtils jwtutil;

    @Autowired
    private Jwtfilter jfilter;
    
    @Autowired
    private Emailutils eutils;

    @Override
    public ResponseEntity<String> Signup(Map<String, String> requestMap) {
        log.info("Inside Signup with request: {}", requestMap);
        try {
            if (validateSignupMap(requestMap)) {
                User user = userdao.findbyEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userdao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully registered", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.InvalidData, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Error during signup: {}", e.getMessage());
            return CafeUtils.getResponseEntity(CafeConstants.SomethingWentWrong, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean validateSignupMap(Map<String, String> requestMap) {
        return requestMap.keySet().containsAll(Set.of("name", "mobilenumber", "email", "password"));
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setMobilenumber(requestMap.get("mobilenumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setRole(requestMap.get("role"));
        user.setStatus("false");
        return user;
    }

    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside Login");
        try {
            Authentication auth = am.authenticate(new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
            if (auth.isAuthenticated()) {
                if (cds.getuserdetails().getStatus().equalsIgnoreCase("true")) {
                    String token = jwtutil.generateToken(cds.getuserdetails().getEmail(), cds.getuserdetails().getRole());
                    return new ResponseEntity<>(String.format("{\"token\":\"%s\"}", token), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Wait for Admin approval", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
        }
        return new ResponseEntity<>("Bad Credentials", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getallusers() {
        try {
            if (jfilter.isAdmin()) {
                List<User> users = userdao.findAll();
                List<UserWrapper> userWrappers = users.stream()
                        .map(user -> new UserWrapper(user.getId(), user.getName(), user.getMobilenumber(), user.getEmail(), user.getPassword(), user.getStatus()))
                        .collect(Collectors.toList());
                return new ResponseEntity<>(userWrappers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("Error fetching users: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jfilter.isAdmin()) {
                Optional<User> optional = userdao.findById(Integer.parseInt(requestMap.get("id")));
                if (optional.isPresent()) {
                    userdao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmins(requestMap.get("status"),optional.get().getEmail(),userdao.getAllAdminEmails());
                    return CafeUtils.getResponseEntity("User status updated successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("User ID does not exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.Unauthorized_access, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("Error updating user status: {}", e.getMessage());
            return CafeUtils.getResponseEntity(CafeConstants.SomethingWentWrong, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendMailToAllAdmins(String status, String user, List<String> allAdminEmails) {
        allAdminEmails.remove(jfilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")) {
            eutils.sendSimpleMessage(jfilter.getCurrentUser(), "Account Approved", 
                "User:-" + user + "\n approved by \n admin:-" + jfilter.getCurrentUser(), allAdminEmails);
        } else {
            eutils.sendSimpleMessage(jfilter.getCurrentUser(), "Account Disabled", 
                "User:-" + user + "\n disabled by \n admin:-" + jfilter.getCurrentUser(), allAdminEmails);
        }
    }

	@Override
	public ResponseEntity<String> checktoken() {
		return CafeUtils.getResponseEntity("true", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<String> changepassword(Map<String, String> requestmap) {
	    try {
	        User userobj = userdao.findByEmail(jfilter.getCurrentUser());
	        if (userobj != null) {
	            if (!userobj.getPassword().equals(requestmap.get("oldPassword"))) {
	                return CafeUtils.getResponseEntity("Incorrect Old Password", HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	            userobj.setPassword(requestmap.get("newPassword"));
	            userdao.save(userobj);
	            
	            return CafeUtils.getResponseEntity("Successfully changed password", HttpStatus.OK); 
	        }
	        return CafeUtils.getResponseEntity("User not found", HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return CafeUtils.getResponseEntity(CafeConstants.SomethingWentWrong, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public ResponseEntity<String> forgotpassword(Map<String, String> requestmap) {
		try {
			User user=userdao.findByEmail(requestmap.get("email"));
			if(!Objects.isNull(user)&&!Strings.isNullOrEmpty(user.getEmail()))
				eutils.forgotmail(user.getEmail(),"Credentials provided by Cafe Management",user.getPassword());
			return CafeUtils.getResponseEntity("Check your mail for credentials", HttpStatus.OK);
		}catch(Exception e) {
			
		}
	return CafeUtils.getResponseEntity(CafeConstants.SomethingWentWrong, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
