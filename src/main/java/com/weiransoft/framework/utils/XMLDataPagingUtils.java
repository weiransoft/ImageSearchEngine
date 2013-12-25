package com.weiransoft.framework.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

/**
 * The Class XMLDataPaginger.
 */
public class XMLDataPagingUtils {
	
	/** The Constant DEFAULT_ENCDING_UTF_8. */
	private static final String DEFAULT_ENCDING_UTF_8 = "UTF-8";
	
	/** The Constant default_pageSize. */
	private static final int default_pageSize = 20;

	/**
	 * Gets the document.
	 * 
	 * @param xmlContent
	 *            the xml content
	 * @return the document
	 */
	public static Document getDocument(String xmlContent) {
		Document doc = null;
		if (StringUtils.isNotEmpty(xmlContent)) {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(DEFAULT_ENCDING_UTF_8));
				Reader reader = new InputStreamReader(inputStream, DEFAULT_ENCDING_UTF_8);
				InputSource is = new InputSource(reader);
				is.setEncoding(DEFAULT_ENCDING_UTF_8);
				doc = (Document) docBuilder.parse(is);
				doc.getDocumentElement().normalize();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return doc;
	}

	/**
	 * Gets the xML nodes.
	 * 
	 * @param xmlContent
	 *            the xml content
	 * @param xpathExpression
	 *            the xpath expression
	 * @return the xML nodes
	 */
	public static NodeList getXMLNodes(String xmlContent, String xpathExpression) {
		if (StringUtils.isEmpty(xmlContent) || StringUtils.isEmpty(xpathExpression)) {
			return null;
		}
		NodeList nodes = null;
		Document doc = getDocument(xmlContent);
		nodes = queryXMLResult(doc, xpathExpression);
		return nodes;
	}

	/**
	 * Gets the page count.
	 * 
	 * @param nodes
	 *            the nodes
	 * @param pageSize
	 *            the page size
	 * @return the page count
	 */
	public static int getPageCount(NodeList nodes, int pageSize) {
		int pageCount = 0;
		if (nodes != null) {
			int count = nodes.getLength();
			pageSize = pageSize <= 0 ? default_pageSize : pageSize;
			pageCount = count / pageSize;
			if (count % pageSize > 0) {
				pageCount = pageCount + 1;
			}
		}
		return pageCount;
	}

	/**
	 * Gets the daily groupons by page num.
	 *
	 * @param pageSize the page size
	 * @param setName the set name
	 * @param DataElementName the data element name
	 * @param pageNum the page num
	 * @param xmlContent the xml content
	 * @return the daily groupons by page num
	 */
	public static String getXMLDataByPageNum(int pageSize, String setName, String DataElementName, int pageNum, String xmlContent) {
		if (StringUtils.isNotEmpty(xmlContent) && StringUtils.isNotEmpty(setName) && StringUtils.isNotEmpty(DataElementName)) {
			Document doc = getDocument(xmlContent);
			NodeList nodes = queryXMLResult(doc, setName + "//" + DataElementName);

			int pageCount = getPageCount(nodes, pageSize);
			if (pageCount < pageNum) {
				return "";
			}
			if (nodes != null) {
				int endNodeIndex = pageSize * pageNum + 1;// 15*2+1=31
				int startNodeIndex = endNodeIndex - pageSize;// 16
				startNodeIndex = startNodeIndex < 0 ? 1 : startNodeIndex;
				String xpathExpression = "/" + setName + "/" + DataElementName + "[position()>=" + startNodeIndex + " and position()<" + endNodeIndex + "]";
				// System.out.println(xpathExpression);
				nodes = queryXMLResult(doc, xpathExpression);
				if (nodes != null) {
					// System.out.println(nodes.getLength());
					try {
						Document newXmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
						Element root = newXmlDocument.createElement(setName);
						newXmlDocument.appendChild(root);
						for (int i = 0; i < nodes.getLength(); i++) {
							Node node = nodes.item(i);
							Node copyNode = newXmlDocument.importNode(node, true);
							root.appendChild(copyNode);
						}
						return exportXMLDocumentToString(newXmlDocument);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
		return "";
	}

	/**
	 * Evaluate xpath.
	 * 
	 * @param doc
	 *            the doc
	 * @param expr
	 *            the expr
	 * @return the object
	 */
	public static NodeList evaluateXpath(Document doc, XPathExpression expr) {
		NodeList XmlResult = null;
		try {
			XmlResult = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return XmlResult;
	}

	/**
	 * Query xml result.
	 * 
	 * @param doc
	 *            the doc
	 * @param xpathExpression
	 *            the xpath expression
	 * @return the object
	 */
	public static NodeList queryXMLResult(Document doc, String xpathExpression) {
		// Document doc = getDocument(result);
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		NodeList XmlResult = null;
		try {
			XPathExpression expr = xpath.compile(xpathExpression);
			XmlResult = evaluateXpath(doc, expr);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return XmlResult;
	}

	/**
	 * Prints the xml document.
	 * 
	 * @param document
	 *            the document
	 * @return the string
	 */
	public static String exportXMLDocumentToString(Document document) {
		DOMImplementationLS domImplementationLS = (DOMImplementationLS) document.getImplementation();
		LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
		String sb = "";
		try {
			sb = lsSerializer.writeToString(document);
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (LSException e) {
			e.printStackTrace();
		}
		return sb;

	}
}
