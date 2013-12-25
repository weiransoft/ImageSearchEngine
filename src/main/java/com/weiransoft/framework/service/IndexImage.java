package com.weiransoft.framework.service;

public class IndexImage {
	private String imageUrl;
	private String keyWords;
	private String productUrl;
	private float score;

	public IndexImage() {
		super();
	}

	public IndexImage(String imageUrl, String keyWords, String productUrl, float score) {
		super();
		this.imageUrl = imageUrl;
		this.keyWords = keyWords;
		this.productUrl = productUrl;
		this.score = score;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

}
