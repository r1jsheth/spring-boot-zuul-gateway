package com.api.gateway.zuul;

/*
 * zuul
 * @author: raj on 16/11/19
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Gateway {

	@Value("${server.URI}")
	private static String URI;

	@Value("${server.searchService.port}")
	private static String SEARCH_SERVICE_PORT;

	@Value("${server.loginService.port}")
	private static String LOGIN_SERVICE_PORT;

	@Value("${server.cartService.port}")
	private static String CART_SERVICE_PORT;

	@Bean
	private RestTemplate getNewRestTemplate(){
		return new RestTemplate();
	}

	RestTemplate restTemplate;

	public Gateway(){
		this.restTemplate = getNewRestTemplate();
	}

	@GetMapping("/login")
	public String login(@RequestBody Object inputObject){
		Object object = this.restTemplate.getForObject(URI+LOGIN_SERVICE_PORT,Object.class);
		return "Done";
	}

	@GetMapping("/search")
	public Object searchMedicines(@RequestParam String query){


		String url = URI+SEARCH_SERVICE_PORT+"/?query="+query;
		
		Object object = this.restTemplate.getForObject(URI+SEARCH_SERVICE_PORT,Object.class );
		return object;
	}
}
