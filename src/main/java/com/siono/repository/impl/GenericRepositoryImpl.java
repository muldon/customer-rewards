package com.siono.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.siono.model.Order;
import com.siono.model.SearchParams;
import com.siono.model.User;
import com.siono.repository.GenericRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class GenericRepositoryImpl implements GenericRepository {
	@PersistenceContext
	private EntityManager em;
 

	@Override
	public void findOrdersByFilters(SearchParams searchParams, Order wrapper) {
		log.debug("findOrdersByFilters with params: "+searchParams);
		String sql= "select o from Order o ";			
		StringBuilder sb = new StringBuilder(sql);
		sb.append(" where 1=1"); //not nice ok, but functional :). Since this just a demo app, no big deal... 
				
		if(searchParams.getIdStatus()!=null) {
			sb.append(" and o.statusId = :idStatus");
		}
		if(searchParams.getIdCustomer()!=null) {
			sb.append(" and o.customerId = :idCustomer");
		}
		if(searchParams.getInitDate()!=null) {
			sb.append(" and o.date > to_timestamp(:initDate, 'YYYY-MM-DD HH24:MI:SS')");
		}
		
		if(searchParams.getEndDate()!=null) {
			sb.append(" and o.date < to_timestamp(:endDate, 'YYYY-MM-DD HH24:MI:SS')");
		}
		 		
		sb.append(" order by o."+searchParams.getSortField());
		if(searchParams.getSortOrder().equals(-1)) {
			sb.append(" desc");
		}		 
		
		Query q = em.createQuery(sb.toString(), Order.class);
		if(searchParams.getIdStatus()!=null) {
			q.setParameter("idStatus", searchParams.getIdStatus());
		}
		if(searchParams.getIdCustomer()!=null) {
			q.setParameter("idCustomer", searchParams.getIdCustomer());
		}
		if(searchParams.getInitDate()!=null) {
			q.setParameter("initDate", searchParams.getInitDate());
		}
		
		if(searchParams.getEndDate()!=null) {
			q.setParameter("endDate", searchParams.getInitDate());
		}
		
		
		wrapper.setTotal(q.getResultList().size());
		q.setFirstResult(searchParams.getRowOffset());
		q.setMaxResults(searchParams.getNumRowsPerPage());
		
		List<Order> rows = q.getResultList();		 
		wrapper.setData(rows);		
		
		
	}
  
	 	
}
