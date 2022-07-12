package com.siono.service;

public interface RewardsService {

	public int calculateRewardPoints(Double transactionValue);

	public void deleteByOrderId(Integer orderId);
}
