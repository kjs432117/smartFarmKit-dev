package com.smart.kit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

@Component
public class Logger {
	
	@Value("${server.port}")
//	private int port;
	
	static String DEVICE_ID = "1";
	static String address;
	static int port = 81;
	
	//최초실행 기기 ip주소 전송
	Logger() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			address = ip.getHostAddress() + ":" + port;
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		List<String> list = new ArrayList<>();
		list.add(DEVICE_ID);
		list.add(address);
		String json = new Gson().toJson(list);
		
		// Parameters
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); 
		params.add(DEVICE_ID, json);		
		// params.add("key", "value");
		 
		// Header
		HttpHeaders headers = new HttpHeaders();
		
		// Request 설정 
		RestTemplate restTemplate = new RestTemplate();
		
		String url = "http://localhost:80/prj/testURL.do";
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		
		String response = restTemplate.postForObject(url, request, String.class);
		
		// Print Response
		System.out.println(response); 
	}
	
	public String write(List<String> log) {
		
		HttpHeaders headers = new HttpHeaders();
		// 파라미터 설정 
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); 
		String json = new Gson().toJson(log);
		// params.add("key", "value");
		params.add(DEVICE_ID, json); 
		// Request 설정 
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:80/prj/testURL.do"; 
		String response = restTemplate.postForObject(url, request, String.class);
		System.out.println(response); 
		
		return "log-updated";
	}

}
