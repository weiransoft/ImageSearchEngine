package com.weiransoft.framework.constant;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

public class VaildationUtils {
	private static String[] schemes = { "http", "https" };

	public static boolean VaildateUrl(String url) {
		UrlValidator urlValidator = new UrlValidator(schemes);
		if (StringUtils.isNotEmpty(url) && urlValidator.isValid(url))
			return true;
		return false;
	}

}
