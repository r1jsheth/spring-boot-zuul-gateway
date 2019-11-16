package com.api.gateway.zuul;

/*
 * zuul
 * @author: raj on 16/11/19
 */


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@Component
public class Gateway {

	@Value("${server.URI}")
	private String URI;

	@Value("${server.searchService.port}")
	private String SEARCH_SERVICE_PORT;

	@Value("${server.loginService.port}")
	private String LOGIN_SERVICE_PORT;

	@Value("${server.cartService.port}")
	private String CART_SERVICE_PORT;

	@Bean
	private RestTemplate getNewRestTemplate(){
		return new RestTemplate();
	}

	private RestTemplate restTemplate;

	public Gateway(){
		this.restTemplate = getNewRestTemplate();
	}

	@GetMapping("/search")
	public Object searchMedicines(@RequestParam String query){

		String url = "http://" + URI + ":" + SEARCH_SERVICE_PORT + "search?medicine=" + query;
		System.out.println(url);
		return restTemplate.getForObject(url,Object.class);

	}

	@PostMapping("/addUser")
	public ResponseEntity<String> addNewUser(@RequestBody Object userJSON){

		String url = "http://" + URI + ":" + LOGIN_SERVICE_PORT + "/addUser";
		ResponseEntity<String> message = restTemplate.postForEntity(url, userJSON, String.class);
		return message;
	}


	@GetMapping("/validate")
	public String validateUser(@RequestParam String email, @RequestParam String password){
		return this.validateInternal(email, password);
	}


	private String validateInternal(String email, String password){
		String url = "http://" + URI + ":" + LOGIN_SERVICE_PORT + "/validate?email="
				+ email + "&password=" + password;
		return restTemplate.getForObject(url,String.class);
	}


	@PostMapping("/addToCart")
	public ResponseEntity<String> addToCart(@RequestBody Object cartRequest,
	                                        @RequestParam String email,
	                                        @RequestParam String password){

		String message = this.validateInternal(email, password);
		ResponseEntity<String> responseMessage;
		if (message.contains("successful")){
			String url = "http://" + URI + ":" + CART_SERVICE_PORT + "/addToCart";
			responseMessage = restTemplate.postForEntity(url, cartRequest, String.class);
		}
		else {
			responseMessage = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		return responseMessage;

	}


	@PostMapping("/placeOrder")
	public ResponseEntity<String> addToCart(@RequestParam String userId,
	                        @RequestParam String email,
		                    @RequestParam String password){


		String message = this.validateInternal(email, password);
		ResponseEntity<String> responseMessage;
		if (message.contains("successful")){
			String url = "http://" + URI + ":" + CART_SERVICE_PORT + "/placeOrder?userId=" + userId;
			responseMessage = restTemplate.postForEntity(url, userId, String.class);
		}
		else responseMessage = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		return responseMessage;

	}


}
