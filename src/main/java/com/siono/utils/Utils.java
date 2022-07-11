package com.siono.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.siono.model.Order;
import com.siono.model.Order.OrderStatusEnum;
import com.siono.model.SearchParams;
import com.siono.model.User;
import com.siono.model.User.UserRoleEnum;
import com.siono.model.User.UserStatusEnum;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Utils {
	static DateFormat sdfFull; 
	static DateFormat sdfMin; 
	static Calendar calendar;
	static DecimalFormat df;	
	
	@PostConstruct
	public void configure() {		
	 	sdfFull = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	 	sdfMin = new SimpleDateFormat("dd-MM-yyyy");
	 	
		sdfFull.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
		sdfMin.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
		
		df = new DecimalFormat("0.00");
	}		

    
	public static boolean isBlank(String input) {
		return StringUtils.isBlank(input);
	}

	 
	
	public synchronized static String getCurrentDate() {
		return sdfFull.format(Calendar.getInstance().getTime());
	}
	
	public synchronized static String formatDate(Timestamp date) {
		return sdfFull.format(date);
	}
	
	public synchronized static String formatDateMin(Timestamp date) {
		return sdfMin.format(date);
	}
	
	public synchronized static String formatDate(Date date) {
		return sdfMin.format(date);
	}
	
	public static void checkSearchParams(SearchParams searchParams) {
		if(searchParams.getRowOffset()==null) {
			searchParams.setRowOffset(0);
		}
 
		if(isBlank(searchParams.getSortField())) {
			searchParams.setSortField("id");
			searchParams.setSortOrder(-1);
		}
		
		if(searchParams.getNumRowsPerPage()==null || searchParams.getNumRowsPerPage() <= 0) {
			searchParams.setNumRowsPerPage(10);
		}
		
		
	}
	
	/**
	 * Prevent SQL injection
	 */
	public static String sanitize(String lowerCase) {
		lowerCase = lowerCase.replaceAll("insert", "");
		lowerCase = lowerCase.replaceAll("update", "");
		lowerCase = lowerCase.replaceAll("delete", "");
		return lowerCase;
	}
	
	public static double getRandomNumberInRange(double rangeMin, double rangeMax) {
		double random = ThreadLocalRandom.current().nextDouble(rangeMin, rangeMax);
		return random;		
	}
	
	public static LocalDate getRandomDateBetween(LocalDate startInclusive, LocalDate endExclusive) {
	    long initEpochDay = startInclusive.toEpochDay();
	    long endEpochDay = endExclusive.toEpochDay();
	    long randomDay = ThreadLocalRandom.current().nextLong(initEpochDay, endEpochDay);

	    return LocalDate.ofEpochDay(randomDay);
	}
	
	public static Instant getRandomDateBetween(Instant startInclusive, Instant endExclusive) {
	    long startSeconds = startInclusive.getEpochSecond();
	    long endSeconds = endExclusive.getEpochSecond();
	    long random = ThreadLocalRandom
	      .current()
	      .nextLong(startSeconds, endSeconds);

	    return Instant.ofEpochSecond(random);
	}
	
	
	public static User createTestUser() {
		User user = new User();		
		user.setName("Rick Silva");
		user.setStatusId(UserStatusEnum.ACTIVE.getId());
		user.setEmail("ricksilva@gmail.com");
		user.setRoleId(UserRoleEnum.CUSTOMER.getId());
		user.setPhone("+197412345");
		user.setDate(new Timestamp(System.currentTimeMillis()));	

		return user;
	}

	public static Order createTestOrder(Integer customerId) {
		Order order = new Order();
		order.setCustomerId(customerId);
		order.setStatusId(OrderStatusEnum.APPROVED.getId());
		order.setTotalValue(Utils.getRandomNumberInRange(50d, 200d));
		
		//generating an date between the last 12 months and now. 
		Instant threeMonthsAgo = Instant.now().minus(Duration.ofDays(12 * 30));  //last 12 months
		Instant now = Instant.now();
		Instant random = getRandomDateBetween(threeMonthsAgo, now);		
		order.setDate(Timestamp.from(random));
		return order;
	}
	
	public static List<Order> generateOrdersList(Integer customerId){
		List<Order> orders = new ArrayList<>();
		for(int i=0; i<30; i++) {  //let's create 30 transactions (orders) for testing 
			orders.add(createTestOrder(customerId));
		}		
		
		return orders;
	}
	
	   
}
