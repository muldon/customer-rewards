package com.siono.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siono.aspect.Loggable;
import com.siono.model.CustomerRewards;
import com.siono.service.MainService;
import com.siono.service.RewardsService;


@Service
@Transactional
public class RewardsServiceImpl extends MainService implements RewardsService{	   
	
	@Value("${scoring.factor}")
	private Integer scoringFactor;
	
	@Value("${rewardBaseline1}")
	private Integer rewardBaseline1;
	
	@Value("${rewardBaseline2}")
	private Integer rewardBaseline2;
	
	 
	/*
	 * Rule: For every dollar spent over $50 (rewardBaseline1) on the transaction, the customer receives one x (scoring.factor) point(s). 
	 * In addition, for every dollar spent over $100 (rewardBaseline2), the customer receives another one x (scoring.factor) point(s).
	 */
	@Transactional(readOnly = true)
	public int calculateRewardPoints(Double transactionValue) {
		int totalTransaction = transactionValue.intValue();  //other rounding methods could also be applied other than this simple one.
		int reward = 0;
		if(totalTransaction > rewardBaseline1) {
			reward += (totalTransaction - rewardBaseline1) * scoringFactor;
		}
		if(totalTransaction > rewardBaseline2) {
			reward += (totalTransaction - rewardBaseline2) * scoringFactor;
		} 
		
		return reward;
		 
	}
	

	@Override
	@Transactional(readOnly = true)	
	public List<CustomerRewards> findByCustomerIdAndDateAfter(Integer customerId, Timestamp dateFrom, Sort sort) {
		return customerRewardsRepository.findByCustomerIdAndDateAfter(customerId,dateFrom, sort);
	}



	// -----------------------------------------------   Transacional Methods ------------------------------------------ //
	
	@Loggable  
	@Override
	public void deleteByOrderId(Integer orderId) {
		customerRewardsRepository.deleteByOrderId(orderId);
		
	}


	

	 

}
