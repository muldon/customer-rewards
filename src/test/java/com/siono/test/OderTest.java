package com.siono.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import com.siono.model.Order;
import com.siono.model.Order.OrderStatusEnum;
import com.siono.model.User;
import com.siono.model.User.UserRoleEnum;
import com.siono.model.User.UserStatusEnum;
import com.siono.service.OrderService;
import com.siono.service.UserService;
import com.siono.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
//@Transactional 
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OderTest {
	public static Integer orderId;
	public static Integer userId;
	 
	@Autowired
	OrderService orderService;

	@Autowired
	UserService userService;
 
	@Test
	@org.junit.jupiter.api.Order(1)
	public void createTest() {
		User user = Utils.createTestUser();
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
		assertTrue(allOrders.size()>0); //the @org.junit.jupiter.api.Order(2) annotation guarantees that the previous method (insert) will be invoked before		
	}
	
	@Test
	@org.junit.jupiter.api.Order(3)
	public void updateTest() {
		Order order = orderService.findById(orderId); 
		assertNotNull(order);
	}
	
	@Test
	@org.junit.jupiter.api.Order(4)
	public void deleteTest() {
		orderService.deleteById(orderId);
		
		Exception exception = assertThrows(RuntimeException.class, () -> {
			Order order = orderService.findById(orderId); 
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
	@org.junit.jupiter.api.Order(5)
	public void findByFiltersTest() {
		
		
		
		
	}
	
	

}
