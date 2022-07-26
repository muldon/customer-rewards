package com.siono.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import com.siono.model.Order;
import com.siono.model.Order.OrderStatusEnum;
import com.siono.model.SearchParams;
import com.siono.model.User;
import com.siono.service.OrderService;
import com.siono.service.RewardsService;
import com.siono.service.UserService;
import com.siono.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrdersTest {
	public static Integer orderId;
	public static Integer userId;
	 
	@Autowired
	OrderService orderService;

	@Autowired
	UserService userService;
	
	@Autowired
	RewardsService rewardsService;
 
	@Test
	@org.junit.jupiter.api.Order(1)
	public void createTest() {
		log.debug("createTest initiated");
		User user = Utils.createTestUser(1,"Rick Silva");
		userService.save(user);
		userId = user.getId();
		Order order = Utils.createTestOrder(user.getId());
		orderService.save(order);
		orderId = order.getId();
		assertTrue(order.getId()!=null);
	}
	
	@Test
	@org.junit.jupiter.api.Order(2)
	public void readTest() {
		List<Order> allOrders = orderService.findAll(Sort.by(Sort.Direction.ASC,"id"));
		log.debug("readTest done. Orders retrieved: "+allOrders.size());
		assertTrue(allOrders.size()>0); //the @org.junit.jupiter.api.Order(2) annotation guarantees that the previous method (insert) will be invoked before		
	}
	
	@Test
	@org.junit.jupiter.api.Order(3)
	public void updateTest() {
		List<Order> allOrders = orderService.findAll(Sort.by(Sort.Direction.ASC,"id"));
		Order first = allOrders.get(0);
		first.setStatusId(OrderStatusEnum.PROCESSING.getId());
		orderService.updateById(first.getId(), first);
		log.debug("updateTest - order updated");
		Order firstTmp = orderService.findById(first.getId());
		assertTrue(firstTmp.getStatusId().equals(OrderStatusEnum.PROCESSING.getId()));
		
	}
	
	@Test
	@org.junit.jupiter.api.Order(4)
	public void deleteTest() {
		
		//first delete the Customer Rewards related to that order
		rewardsService.deleteByOrderId(orderId);
		log.info("deleteTest - before deleting the order");
		orderService.deleteById(orderId);
		
		Exception exception = assertThrows(RuntimeException.class, () -> {
			orderService.findById(orderId); 
	    });

	    String expectedMessage = "Error: Id not found: "+orderId;
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	@org.junit.jupiter.api.Order(5)
	public void generateOrdersTest() {
		List<Order> randomOrders = Utils.generateOrdersList(userId);
		assertTrue(randomOrders.size()>0);
		
	}
	
	
	@Test
	@org.junit.jupiter.api.Order(6)
	public void findByFiltersTest() {
		
		SearchParams searchParams = new SearchParams();
		searchParams.setIdStatus(OrderStatusEnum.APPROVED.getId());
		searchParams.setSortField("date");
		
		Order order = new Order();
		orderService.findByFilters(searchParams,order);
		
		log.info("findByFiltersTest - orders: "+order.getData().size() +" for the default rows per page");
		assertTrue(order.getData().size()==10);
		
		searchParams.setNumRowsPerPage(5);
		searchParams.setRowOffset(2);
		orderService.findByFilters(searchParams,order);
		log.info("findByFiltersTest - now checking the second page with 5 rows per page: "+order.getData().size());
		assertTrue(order.getData().size()==5);
		
		searchParams = new SearchParams();
		searchParams.setInitDate(Timestamp.from(Instant.now().minus(Duration.ofDays(3 * 30))));  //last 3 months
		orderService.findByFilters(searchParams,order);
		log.info("findByFiltersTest - now checking orders with date > "+Utils.formatDate(searchParams.getInitDate())+ " : "+ order.getData().size());
		
		
		
	}
	
	@Test
	@org.junit.jupiter.api.Order(7)
	public void findByCustomerIdTest() {
		List<Order> allOrders = orderService.findAll(Sort.by(Sort.Direction.ASC,"id"));
		Order first = allOrders.get(0);
		List<Order> ordersByCustomer = orderService.findByCustomerId(first.getCustomerId(), Sort.by(Sort.Direction.ASC,"date"));
		assertTrue(ordersByCustomer.size() <= allOrders.size());
	}
	
	
	
	@Test
	@org.junit.jupiter.api.Order(9)
	public void changeStatusTest() {
		List<Order> allOrders = orderService.findAll(Sort.by(Sort.Direction.ASC,"id"));
		Order first = allOrders.get(0);
		first.setStatusId(OrderStatusEnum.PAID.getId());
		orderService.updateById(first.getId(), first);
		
		Order firstTmp = orderService.findById(first.getId());
		assertTrue(firstTmp.getStatusId().equals(OrderStatusEnum.PAID.getId()));
		
	}
	
	 

}
