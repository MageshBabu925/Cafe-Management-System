package com.tl.ControllerImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tl.Controller.Billreportrest;
import com.tl.POJO.Bill;
import com.tl.service.Billservice;

@RestController
public class Billreportimpl implements Billreportrest{
	
	@Autowired 
	private Billservice bs;

	@Override
	public ResponseEntity<String> generatereport(Map<String, Object> requestmap) {
		return bs.generate(requestmap);
	}

	@Override
	public ResponseEntity<List<Bill>> getbills() {
		return bs.getBills();
	}

	@Override
	public ResponseEntity<byte[]> getpdf(Map<String, Object> requestmap) {
		return bs.getPdf(requestmap);
	}

	@Override
	public ResponseEntity<String> deletebillbyid(Integer id) {
		return bs.deleteBillbyId(id);
	}

}
