package com.tl.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tl.POJO.Bill;

@RequestMapping(path="/bill")
public interface Billreportrest {
	
	@PostMapping(path="/generate")
	public ResponseEntity<String>generatereport(@RequestBody (required=true) Map<String,Object> requestmap);
	
	@GetMapping(path="/getbills")
	public ResponseEntity<List<Bill>>getbills();
	
	@PostMapping(path="/getpdf")
	public ResponseEntity<byte[]>getpdf(@RequestBody  Map<String,Object>requestmap);
	
	@PostMapping(path="/deletebyid/{id}")
	public ResponseEntity<String>deletebillbyid(@PathVariable Integer id);
}
