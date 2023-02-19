package com.freeApp.kknni.repo;

import com.freeApp.kknni.entity.Statistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticRepository extends CrudRepository<Statistic, Long> {
	Page<Statistic> findAll(Pageable pageable);
	List<Statistic> findAll(Sort sort);
}
