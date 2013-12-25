package com.weiransoft.framework.service;

import org.springframework.stereotype.Service;

import com.weiransoft.framework.service.client.rest.GrouponDataPagingUtil;

@Service(value = "grouponService")
public class GrouponService {

	private GrouponDataPagingUtil grouponDataByPage = new GrouponDataPagingUtil();

	public String getGrouponDailyListByPageNum(String Url, String version, Integer pageNum, String... citys) {
		return grouponDataByPage.getDailyGrouponsByPageNum(pageNum, Url, version, citys);
	}
}
