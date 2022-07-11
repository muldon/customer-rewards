package com.siono.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siono.aspect.Loggable;
import com.siono.model.MessageResponse;
import com.siono.model.SearchParams;
import com.siono.model.User;
import com.siono.service.MainService;
import com.siono.service.UserService;
import com.siono.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserServiceImpl extends MainService implements UserService{	     
	
	  
	private void verifyIfExists(Integer id) {
        this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: Id not found: "+id));
    }

	@Transactional(readOnly = true)
	public List<User> findAll(Sort by) {
		List<User> tos = userRepository.findAll(by);		
		return tos;		
	} 
	 
	
	@Transactional(readOnly = true)
	public void findByFilters(SearchParams searchParams, User wrapper) {
		Utils.checkSearchParams(searchParams);
		
		genericRepository.findUsersByFilters(searchParams,wrapper);				
		 
	}

	@Transactional(readOnly = true)
	public User findById(Integer id) {
		User v = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: Id not found: "+id));
		return v;				
	}

	 
	 
	   
	// -----------------------------------------------   Transacional Methods ------------------------------------------ //
	
	@Loggable  
	public void deleteById(Integer id)  {
		this.verifyIfExists(id);

        this.userRepository.deleteById(id);
		
	}

	public MessageResponse save(User to) {
		
		if(to.getId()==null) { //new user
			to.setDate(new Timestamp(System.currentTimeMillis()));
		}
		
		userRepository.save(to);		
		
        return createMessageResponse("User saved");
	}

	public User updateById(Integer id, User user) {
		user.setId(id);
		User saved = userRepository.save(user);
		return saved;
	}

	public MessageResponse changeStatus(User user) {
		userRepository.changeStatus(user.getId(), user.getStatusId());
		return createMessageResponse("Status updated");
	}
	 

}
