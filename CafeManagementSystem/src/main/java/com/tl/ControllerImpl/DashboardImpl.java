package com.tl.ControllerImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tl.Controller.Dashboardrest;
import com.tl.service.Dashboardservice;

@RestController
public class DashboardImpl implements Dashboardrest {
	
	@Autowired
	private Dashboardservice ds;

	@Override
	public ResponseEntity<Map<String, Object>> getCount() {		
		return ds.getCount();
	}
	

}
