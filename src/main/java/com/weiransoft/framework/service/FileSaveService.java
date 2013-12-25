package com.weiransoft.framework.service;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.weiransoft.framework.constant.IndexConstant;

@Service
public class FileSaveService {
	private static final Logger logger = LoggerFactory.getLogger(FileSaveService.class);
	@Autowired
	ApplicationContext applicationContext;

	public String saveImageFile(MultipartFile imagefile) {
		String imageName = imagefile.getOriginalFilename();
		try {
			imageName = IndexConstant.INDEX_UPLOAD_FILE_PATH + imageName;
			logger.info("Save upload image: " + imageName);
			File dir = new File(this.calculateDestinationDirectory());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File multipartFile = new File(this.calculateDestinationPath(imagefile));
			if (!multipartFile.exists()) {
				imagefile.transferTo(multipartFile);
			}
			// imageName=multipartFile.getName();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageName;
	}

	private String calculateDestinationDirectory() {
		// TODO:add logical to create different sub path
		String result = IndexConstant.INDEX_UPLOAD_FILE_PATH;
		result += "/";

		return result;
	}

	private String calculateDestinationPath(MultipartFile file) {
		String result = this.calculateDestinationDirectory();
		result += "/";
		result += file.getOriginalFilename();
		return result;
	}

}
