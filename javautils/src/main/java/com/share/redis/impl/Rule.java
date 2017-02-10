package com.share.redis.impl;

public class Rule {

	/**
	 * 构造函数
	 * 
	 * @param key
	 * @param id
	 */
	protected Rule(String key, String id) {
		super();
		this.key = key;
		this.id = id;
	}

	/**
	 * 保存到redis中的key
	 */
	private String key;

	/**
	 * 对象的唯一标识属性
	 */
	private String id;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIndexingKey() {
		return getKey() + ":" + getId();
	}

}
