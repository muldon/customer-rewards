package com.siono.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.siono.model.MessageResponse;
import com.siono.model.User;
import com.siono.model.SearchParams;

public interface UserService {

	public List<User> findAll(Sort by);

	public void findByFilters(SearchParams searchParams, User wrapper);

	public User findById(Integer id);
	
	public void deleteById(Integer id);

	public MessageResponse save(User to);

	public User updateById(Integer id, User order);

	public MessageResponse changeStatus(User order);

}
