package com.tl.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface Dashboardservice {

	ResponseEntity<Map<String, Object>> getCount();

}
