package com.freeApp.kknni.job;

import com.freeApp.kknni.common.DTDJob;
import com.freeApp.kknni.entity.Statistic;
import com.freeApp.kknni.entity.User;
import com.freeApp.kknni.service.StatisticService;
import com.freeApp.kknni.service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 사용자 랭킹을 집계해옵니다.
 *
 * @author kknni
 * @jobId Job0001
 * @description 일배치 :: 사용자 랭킹 집계
 * @since 2023-02-16
 */
@Component
public class Job0001 extends DTDJob {

	private static final Logger logger = LoggerFactory.getLogger(Job0001.class);

	@Autowired
	private UserService userService;

	@Autowired
	private StatisticService statisticService;

	private List<User> userList;

	@PostConstruct
	@Override
	public void setJob() {
		setJobId("0001");
	}

	/**
	 * 23시 30분마다 일배치 실행 - 백준 풀었는지 안풀었는지 체크하여 DB 적재
	 *
	 * @author kknni
	 * @since 2023-02-14
	 */
	@Scheduled(cron = "0 10 00 ? * MON-SUN") // 왼쪽부터 초/분/시/일/월/요일/[선택]연도 , 월요일~일요일 00:10 에 체크
//	@Scheduled(cron = "0 0/1 * * * *") // 왼쪽부터 초/분/시/일/월/요일/[선택]연도 , 1분마다 체크
	@Override
	public void makeJob() {
		extract();

		userList = userService.findAll();
		for (int i = 0; i < userList.size(); i++) {
			getCrawlingData(userList.get(i)); // ID 이용하여 크롤링
		}

		// transform();
		// load();
	}

	private void getCrawlingData(User user) {
		Date today = new Date();
		try {
			Document doc = Jsoup.connect("https://www.acmicpc.net/status?user_id=" + user.getName())
								.header("Content-Type", "application/json;charset=UTF-8")
								.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
								.get();
			logger.info(user.getName() + "님 차례!");

			// 4. 요소 탐색
			// 4-1. Attribute 탐색
			System.out.println("[Attribute 탐색]");
			Elements fileblocks = doc.select("table#status-table tbody tr");

			logger.info("사이즈:" + fileblocks.size());
			int correctCount = 0;
			if (fileblocks.size() == 0) {
				logger.info("데이터가 없는 사용자!");
			} else {
				for (int i = 0; i < fileblocks.size(); i++) {
					Elements timestamps = fileblocks.get(i).select("a[data-timestamp]");
					Date date = null;
					if (timestamps.size() > 0) {
						Timestamp ts = new Timestamp(Long.parseLong(timestamps.get(0).attr("data-timestamp")) * 1000);
						date = new Date(ts.getTime());
						System.out.println(date);
					}
					if (date != null) {
						// Get msec from each, and subtract.
						long diff = today.getTime() - date.getTime();
						diff = (diff / (1000 * 60 * 60 * 24));
						System.out.println(diff + "일 차이!");

						if (diff == 0) {
							Elements correctElements = fileblocks.get(i).select(".result-ac");
							if (correctElements.size() > 0) {
								correctCount++;
							}
						} else {
							break;
						}
					} else {
						break;
					}

					if (i == fileblocks.size() - 1) {
						// First page Last Elements ...
						// Next crawling..?!
					}
				}
			}
			System.out.println(correctCount + "개 맞음!");

			Statistic statistic = new Statistic();
			statistic.setCreatedDate(today);
			statistic.setTitle("백준 알고리즘!");
			statistic.setUser(user);
			statistic.setCorrectCount(correctCount);

			statisticService.save(statistic);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void extract() {
		super.extract();
	}

	@Override
	public void transform() {
		super.transform();
	}

	@Override
	public void load() {
		super.load();
	}
}
