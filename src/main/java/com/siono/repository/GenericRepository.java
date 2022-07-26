package com.siono.repository;

import com.siono.model.Order;
import com.siono.model.SearchParams;
import com.siono.model.User;

public interface GenericRepository { 
	
	void findOrdersByFilters(SearchParams searchParams, Order wrapper);

	 
}
