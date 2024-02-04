package com.cs309.tutorial.tests;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
public class TestController {


//Here you get the current time
	@GetMapping("/getTime")
	public TimeResponse getCurrentTime() {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedTime = now.format(formatter);
			return new TimeResponse(formattedTime);
		}
		public class TimeResponse {
			private String time;

			public TimeResponse(String time) {
				this.time = time;
			}

			public String getTime() {
				return time;
			}
		}

//		public String getTest(@RequestParam(value = "username", defaultValue = "World") String message) {
//			return String.format("Hello, %s! Yes  it is working!", message);
//		}

	
	@GetMapping("/getTest")
	public String getTest(@RequestParam(value = "username", defaultValue = "World") String message) {
		return String.format("Hello, %s! You sent a get request with a parameter!", message);
	}

@PostMapping("/getTest")
public String getTest(@RequestBody Map<String, String> requestMap) {
	String username = requestMap.get("username");
	if (username == null) {
		username = "World";
	}
	return String.format("Hello, %s! You sent a post request with a parameter!", username);
}
	@PostMapping("/postTest1")
	public String postTest1(@RequestParam(value = "username", defaultValue = "World") String message) {
		//TODO
		return String.format("Hello, %s! You sent a post request with a parameter!", message);
	}
	
	@PostMapping("/postTest2")
	public String postTest2(@RequestBody TestData testData) {
		//TODO
		return String.format("Hello, %s! You sent a post request with a requestbody!", testData.getMessage());
	}
	
	@DeleteMapping("/deleteTest")
	public void deleteTest() {
		//TODO
	}
	
	@PutMapping("/putTest")
	public void putTest() {
		//TODO
	}
}
