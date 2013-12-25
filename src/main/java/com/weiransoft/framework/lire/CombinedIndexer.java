package com.weiransoft.framework.lire;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.DocumentBuilderFactory;
import net.semanticmetadata.lire.impl.ChainedDocumentBuilder;
import net.semanticmetadata.lire.utils.LuceneUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.weiransoft.framework.constant.IndexConstant;

public class CombinedIndexer {
	private static IndexWriter writer = null;
	private static ChainedDocumentBuilder builder = null;

	public boolean hasAddDoc = false;
	public int count;

	public CombinedIndexer() {
		super();
		getBuilder();
		getWriter();
	}

	@SuppressWarnings("deprecation")
	public void index(String identifier, InputStream stream, String keyWords, String productUrl) throws IOException {

		System.out.println("Index " + count + " image: " + identifier);
		BufferedImage image = ImageIO.read(stream);
		Document doc = builder.createDocument(image, identifier);
		doc.add(new Field(IndexConstant.FIELD_PRODUCT_URL, productUrl, Field.Store.YES, Field.Index.NOT_ANALYZED));
		if (StringUtils.isEmpty(keyWords)) {
			keyWords = "";
		}

		doc.add(new Field(IndexConstant.FIELD_KEYWORDS, keyWords, Field.Store.YES, Field.Index.ANALYZED));

		writer.addDocument(doc);
		count++;
		hasAddDoc = true;
		writer.commit();
	}

	public void closeWriter() {
		if (writer == null) {
			try {
				commit();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void commit() {
		if (writer == null && hasAddDoc) {
			try {

				writer.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ChainedDocumentBuilder getBuilder() {
		if (builder == null) {
			builder = new ChainedDocumentBuilder();
			builder.addBuilder(DocumentBuilderFactory.getCEDDDocumentBuilder());
		}
		return builder;
	}

	public IndexWriter getWriter() {
		if (writer == null) {
			IndexWriterConfig conf = new IndexWriterConfig(LuceneUtils.LUCENE_VERSION, new IKAnalyzer());

			try {
				writer = new IndexWriter(FSDirectory.open(new File(IndexConstant.INDEX_DATA_FILE_PATH)), conf);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return writer;
	}
}
