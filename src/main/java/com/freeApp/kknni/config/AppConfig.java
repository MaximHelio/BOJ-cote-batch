package com.freeApp.kknni.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 웹 어플리케이션 설정용 파일
 *
 * @author kkni
 * @since 2023-02-14
 */
@Configuration
@EnableScheduling
@EnableJpaRepositories(value = "com.freeApp.kknni.repo")
@EntityScan(basePackages = {"com.freeApp.kknni.entity"})
public class AppConfig {
//
}
