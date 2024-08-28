package com.tl.utils;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CafeUtils {

    private CafeUtils() {
    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        String jsonResponse = String.format("{\"message\":\"%s\"}", responseMessage);
        return new ResponseEntity<>(jsonResponse, httpStatus);
    }

    public static String getUUID() {
        return "Bill----" + UUID.randomUUID().toString();
    }

    public static JSONArray getJsonArray(String data) throws JSONException {
        return new JSONArray(data);
    }

    public static Map<String, Object> getMapFromJson(String data) {
        if (!StringUtils.hasText(data)) {
            return Collections.emptyMap(); 
        }
        return new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {}.getType());
    }
    
    public static Boolean isFileExistornot(String path) {
    	log.info("is file exist or not",path);
    	try {
    		File f=new File(path);
    		return(f!=null&&f.exists())?Boolean.TRUE:Boolean.FALSE;
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return false;
    	
    }
}
