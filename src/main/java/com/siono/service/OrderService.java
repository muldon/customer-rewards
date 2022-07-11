package com.siono.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.siono.model.MessageResponse;
import com.siono.model.Order;
import com.siono.model.SearchParams;

public interface OrderService {

	public List<Order> findAll(Sort by);

	public void findByFilters(SearchParams searchParams, Order wrapper);

	public Order findById(Integer id);

	public List<Order> findByCustomerId(Integer customerId, Sort by);

	public void deleteById(Integer id);

	public MessageResponse save(Order to);

	public Order updateById(Integer id, Order order);

	public MessageResponse changeStatus(Order order);

}
