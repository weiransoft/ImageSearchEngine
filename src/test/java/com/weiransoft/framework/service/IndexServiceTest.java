package com.weiransoft.framework.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("classpath:app-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class IndexServiceTest {
	@Resource
	ImageIndexService imageIndexService;

	@Test
	public void test() {

		try {

			List<IndexImage> imageList = new ArrayList<IndexImage>();
			getBookUrlByUrlAndPageNum(
					"http://list.jd.com/1315-1343-9719-0-0-0-0-0-0-0-1-1-", 1,
					100, imageList);
			System.out.println("images count:" + imageList.size());
			imageIndexService.indexImageList(imageList);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// http://list.jd.com/1315-1343-9719-0-0-0-0-0-0-0-1-1-1-1-1-72-4137-0.html
	//http://list.jd.com/1315-1343-9719-0-0-0-0-0-0-0-1-1-1-1-2-2815-2870-0.html
//	  http://list.jd.com/1315-1343-9719-0-0-0-0-0-0-0-1-1-2-1-2-2815-2870-0.html
	// http://list.jd.com/1315-1343-9719-0-0-0-0-0-0-0-1-1-//2-1-1-72-4137-0.html
	public static void getBookUrlFromPage(String url, List<IndexImage> list)
			throws Exception {
		// timeout 0:不断请求 默认:2000毫秒超时
		System.out.println("URL:" + url);
		try {
			Document document = Jsoup.connect(url).timeout(2000).get();
			Elements node = document.getElementsByClass("p-img");// .select(".list-h");
			// Elements elements = node.get(1).getElementsByTag("li");
			for (Element item : node) {

				String urlimage = item.child(0).child(0).attr("src");
				if (StringUtils.isNotEmpty(urlimage)) {
					if ("220".equals(item.child(0).child(0).attr("width"))) {
						IndexImage iamge = new IndexImage();
						iamge.setProductUrl(item.child(0).attr("href"));
						iamge.setImageUrl(urlimage);
						iamge.setKeyWords(item.child(0).child(0).attr("alt"));
						list.add(iamge);
					}else{
						System.out.println("too small~");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("URL:" + url + " faild!");
		}
	}

	public static void getBookUrlByUrlAndPageNum(String url, Integer startPage,
			Integer endPage, List<IndexImage> list) {
		try {
			for (int i = startPage; i <= endPage; i++) {
				String urlStr = url + i + "-1-2-2815-2870-0.html";
				getBookUrlFromPage(urlStr, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
