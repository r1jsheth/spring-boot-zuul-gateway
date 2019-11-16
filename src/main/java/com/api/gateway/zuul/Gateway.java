package com.api.gateway.zuul;

/*
 * zuul
 * @author: raj on 16/11/19
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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

	RestTemplate restTemplate;

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

		String url = "http://" + URI + ":" + LOGIN_SERVICE_PORT + "/validate?email="
				+ email + "&password=" + password;
		String message = restTemplate.getForObject(url,String.class);
		return message;

	}


	@PostMapping("/addToCart")
	public ResponseEntity<String> addToCart(@RequestBody Object cartRequest){

		String url = "http://" + URI + ":" + CART_SERVICE_PORT + "/addToCart";
		return restTemplate.postForEntity(url, cartRequest, String.class);

	}


	@PostMapping("/placeOrder")
	public String addToCart(@RequestParam String userId){

		String url = "http://" + URI + ":" + CART_SERVICE_PORT + "/placeOrder?userId=" + userId;
		return restTemplate.getForObject(url, String.class);

	}


}
