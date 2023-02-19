package com.freeApp.kknni.service;

import com.freeApp.kknni.entity.User;

import java.util.List;

public interface UserService {

	List<User> findAll();

	User findById(Long id);

	void save(User entity);

	long count();

	void delete(User entity);

	void deleteById(Long id);

	void deleteAll();

	List<User> findAllByPageNo(int pageNo);

	String getNames();
}
