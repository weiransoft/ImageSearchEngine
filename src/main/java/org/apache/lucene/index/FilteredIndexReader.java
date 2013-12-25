package org.apache.lucene.index;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.semanticmetadata.lire.utils.LuceneUtils;

import org.apache.lucene.document.DocumentStoredFieldVisitor;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.weiransoft.framework.constant.IndexConstant;

public class FilteredIndexReader extends CompositeReader {
	private IndexReader reader;
	private ScoreDoc[] docs;

	@SuppressWarnings("deprecation")
	public FilteredIndexReader(String keyWords, int maxDocs) throws IOException, ParseException {
		if (reader == null)
			reader = IndexReader.open(FSDirectory.open(new File(IndexConstant.INDEX_DATA_FILE_PATH)));
		
		QueryParser parser = new QueryParser(LuceneUtils.LUCENE_VERSION, IndexConstant.FIELD_KEYWORDS, new IKAnalyzer());
		parser.setDefaultOperator(QueryParser.AND_OPERATOR);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(maxDocs, true);
		
		searcher.search(parser.parse(keyWords), collector);
		docs = collector.topDocs().scoreDocs;
		System.out.println("Fileter Doc Size:" + docs.length);
	}

	public int maxDoc() {
		return docs.length;
	}

	public int numDocs() {
		return docs.length;
	}

	@Override
	protected List<? extends IndexReader> getSequentialSubReaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doClose() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int docFreq(Term arg0) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void document(int i, StoredFieldVisitor visitor) throws IOException {
		if (visitor instanceof DocumentStoredFieldVisitor) {
			((DocumentStoredFieldVisitor) visitor).setDocument(reader.document(docs[i].doc));
		}

	}

	@Override
	public Fields getTermVectors(int arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasDeletions() {
		return false;
	}

	@Override
	public long totalTermFreq(Term arg0) throws IOException {
		return 0;
	}

}
