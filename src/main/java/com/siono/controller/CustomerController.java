package com.siono.controller;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.siono.model.Order;
import com.siono.model.SearchParams;
import com.siono.model.Statement;
import com.siono.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/customer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

	@Autowired
	OrderService orderService;

	
	@GetMapping("/statement/{customerId}")
	@ResponseStatus(HttpStatus.OK)
	public Statement getStatament(@PathVariable(value = "customerId") Integer customerId, @PathVariable(value = "lastNDays") Integer lastNDays) {
		SearchParams searchParams = new SearchParams();
		Order orderWrapper = new Order();
		
		searchParams.setInitDate(Timestamp.from(Instant.now().minus(Duration.ofDays(lastNDays))));  //last n days from Front End
		
		orderService.findByFilters(searchParams,orderWrapper);
		Statement wrapper = new Statement();
		wrapper.setOrders(orderWrapper.getData());
		
		return wrapper;
	}
	 
	 
}
