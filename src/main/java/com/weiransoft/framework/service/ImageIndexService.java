package com.weiransoft.framework.service;

import com.weiransoft.framework.constant.VaildationUtils;
import com.weiransoft.framework.lire.CombinedIndexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Service
@Scope("prototype")
public class ImageIndexService {

	CombinedIndexer combinedIndexer;
	private static final Logger logger = LoggerFactory.getLogger(ImageIndexService.class);
    @Autowired
    FileSaveService fileSaveService;
	public void indexImageFile(String imageUrl, String keyWords, String productUrl) {

		try {
			if (combinedIndexer == null) {
				combinedIndexer = new CombinedIndexer();
			}
			if (VaildationUtils.VaildateUrl(imageUrl)) {
				InputStream is = fileSaveService.getInputStreamFromWeb(imageUrl);
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
