package com.weiransoft.framework.service.client.rest;

import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.weiransoft.framework.utils.XMLDataPagingUtils;

/**
 * The Class GrouponClient.
 */
public class GrouponClient {

	/** The default version. */
	private static final String DEFAULT_VERSION = "v1";

	/**
	 * Gets the groupon data.
	 * 
	 * @param url
	 *            the url
	 * @param version
	 *            the version
	 * @param citys
	 *            the citys
	 * @return the groupon data
	 */
	public String getGrouponData(String url, String version, String... citys) {
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		sb.append("?version=").append(version == null ? DEFAULT_VERSION : version);
		for (String city : citys) {
			sb.append("&city=" + city);
		}
		Client client = Client.create();
		WebResource webResource = client.resource(sb.toString());
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String result = response.getEntity(String.class);

		return result;
	}

	/**
	 * Gets the node count.
	 * 
	 * @param xmlContent
	 *            the xml content
	 * @param xpathExpression
	 *            the xpath expression
	 * @return the node count
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 * @throws FactoryConfigurationError
	 *             the factory configuration error
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the sAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int getNodeCount(String xmlContent, String xpathExpression) throws XPathExpressionException, FactoryConfigurationError, ParserConfigurationException, SAXException, IOException {
		if (StringUtils.isEmpty(xmlContent) || StringUtils.isEmpty(xpathExpression)) {
			return 0;
		}
		Document doc = XMLDataPagingUtils.getDocument(xmlContent);
		Object XmlResult = XMLDataPagingUtils.queryXMLResult(doc, xpathExpression);
		NodeList nodes = (NodeList) XmlResult;
		return nodes == null ? 0 : nodes.getLength();
	}
}
