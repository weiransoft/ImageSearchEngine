package com.weiransoft.framework.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
@ContextConfiguration("classpath:app-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RestIndexImageTest {
	@Autowired
	RestTemplate rt;
	@Test
	public void testFileUpload() {

		String url = "http://localhost:8081/baotuSearch/indexProductImage";

		Resource resource = new ClassPathResource("image/psbe.jpg");

		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();
		mvm.add("productUrl", "http://www.importnew.com/7774.html");
		mvm.add("imageUrl", "");
		mvm.add("imagefile", resource);
		mvm.add("keywords", "test 测试");
//		RestTemplate rt=new RestTemplate();
		
		ResponseEntity<String> respEnt = rt.postForEntity(url, mvm, String.class);
		System.out.println("respEnt"+respEnt.getBody());
	}
}
