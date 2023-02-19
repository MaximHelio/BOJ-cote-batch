package com.freeApp.kknni.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "kknni_tb_STA")
public class Statistic {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column
	private Date createdDate;

	@Column
	private String title;

	@Column
	private int correctCount;

	@ManyToOne
	@JoinColumn(name = "user_id",
			referencedColumnName = "id")
	private User user;

	@Transient
	private String nameStr;
}
