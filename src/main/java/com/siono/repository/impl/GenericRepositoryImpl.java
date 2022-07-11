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
import com.siono.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class GenericRepositoryImpl implements GenericRepository {
	@PersistenceContext
	private EntityManager em;
 

	@Override
	public void findOrdersByFilters(SearchParams searchParams, Order wrapper) {
		String sql= "select o from Order o ";			
		StringBuilder sb = new StringBuilder(sql);
		sb.append(" where 1=1"); //not nice ok, but functional :). Since this just a demo app, no big deal... 
				
		if(searchParams.getIdStatus()!=null) {
			sb.append(" and o.statusId = :idStatus");
		}
		if(searchParams.getIdCustomer()!=null) {
			sb.append(" and o.customerId = :idCustomer");
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
		
		wrapper.setTotal(q.getResultList().size());
		q.setFirstResult(searchParams.getRowOffset());
		q.setMaxResults(searchParams.getNumRowsPerPage());
		
		List<Order> rows = q.getResultList();		 
		wrapper.setData(rows);		
		
		
	}

	@Override
	public void findUsersByFilters(SearchParams searchParams, User wrapper) {
		String sql= "select u from User u ";			
		StringBuilder sb = new StringBuilder(sql);
		sb.append(" where 1=1"); //again, not nice ok, but functional :). Since this just a demo app, no big deal... 
		
		
		if(searchParams.getIdStatus()!=null) {
			sb.append(" and u.statusId = :idStatus");
		}

		if(!Utils.isBlank(searchParams.getSearchText())) {
			String searchText = Utils.sanitize(searchParams.getSearchText().toLowerCase());
			sb.append(" and (lower(u.name) like '%"+searchText+"%' or lower(u.email) like '%"+searchText+"%' or lower(u.phone)) ");					
		} 
		 		
		sb.append(" order by ue."+searchParams.getSortField());
		if(searchParams.getSortOrder().equals(-1)) {
			sb.append(" desc");
		}		 
		
		Query q = em.createQuery(sql, User.class);
		if(searchParams.getIdStatus()!=null) {
			q.setParameter("idStatus", searchParams.getIdStatus());
		}
		
		wrapper.setTotal(q.getResultList().size());
		q.setFirstResult(searchParams.getRowOffset());
		q.setMaxResults(searchParams.getNumRowsPerPage());
		
		List<User> rows = q.getResultList();		 
		wrapper.setData(rows);		
		
	}



 

	 	
}
