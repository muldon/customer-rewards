package com.siono.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.siono.service.RewardsService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RewardsTest {
	@Autowired
	RewardsService rewardsService;
	
	@Test
	@org.junit.jupiter.api.Order(1)
	public void rewardPointsTest() {
		
		Double transactionValue = 120d;
		int points = rewardsService.calculateRewardPoints(transactionValue);
		assertTrue(points == 90);  //with a scoring factor = 1
		
		transactionValue = 70d;
		points = rewardsService.calculateRewardPoints(transactionValue);
		assertTrue(points == 20); 
		
	}
	

}
