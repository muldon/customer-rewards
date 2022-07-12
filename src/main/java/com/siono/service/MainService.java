package com.siono.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.siono.model.MessageResponse;
import com.siono.repository.CustomerRewardsRepository;
import com.siono.repository.GenericRepository;
import com.siono.repository.OrderRepository;
import com.siono.repository.UserRepository;
import com.siono.utils.Utils;

public abstract class MainService {

	@Autowired
	protected OrderRepository orderRepository;	 
	
	@Autowired
	protected UserRepository userRepository;
	
	@Autowired
	protected CustomerRewardsRepository customerRewardsRepository;
	
	
	@Autowired
	protected GenericRepository genericRepository;
	
	
	@Autowired
	protected Utils utils;
	 

	protected MessageResponse createMessageResponse(String message, Integer id) {
		return new MessageResponse(message);
	}
	
	protected MessageResponse createMessageResponse(String message) {
		return new MessageResponse(message);
	}

	 

}
