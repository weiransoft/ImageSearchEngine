package com.weiransoft.framework.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("classpath:app-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchServiceTest {
	@Resource
	ImageSearchService ImageSearchService;

	@Test
	public void test() {
		 try {
//			URL url = new URL(
//					"http://img10.360buyimg.com/n8/g15/M0A/02/02/rBEhWlH3enMIAAAAAAQSQrQ-6bIAABibwEvdt4ABBJa451.jpg");
//			//实列一个URLconnection对象，用来读取和写入此 URL 引用的资源
//			URLConnection con = url.openConnection();
//			//获取一个输入流
//			InputStream is = con.getInputStream();
			 
			List<IndexImage> imageList= ImageSearchService.search(new FileInputStream("C:\\Users\\wwang16\\Desktop\\testjpg.png"), "秋水");
			System.out.println("images count:"+imageList.size());
			for(IndexImage image:imageList){
				System.out.println("images  name: "+image.getImageUrl()+" simility: "+image.getScore());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
