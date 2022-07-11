package com.siono.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.siono.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

	Optional<List<Order>> findByCustomerId(Integer userId, Sort sort);

	@Modifying
	@Query("update Order o set o.statusId = ?2 where o.id = ?1")
	void changeStatus(Integer id, Integer statusId);
	
	void deleteByCustomerId(Integer userId);

	@Query(value = "from Order o where o.customerId=?1 "
			+ " and o.statusId >= ?2"
			+ " and o.date BETWEEN ?3 AND ?4"
			+ " order by o.id desc")
	List<Order> findApprovedOrdersByPeriod(Integer customerId,Integer statusIdApproved, Timestamp dateFrom, Timestamp dateTo);

	 
	
	
	  
}
