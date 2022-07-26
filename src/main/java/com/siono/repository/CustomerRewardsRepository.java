package com.siono.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.siono.model.CustomerRewards;

@Repository
public interface CustomerRewardsRepository extends JpaRepository<CustomerRewards, Integer>{

	void deleteByOrderId(Integer orderId);

	List<CustomerRewards> findByCustomerIdAndDateAfter(Integer customerId, Timestamp dateFrom, Sort sort);

	 	
	  
}
