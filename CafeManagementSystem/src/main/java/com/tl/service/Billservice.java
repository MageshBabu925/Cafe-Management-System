package com.tl.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.tl.POJO.Bill;

public interface Billservice {

	ResponseEntity<String> generate(Map<String, Object> requestmap);

	ResponseEntity<List<Bill>> getBills();

	ResponseEntity<byte[]> getPdf(Map<String, Object> requestmap);

	ResponseEntity<String> deleteBillbyId(Integer id);

}
