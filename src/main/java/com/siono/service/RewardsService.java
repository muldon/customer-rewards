package com.siono.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Sort;

import com.siono.model.CustomerRewards;
import com.siono.model.SearchParams;

public interface RewardsService {

	public int calculateRewardPoints(Double transactionValue);

	public void deleteByOrderId(Integer orderId);

	public List<CustomerRewards> findByCustomerIdAndDateAfter(Integer customerId, Timestamp dateFrom, Sort sort);
	
	 
}
