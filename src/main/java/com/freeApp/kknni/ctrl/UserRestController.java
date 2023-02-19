package com.freeApp.kknni.ctrl;

import com.freeApp.kknni.common.AjaxResult;
import com.freeApp.kknni.entity.Statistic;
import com.freeApp.kknni.entity.User;
import com.freeApp.kknni.service.StatisticService;
import com.freeApp.kknni.service.UserService;
import com.freeApp.kknni.util.DateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserRestController {

	private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

	private UserService userService;

	private StatisticService statisticService;

	@Autowired
	public UserRestController(UserService userService, StatisticService statisticService) {
		this.userService = userService;
		this.statisticService = statisticService;
	}

	/**
	 * 사용자 PUT
	 *
	 * @param user 사용자
	 * @return AjaxResult
	 * @author kknni
	 * @since 2023-02-05
	 */
	@PutMapping(value = "/put")
	public AjaxResult put(@ModelAttribute("user") User user) {
		Stack<Integer> stack = new Stack<>();

		AjaxResult ajaxResult;
		try {
			userService.save(user);
			ajaxResult = new AjaxResult(true, 0, "정상 처리입니다.");
		} catch (Exception e) {
			ajaxResult = new AjaxResult(false, 999, "DB 처리에 실패하였습니다.");
		}

		return ajaxResult;
	}

	/**
	 * 사용자 DELETE
	 *
	 * @param user 사용자
	 * @return AjaxResult
	 * @author kknni
	 * @since 2023-02-05
	 */
	@DeleteMapping(value = "/delete")
	public AjaxResult delete(@ModelAttribute("user") User user) {
		AjaxResult ajaxResult;
		try {
			userService.delete(user);
			ajaxResult = new AjaxResult(true, 0, "정상 처리입니다.");
		} catch (Exception e) {
			ajaxResult = new AjaxResult(false, 999, "DB 처리에 실패하였습니다.");
		}

		return ajaxResult;
	}

	/**
	 * 사용자 GET
	 *
	 * @param id 사용자 id
	 * @return AjaxResult
	 * @author kknni
	 * @since 2023-02-05
	 */
	@GetMapping(value = "/detail/{id}")
	public AjaxResult getDetail(@PathVariable("id") long id) {
		AjaxResult ajaxResult;
		Map<String, Object> data = new HashMap<>();
		try {
			User user = userService.findById(id);
			data.put("user", user);
			ajaxResult = new AjaxResult(true, 0, "정상 처리입니다.", data);
		} catch (Exception e) {
			ajaxResult = new AjaxResult(false, 999, "DB 처리에 실패하였습니다.");
		}

		return ajaxResult;
	}

	/**
	 * 사용자 List GET
	 *
	 * @param pageNo 페이지번호
	 * @return AjaxResult
	 * @author kknni
	 * @since 2023-02-05
	 */
	@GetMapping(value = "/list/{pageNo}")
	public AjaxResult getList(@PathVariable("pageNo") int pageNo) {
		AjaxResult ajaxResult;
		Map<String, Object> data = new HashMap<>();
		try {
			List<User> userList = userService.findAllByPageNo(pageNo);
			data.put("userList", userList);
			ajaxResult = new AjaxResult(true, 0, "정상 처리입니다.", data);
		} catch (Exception e) {
			ajaxResult = new AjaxResult(false, 999, "DB 처리에 실패하였습니다.");
		}

		return ajaxResult;
	}

	/**
	 * 수동 크롤링 기능
	 *
	 * @author kknni
	 * @since 2023-08-09
	 * @param yyyyMMdd 년월일
	 * @return
	 */
	@GetMapping(value = "/manual/get/{yyyyMMdd}")
	public AjaxResult getCrawlingDataAPI(@PathVariable("yyyyMMdd") String yyyyMMdd) {
		AjaxResult ajaxResult;

		try {
			List<User> userList = userService.findAll();
			for (int i = 0; i < userList.size(); i++) {
				getCrawlingData(userList.get(i), yyyyMMdd); // ID 이용하여 크롤링
			}
			ajaxResult = new AjaxResult(true, 0, "정상 처리입니다.");
		} catch (Exception e) {
			ajaxResult = new AjaxResult(false, 999, "DB 처리에 실패하였습니다.");
		}
		return ajaxResult;
	}

	private void getCrawlingData(User user, String yyyyMMdd) {
		Date today = null;
		try {
			today = DateUtil.getDate(yyyyMMdd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			int correctCount = 0;
			boolean isFuture = false;
			Document doc = Jsoup.connect("https://www.acmicpc.net/status?user_id=" + user.getName()).header("Content-Type", "application/json;charset=UTF-8").get();
			do {
				logger.info(user.getName() + "님 차례!");

				// 4. 요소 탐색
				// 4-1. Attribute 탐색
				System.out.println("[Attribute 탐색]");
				Elements fileblocks = doc.select("table#status-table tbody tr");

				logger.info("사이즈:" + fileblocks.size());
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
								isFuture = false;
							} else if (diff < 0) {
								isFuture = true;
								continue;
							} else{
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

					if (isFuture) {
						String nextPage = doc.select("#next_page").get(0).attr("href");
						doc = Jsoup.connect("https://www.acmicpc.net" + nextPage).header("Content-Type", "application/json;charset=UTF-8").get();
					}
				}
			} while (isFuture);
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
}
