package com.weiransoft.framework.service.client.rest;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.NodeList;

import com.weiransoft.framework.utils.XMLDataPagingUtils;

/**
 * The Class GrouponDataByPage.
 */
public class GrouponDataPagingUtil {
	private static final String EMPTY_STRING = "";
	/** The default page size. */
	private int pageSize = 15;
	GrouponClient grouponClient = new GrouponClient();

	/**
	 * Instantiates a new groupon data by page.
	 */
	public GrouponDataPagingUtil() {
		super();

	}

	/**
	 * Gets the daily groupon list by get.
	 * 
	 * @param url
	 *            the url
	 * @param version
	 *            the version
	 * @param citys
	 *            the citys
	 * @return the daily groupon list by get
	 */
	public String getDailyGrouponList(String url, String version, String... citys) {
		if (StringUtils.isEmpty(url)) {
			return EMPTY_STRING;
		}
		try {
			String xmlContent = grouponClient.getGrouponData(url, version, citys);
			NodeList nodes = XMLDataPagingUtils.getXMLNodes(xmlContent, "urlset//url");
			if (nodes != null)
				return nodes.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_STRING;
	}

	/**
	 * Gets the page count.
	 * 
	 * @param nodes
	 *            the nodes
	 * @return the page count
	 */
	public int getPageCount(NodeList nodes) {
		return XMLDataPagingUtils.getPageCount(nodes, this.pageSize);
	}

	/**
	 * Gets the page count.
	 * 
	 * @param url
	 *            the url
	 * @param version
	 *            the version
	 * @param citys
	 *            the citys
	 * @return the page count
	 */
	public int getPageCount(String url, String version, String... citys) {
		return getPageCount(url, this.pageSize, version, citys);
	}

	/**
	 * Gets the page count.
	 * 
	 * @param url
	 *            the url
	 * @param pageSize
	 *            the page size
	 * @param version
	 *            the version
	 * @param citys
	 *            the citys
	 * @return the page count
	 */
	public int getPageCount(String url, int pageSize, String version, String... citys) {
		String xmlContent = grouponClient.getGrouponData(url, version, citys);
		NodeList nodes = XMLDataPagingUtils.getXMLNodes(xmlContent, "urlset//url");
		return XMLDataPagingUtils.getPageCount(nodes, pageSize);
	}

	/**
	 * Gets the daily groupon list page.
	 * 
	 * @param pageSize
	 *            the page size
	 * @param pageNum
	 *            the page num
	 * @param url
	 *            the url
	 * @param version
	 *            the version
	 * @param citys
	 *            the citys
	 * @return the daily groupon list page
	 */
	public String getDailyGrouponsByPageNum(int pageSize, int pageNum, String url, String version, String... citys) {
		String xmlContent = grouponClient.getGrouponData(url, version, citys);
		return XMLDataPagingUtils.getXMLDataByPageNum(pageSize, "urlSet", "url", pageNum, xmlContent);
	}

	/**
	 * Gets the daily groupons by page num.
	 * 
	 * @param pageNum
	 *            the page num
	 * @param url
	 *            the url
	 * @param version
	 *            the version
	 * @param citys
	 *            the citys
	 * @return the daily groupons by page num
	 */
	public String getDailyGrouponsByPageNum(int pageNum, String url, String version, String... citys) {
		String xmlContent = grouponClient.getGrouponData(url, version, citys);
		return XMLDataPagingUtils.getXMLDataByPageNum(pageSize, "urlSet", "url", pageNum, xmlContent);
	}

	/**
	 * Gets the daily groupons by page num.
	 * 
	 * @param pageNum
	 *            the page num
	 * @param xmlContent
	 *            the xml content
	 * @return the daily groupons by page num
	 */
	public String getDailyGrouponsByPageNum(int pageNum, String setName, String DataElementName, String xmlContent) {
		return XMLDataPagingUtils.getXMLDataByPageNum(pageSize, setName, DataElementName, pageNum, xmlContent);
	}

}