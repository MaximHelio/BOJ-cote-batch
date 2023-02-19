package com.freeApp.kknni.ctrl;

import com.freeApp.kknni.entity.Statistic;
import com.freeApp.kknni.service.StatisticService;
import com.freeApp.kknni.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * user rank page
 *
 * @author kknni
 * @since 2023-02-14
 */
@Controller
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	private final StatisticService statisticService;

	@Autowired
	public UserController(UserService userService, StatisticService statisticService) {
		this.userService = userService;
		this.statisticService = statisticService;
	}

	/**
	 * 사용자 RANK
	 *
	 * @return ModelAndView
	 * @author kknni
	 * @since 2023-02-15
	 */
	@GetMapping(value = "/rank")
	public ModelAndView rank() {
		ModelAndView mav = new ModelAndView("user/rank");

		List<Statistic> statisticList = statisticService.findAll();
		String names = userService.getNames();

		mav.addObject("statisticList", statisticList);
		mav.addObject("names", names);

		return mav;
	}
}
