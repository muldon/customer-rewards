package com.siono.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siono.aspect.Loggable;
import com.siono.model.MessageResponse;
import com.siono.model.Order;
import com.siono.model.SearchParams;
import com.siono.service.MainService;
import com.siono.service.OrderService;
import com.siono.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class OrderServiceImpl extends MainService implements OrderService{	     
	
	  
	private void verifyIfExists(Integer id) {
        this.orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: Id not found: "+id));
    }

	@Transactional(readOnly = true)
	public List<Order> findAll(Sort by) {
		List<Order> tos = orderRepository.findAll(by);		
		return tos;		
	} 
	 
	
	@Transactional(readOnly = true)
	public void findByFilters(SearchParams searchParams, Order wrapper) {
		Utils.checkSearchParams(searchParams);
		
		genericRepository.findOrdersByFilters(searchParams,wrapper);
				
		 
	}

	@Transactional(readOnly = true)
	public Order findById(Integer id) {
		Order v = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: Id not found: "+id));
		return v;				
	}

	@Transactional(readOnly = true)
	public List<Order> findByIdCustomerId(Integer customerId,Sort by) {		
		 Optional<List<Order>> orders = orderRepository.findByCustomerId(customerId,by);
		 
		 if(!orders.isPresent()) {
			 log.debug("No order not found for user: "+customerId);			 
			 return null;
		 }
			 		 
		 return orders.get();
	}
	
	 
	   
	// -----------------------------------------------   Transacional Methods ------------------------------------------ //
	
	@Loggable  
	public void deleteById(Integer id)  {
		this.verifyIfExists(id);

        this.orderRepository.deleteById(id);
		
	}

	public MessageResponse save(Order to) {
		
		if(to.getId()==null && to.getDate()==null) { //new order
			to.setDate(new Timestamp(System.currentTimeMillis()));
		}
		
		orderRepository.save(to);		
		
        return createMessageResponse("Order saved");
	}

	public Order updateById(Integer id, Order order) {
		order.setId(id);
		Order saved = orderRepository.save(order);
		return saved;
	}

	public MessageResponse changeStatus(Order order) {
		orderRepository.changeStatus(order.getId(), order.getStatusId());
		return createMessageResponse("Status updated");
	}
	 

}
