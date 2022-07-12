package com.siono.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.siono.model.CustomerRewards;

@Repository
public interface CustomerRewardsRepository extends JpaRepository<CustomerRewards, Integer>{

	void deleteByOrderId(Integer orderId);

	 	
	  
}
