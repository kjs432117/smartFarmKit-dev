package com.smart.kit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
//	@Value("${server.port}")
//	private int port;
//	포트번호
	static int port = 81;
	
//	키트주문번호(고유번호, 시리얼넘버역할)
	static String DEVICE_ID = "0";
//	ip주소
	static String address;
	
//	전원 On(프로그램시작) 시에 ip전송
	Logger() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
//			address = ip.getHostAddress() + ":" + port;
			address = "192.168.0.24:81";
//			address = "3.36.147.14:81";
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		
		List<String> list = new ArrayList<>();
		list.add(DEVICE_ID);
		list.add(address);
		String json = new Gson().toJson(list);
		
		// Parameters
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); 
		params.add("kit", json);		
		// params.add("key", "value");
		 
		// Header
		HttpHeaders headers = new HttpHeaders();
		
		// Request 설정 
		RestTemplate restTemplate = new RestTemplate();
		
//		String url = "http://192.168.0.35:80/prj/addressUpdate.do";
		String url = "http://localhost:80/prj/addressUpdate.do";
//		String url = "http://3.36.147.14:8080/prj/addressUpdate.do";
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		
		String response = restTemplate.postForObject(url, request, String.class);
		
		// Print Response
		System.out.println(response); 
	}
	
//	로그전송
	public String write(List<String> log) {
		
		HttpHeaders headers = new HttpHeaders();
		// 파라미터 설정 
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); 
		String json = new Gson().toJson(log);
		// params.add("key", "value");
		params.add("kit", json);
		params.add("id", DEVICE_ID);
		params.add("sd", SmartKit.format.format(SmartKit.startDate));
		// Request 설정 
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		RestTemplate restTemplate = new RestTemplate();
//		String url = "http://192.168.0.35:80/prj/logger.do";
		String url = "http://localhost:80/prj/logger.do"; 
//		String url = "http://3.36.147.14:8080/prj/logger.do"; 
		String response = restTemplate.postForObject(url, request, String.class);
		System.out.println(response); 
		
		return "log-updated";
	}
	
//	재배기록
	public String plantWrite(List<String> db) {
		
		HttpHeaders headers = new HttpHeaders();
		// 파라미터 설정 
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); 
		String json = new Gson().toJson(db);
		// params.add("key", "value");
		params.add("kit", json); 
		// Request 설정 
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		RestTemplate restTemplate = new RestTemplate();
//		String url = "http://192.168.0.35:80/prj/plantWrite.do";
		String url = "http://localhost:80/prj/plantWrite.do"; 
//		String url = "http://3.36.147.14:8080/prj/plantWrite.do"; 
		String response = restTemplate.postForObject(url, request, String.class);
		System.out.println(response); 
		
		return "Plant-updated";
	}
	
//	일지와 재배 기록
	public String diaryWrite(List<String> db) {
		
		HttpHeaders headers = new HttpHeaders();
		// 파라미터 설정 
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); 
		String json = new Gson().toJson(db);
		// params.add("key", "value");
		params.add("kit", json); 
		// Request 설정 
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		RestTemplate restTemplate = new RestTemplate();
//		String url = "http://192.168.0.35:80/prj/diaryWrite.do";
		String url = "http://localhost:80/prj/diaryWrite.do"; 
//		String url = "http://3.36.147.14:8080/prj/diaryWrite.do"; 
		String response = restTemplate.postForObject(url, request, String.class);
		System.out.println(response); 
		
		return "Diary&Plant-updated";
	}
}
