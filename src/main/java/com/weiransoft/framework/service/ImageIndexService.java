package com.weiransoft.framework.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.weiransoft.framework.constant.VaildationUtils;
import com.weiransoft.framework.lire.CombinedIndexer;

@Service
@Scope("prototype")
public class ImageIndexService {

	CombinedIndexer combinedIndexer;
	private static final Logger logger = LoggerFactory.getLogger(ImageIndexService.class);

	public void indexImageFile(String imageUrl, String keyWords, String productUrl) {

		try {
			if (combinedIndexer == null) {
				combinedIndexer = new CombinedIndexer();
			}
			if (VaildationUtils.VaildateUrl(imageUrl)) {
				URL url = new URL(imageUrl);
				// 实列一个URLconnection对象，用来读取和写入此 URL 引用的资源
				URLConnection con = url.openConnection();
				// 获取一个输入流
				InputStream is = con.getInputStream();
				combinedIndexer.index(imageUrl, is, keyWords, productUrl);
			} else {
				FileInputStream stream = new FileInputStream(imageUrl);
				combinedIndexer.index(imageUrl, stream, keyWords, productUrl);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			logger.warn("NOT found image: " + imageUrl);
		}

	}

	public void indexImageList(List<IndexImage> imageList) {
		for (IndexImage image : imageList) {
			indexImageFile(image.getImageUrl(), image.getKeyWords(), image.getProductUrl());
		}
		// combinedIndexer.commit();
	}
}
