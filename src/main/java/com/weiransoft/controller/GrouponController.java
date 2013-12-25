package com.weiransoft.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weiransoft.framework.constant.VaildationUtils;
import com.weiransoft.framework.service.GrouponService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class GrouponController {

	private static final Logger logger = LoggerFactory.getLogger(GrouponController.class);
	@Autowired
	private GrouponService grouponService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/getGrouponDataByPage", method = RequestMethod.POST,produces = "application/xml; charset=UTF-8")
	public @ResponseBody
	String getGrouponDataByPage(@RequestParam(value = "pageNum") String pageNumStr, @RequestParam(value = "city") String city, @RequestParam(value = "url") String url,
			@RequestParam(value = "version") String version) {
		logger.info("requesting home");

		Integer pageNum = 0;
		if (StringUtils.isNotEmpty(pageNumStr)) {
			try {
				pageNum = Integer.parseInt(pageNumStr);
			} catch (NumberFormatException e) {

			}
		}
		// CHECH url
		if (VaildationUtils.VaildateUrl(url)) {
			logger.warn("url:" + url + " is valid");
		} else {
			return "<error>Invaild URL</error>";
		}
		if (StringUtils.isEmpty(version)) {
			version = "v1";
		}
		if (StringUtils.isEmpty(city)) {
			return "<error>Invaild City</error>";
		} else {
			String[] citys = city.split("&");
			return grouponService.getGrouponDailyListByPageNum(url, version, pageNum, citys);
		}
	}

}
