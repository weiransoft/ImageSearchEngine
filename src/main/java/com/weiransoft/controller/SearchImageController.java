package com.weiransoft.controller;

import java.io.IOException;
import java.util.List;

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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.weiransoft.framework.constant.VaildationUtils;
import com.weiransoft.framework.service.FileSaveService;
import com.weiransoft.framework.service.ImageSearchService;
import com.weiransoft.framework.service.IndexImage;

/**
 * Handles requests for the indexing Product's Image.
 */
@Controller
public class SearchImageController {

	private static final Logger logger = LoggerFactory.getLogger(SearchImageController.class);
	@Autowired
	private ImageSearchService ImageSearchService;
	@Autowired
	FileSaveService fileSaveService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/searchProductImage", method = RequestMethod.POST, produces = "application/xml; charset=UTF-8")
	public @ResponseBody
	String searchProductImage(@RequestParam(value = "imageUrl") String imageUrl, @RequestParam MultipartFile imagefile, @RequestParam(value = "keywords") String keywords) {
		logger.info("requesting searchProductImage");

		if (StringUtils.isEmpty(imageUrl) && (imagefile == null || imagefile.isEmpty())) {
			return "<error>Empty imagefile or invalid image URL </error>";
		}
		if (StringUtils.isNotEmpty(imageUrl) && !VaildationUtils.VaildateUrl(imageUrl)) {
			return "<error>Invaild imageUrl</error>";
		}

		try {
			List<IndexImage> imageList = ImageSearchService.search(imagefile.getInputStream(), keywords);
			if (imageList != null && !imageList.isEmpty()) {
				XStream xstream = new XStream(new StaxDriver());
				return xstream.toXML(imageList);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "<msg>Not Found</msg>";

	}

}
