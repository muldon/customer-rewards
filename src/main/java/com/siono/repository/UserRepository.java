package com.siono.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.siono.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	User findByEmail(String email);

	User findByPhone(String phone);
	 

	@Modifying
	@Query("update User u set u.email = ?1 where u.id = ?2")
	void updateEmail(String email, Integer id);
	  
	@Modifying
	@Query("update User u set u.statusId = ?2 where u.id = ?1")
	void changeStatus(Integer id, Integer statusId);

	List<User> findByRoleIdAndStatusId(Integer roleId, Integer statusId, Sort by);

	 
}
