package com.freeApp.kknni.repo;

import com.freeApp.kknni.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	Page<User> findAll(Pageable pageable);

//	@Query(nativeQuery = true, value = "SELECT\n\tLISTAGG(dtu.NAME, ',') WITHIN GROUP (\n\tORDER BY id) AS NAME\nFROM\n\tsunygo_TB_USR dtu")
	@Query(nativeQuery = true, value = "SELECT\n\tgroup_concat(dtu.NAME ORDER BY  id DESC SEPARATOR ',') AS NAME\nFROM\n\tkknni_tb_USR dtu")
	String getNames();
}
