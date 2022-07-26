package com.siono.configuration;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.siono.model.Order;
import com.siono.model.User;
import com.siono.repository.CustomerRewardsRepository;
import com.siono.repository.OrderRepository;
import com.siono.repository.UserRepository;
import com.siono.service.OrderService;
import com.siono.service.UserService;
import com.siono.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MockDataConfig {
	@Autowired
	OrderRepository orderRepository;	 
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CustomerRewardsRepository customerRewardsRepository;
	
	@Autowired
	OrderService orderService;

	@Autowired
	UserService userService;

	@PostConstruct
	public void mockData() {
		log.info("Simulating app data. First cleaning the dataset...");
		
		//I know I shouldn't access the database directly ... but ok, I am just a demo app...
		customerRewardsRepository.deleteAll();
		orderRepository.deleteAll();			
		userRepository.deleteAll();
		
		log.info("Now rebuilding it...");
		User user = Utils.createTestUser(1,"Rick Silva");
		userService.save(user);
		log.info("First user: "+user);
		
		User user2 = Utils.createTestUser(2,"Robert Caldwel");
		userService.save(user2);
		log.info("Second user: "+user2);
		
		
		List<Order> randomOrders = Utils.generateOrdersList(user.getId());
		for(Order order: randomOrders) {
			orderService.save(order);
		}
		
		log.info("Database all right. Let's check:");
		List<Order> allOrders = orderService.findAll(Sort.by(Sort.Direction.ASC,"date"));
		log.info("Printing orders over the last year... \n");
		for(Order order: allOrders) {
			log.info("Order: "+order.getId()+ " - value: "+order.getTotalValue()+ " - date: "+Utils.formatDate(order.getDate()));			
		}
		
	}
}
