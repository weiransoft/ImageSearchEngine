package com.weiransoft.framework.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.FilteredIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.weiransoft.framework.constant.IndexConstant;

@Service
@Scope("prototype")
public class ImageSearchService {
	public static final int MAX_RESULTS = 20;

	@SuppressWarnings("deprecation")
	public List<IndexImage> search(InputStream reference, String keyWords) throws IOException {
		IndexReader filter;
		List<IndexImage> result = null;
		ImageSearcher searcher;
		Map<String, IndexImage> indexImageMap = new HashMap<String, IndexImage>();
		BufferedImage image;
		try {
			image = ImageIO.read(reference);
			if (StringUtils.isNotEmpty(keyWords)) {
				filter = new FilteredIndexReader(keyWords, MAX_RESULTS);
			} else {
				filter = IndexReader.open(FSDirectory.open(new File(IndexConstant.INDEX_DATA_FILE_PATH)));
			}
			searcher = ImageSearcherFactory.createCEDDImageSearcher(MAX_RESULTS);
			ImageSearchHits hits = searcher.search(image, filter);

			if (hits.length() > 0) {
				for (int i = 0; i < hits.length(); i++) {
					Document doc = hits.doc(i);
					if (doc.getFields().size() > 0) {
						IndexableField keyWordsfield = doc.getField(IndexConstant.FIELD_KEYWORDS);
						IndexableField productUrlFiled = doc.getField(IndexConstant.FIELD_PRODUCT_URL);
						IndexableField fileNameFiled = doc.getField(DocumentBuilder.FIELD_NAME_IDENTIFIER);
						if (productUrlFiled != null && keyWordsfield != null && fileNameFiled != null) {
							String imageUrl = fileNameFiled.stringValue();
							if (!indexImageMap.containsKey(imageUrl)) {
								indexImageMap.put(imageUrl, new IndexImage(imageUrl, keyWordsfield.stringValue(), productUrlFiled.stringValue(), hits.score(i)));
							}
						}
					}
				}
				result = new ArrayList<IndexImage>(indexImageMap.values());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
