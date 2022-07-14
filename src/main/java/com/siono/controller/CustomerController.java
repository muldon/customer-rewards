package com.siono.controller;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.siono.model.CustomerRewards;
import com.siono.model.CustomerRewards.OperationEnum;
import com.siono.model.Statement;
import com.siono.model.StatementPeriod;
import com.siono.model.User;
import com.siono.model.User.UserRoleEnum;
import com.siono.model.User.UserStatusEnum;
import com.siono.service.OrderService;
import com.siono.service.RewardsService;
import com.siono.service.UserService;
import com.siono.utils.Utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/customer", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

	@Autowired
	OrderService orderService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RewardsService rewardsService;
	
	
	@Operation(summary = "Shows the names of active customers")	
	@GetMapping("/list")
	@ResponseStatus(HttpStatus.OK)
	public List<String> getCustomerNames() {
		log.info("Retrieving customer names");
		List<User> activeUsers = userService.findByRoleIdAndStatusId(UserRoleEnum.CUSTOMER.getId(),UserStatusEnum.ACTIVE.getId(),Sort.by(Sort.Direction.ASC,"name"));
		List<String> customerNames = activeUsers.stream().map(u -> u.getName()).collect(Collectors.toCollection(ArrayList::new));
		return customerNames;
	}
	

	@Operation(summary = "Get the statement of the points for a user by its id and the number of previous days")	
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Found the user", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			  @ApiResponse(responseCode = "404", description = "Invalid id supplied", content = @Content), @ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	@GetMapping("/statement/{customerId}/{lastNDays}")
	@ResponseStatus(HttpStatus.OK)
	public Statement getStatement(@PathVariable(value = "customerId") Integer customerId, @PathVariable(value = "lastNDays") Integer lastNDays) {
		Statement responseWrapper = new Statement();
		Optional<User> user = userService.findById(customerId);
		if(!user.isPresent()) {
			log.error("User not found: "+customerId);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		log.info("Genereting statement for customer "+user.get().getName()+ " for the last "+lastNDays+ " days");
		Timestamp dateFrom = Timestamp.from(Instant.now().minus(Duration.ofDays(lastNDays))); //last n days 
		List<CustomerRewards> rewardsList = rewardsService.findByCustomerIdAndDateAfter(customerId,dateFrom,Sort.by(Sort.Direction.ASC, "date"));
		buildStatement(rewardsList,responseWrapper,user.get());		 
		return responseWrapper;
	}


	private void buildStatement(List<CustomerRewards> rewardsList, Statement responseWrapper,	User user) {
		List<StatementPeriod> statementPeriods = new ArrayList<>();
		responseWrapper.setCustomerName(user.getName());
		Set<Integer> months = rewardsList.stream().map(r -> Utils.getMonthFromDate(r.getDate())).collect(Collectors.toCollection( LinkedHashSet::new ) );
		Integer accumulatedBalance = 0;
		for(Integer month: months) {
			List<CustomerRewards> transactionsForMonth = rewardsList.stream().filter(r -> Utils.getMonthFromDate(r.getDate()).equals(month)).collect(Collectors.toCollection( ArrayList::new ) );
			Integer totalReceivedMonth = transactionsForMonth.stream().filter(r -> r.getOperationId().equals(OperationEnum.INPUT.getId())).map(r -> r.getNumPoints()).reduce(0, Integer::sum);
			Integer totalSpentMonth = transactionsForMonth.stream().filter(r -> r.getOperationId().equals(OperationEnum.OUTPUT.getId())).map(r -> r.getNumPoints()).reduce(0, Integer::sum);
			Integer balanceMonth = totalReceivedMonth - totalSpentMonth;
			accumulatedBalance+= balanceMonth;
			StatementPeriod sp = new StatementPeriod(month, balanceMonth,accumulatedBalance);
			statementPeriods.add(sp);
		} 
		responseWrapper.setTotalPoints(accumulatedBalance);
		responseWrapper.setStatementPeriods(statementPeriods);
	}

  
}
