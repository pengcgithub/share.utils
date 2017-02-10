package com.share.redis;


import com.share.redis.impl.Rule;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public interface CacheClient {
	/**
	 * 创建规则
	 * 
	 * @param key
	 * @param id
	 * @return
	 */
	public Rule createRule(String key, String id);

	/**
	 * 设置所有数据进hash缓存
	 * 
	 * @param list
	 */
	public <T> void hashSet(Rule rule, List<T> list);

	/**
	 * 对hash中的某一个数据进行更新或者增加
	 * 
	 * @param t
	 */
	public <T> void hashUpdate(Rule rule, T t);

	/**
	 * 对hash中的某一组数据进行更新或者增加
	 * 
	 * @param list
	 */
	public <T> void hashUpdate(Rule rule, List<T> list);

	/**
	 * 删除key
	 * 
	 * @param rule
	 */
	public void hashDel(Rule rule);

	/**
	 * 删除hash中的元素
	 * 
	 * @param t
	 */
	public <T> void hashRemove(Rule rule, T t);

	/**
	 * 删除hash中的元素
	 * 
	 * @param field
	 */
	public <T> void hashRemove(Rule rule, String field);

	/**
	 * 批量删除hash中的元素
	 * 
	 * @param rule
	 */
	public <T> void hashRemove(Rule rule, List<T> list);

	/**
	 * 批量删除hash中的元素
	 * 
	 * @param rule
	 */
	public void hashRemoveByFields(Rule rule, List<String> fields);

	/**
	 * 
	 * 取得所有hash缓存中的数据
	 * 
	 * @param typeOfT
	 * @return
	 */
	public <T> List<T> hashGetAll(Rule rule, Type typeOfT);

	/**
	 * 
	 * 根据limit和offset 取得hash缓存中的数据
	 * 
	 * @param typeOfT
	 * @return
	 */
	public <T> List<T> hashGetWithNumber(Rule rule, Type typeOfT, int limit,
										 int offset);

	/**
	 * 
	 * 根据ids获取hash缓存中的部分数据 ids数组存放的是对象唯一标识
	 * 
	 * @param typeOfT
	 * @param fields
	 * @return
	 */
	public <T> List<T> hashGetWithFields(Rule rule, Type typeOfT,
										 String[] fields);
	
	/** 
     * 根据pattern 获取所有的keys 
     * @param pattern 
     * @return 
     */  
    Set<String> getKeys(String pattern);
	

	/**
	 * 根据key 保存或者更新redis
	 * 
	 * @param key
	 * @param t
	 */
	public <T> void save(String key, T t);

	/**
	 * 根据key查询对象
	 * 
	 * @param key
	 * @param typeOfT
	 *            innerClass like : new TypeToken<List<Map<String ,
	 *            String>>>(){}.getType()
	 * @return
	 */
	public <T> T get(String key, Type typeOfT);

	/**
	 * 删除key对应的redis缓存
	 * 
	 * @param keys
	 */
	public void delete(String... keys);

	/**
	 * 匹配删除
	 * @param patten
	 */
	public void deletePattern(String patten);



	/**
	 * 清空所有缓存
	 */
	public void flushAll();

}
