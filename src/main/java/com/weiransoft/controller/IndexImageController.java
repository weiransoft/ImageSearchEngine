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
import org.springframework.web.multipart.MultipartFile;

import com.weiransoft.framework.constant.VaildationUtils;
import com.weiransoft.framework.service.FileSaveService;
import com.weiransoft.framework.service.ImageIndexService;

/**
 * Handles requests for the indexing Product's Image.
 */
@Controller
public class IndexImageController {

	private static final Logger logger = LoggerFactory.getLogger(IndexImageController.class);
	@Autowired
	private ImageIndexService imageIndexService;
	@Autowired
	FileSaveService fileSaveService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/indexProductImage", method = RequestMethod.POST,  produces = "application/xml; charset=UTF-8")
	public @ResponseBody
	String indexProductImage(@RequestParam(value = "productUrl") String productUrl, @RequestParam(value = "imageUrl") String imageUrl, @RequestParam MultipartFile imagefile,
			@RequestParam(value = "keywords") String keywords) {
		logger.info("requesting indexProduct");

		if (StringUtils.isEmpty(imageUrl) && (imagefile == null || imagefile.isEmpty())) {
			return "<error>Empty imagefile or invalid image URL </error>";
		}
		if (StringUtils.isNotEmpty(imageUrl) && !VaildationUtils.VaildateUrl(imageUrl)) {
			return "<error>Invaild imageUrl</error>";
		}

		if (!imagefile.isEmpty()) {
			imageUrl = fileSaveService.saveImageFile(imagefile);
		}
		if (VaildationUtils.VaildateUrl(productUrl)) {
			imageIndexService.indexImageFile(imageUrl, keywords, productUrl);
		} else {
			return "<error>Invaild ProductUrl</error>";
		}

		return "<msg>success</msg>";

	}

}
