package com.freeApp.kknni.service;

import com.freeApp.kknni.common.Constant;
import com.freeApp.kknni.entity.User;
import com.freeApp.kknni.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public void save(User entity) {
		userRepository.save(entity);
	}

	@Override
	public long count() {
		return userRepository.count();
	}

	@Transactional
	@Override
	public void delete(User entity) {
		userRepository.delete(entity);
	}

	@Transactional
	@Override
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	}

	@Override
	public List<User> findAllByPageNo(int pageNo) {
		PageRequest pageRequest = PageRequest.of(pageNo, Constant.COMMON_PAGING_SIZE, Sort.by("id").descending());
		return userRepository.findAll(pageRequest).getContent();
	}

	@Override
	public String getNames() {
		return userRepository.getNames();
	}
}
