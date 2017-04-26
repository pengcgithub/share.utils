package com.share.redis.impl;

import com.share.json.GsonUtil;
import com.share.other.ReflectUtil;
import com.share.exception.BusinessException;
import com.share.redis.CacheClient;
import com.share.string.StringUtil;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisCommands;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 灵活的缓存操作类
 * 
 * 适用于处理类似于 List<JavaBean> 结构的数据 ， JavaBean中必须包含可以唯一标识此javaBean的属性。 或者
 * List<Map<String,Object>>结构，Map中的某一个key对应的Object作为唯一标识。 或者
 * List<Map<String,JavaBean
 * >>结构，与上面结构有点类似，但是也有区别，Object比如是一个int或者String等，所以是可以作为唯一标识的。
 * 但是，如果是javaBean对象，因为每个对象都是不同的，所以肯定是唯一的，但是也没有办法去区分是新增还是更新，
 * 所以，这种情况下，需要实现javaBean的toString()方法来唯一标识,
 * toString()方法的返回值作为确定对象是否相同的依据,同时作为hash中的field。
 * 
 * @author pengc
 * 
 */
public abstract class AbstractSmartRedisOpts implements CacheClient {

	/**
	 * 创建规则
	 * 
	 * @param key
	 * @param id
	 * @return
	 */
	public Rule createRule(String key, String id) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(id)) {
			throw new BusinessException("00900001");
		}
		return new Rule(key, id);
	}

	/**
	 * 设置所有数据进hash缓存
	 * 
	 * @param list
	 */
	public <T> void hashSet(final Rule rule, final List<T> list) {
		excute(new RedisInvocation() {
			@Override
			public void invoke(JedisCommands resource) {
				// 清空key对应的缓存
				if (resource.exists(rule.getKey())) {
					resource.del(rule.getKey());
					resource.del(rule.getIndexingKey());
				}

				// 遍历存入hash
				for (T t : list) {
					// 获取对象中指定的属性值，做为数据的唯一标识
					String field = getField(rule, t);
					// 保存数据的唯一标识到list
					resource.rpush(rule.getIndexingKey(), field);
					// 实际数据保存到hsah
					resource.hset(rule.getKey(), field, GsonUtil.toJson(t));
				}
			}
		});
	}

	/**
	 * 对hash中的某一个数据进行更新或者增加
	 * 
	 * @param t
	 */
	public <T> void hashUpdate(final Rule rule, final T t) {
		excute(new RedisInvocation() {
			@Override
			public void invoke(JedisCommands resource) {
				if (!resource.exists(rule.getKey())) {
					throw new BusinessException("00900002",
							new String[] { rule.getKey() });
				}

				// 所有唯一标识
				List<String> fields = resource.lrange(rule.getIndexingKey(), 0, -1);

				hashUpdateOne(rule, fields, t);
			}
		});
	}

	/**
	 * 对hash中的某一组数据进行更新或者增加
	 * 
	 * @param list
	 */
	public <T> void hashUpdate(final Rule rule, final List<T> list) {
		excute(new RedisInvocation() {
			@Override
			public void invoke(JedisCommands resource) {
				if (!resource.exists(rule.getKey())) {
					throw new BusinessException("00900002",
							new String[] { rule.getKey() });
				}

				// 所有唯一标识
				List<String> fields = resource.lrange(rule.getIndexingKey(), 0, -1);

				for (T t : list) {
					hashUpdateOne(rule, fields, t);
				}
			}
		});
	}

	/**
	 * @param rule
	 * @param fields
	 * @param t
	 */
	private <T> void hashUpdateOne(final Rule rule, final List<String> fields, final T t) {
		excute(new RedisInvocation() {
			@Override
			public void invoke(JedisCommands resource) {
				// 获取对象中指定的属性值，做为数据的唯一标识
				String field = getField(rule, t);

				if (fields.contains(field)) {
					// 实际数据保存到hsah
					resource.hset(rule.getKey(), field, GsonUtil.toJson(t));
				} else {
					// 保存数据的唯一标识到list
					resource.rpush(rule.getIndexingKey(), field);
					// 实际数据保存到hsah
					resource.hset(rule.getKey(), field, GsonUtil.toJson(t));
				}
			}
		});
	}

	/**
	 * 删除key
	 * 
	 * @param rule
	 */
	public void hashDel(final Rule rule) {
		excute(new RedisInvocation() {
			@Override
			public void invoke(JedisCommands resource) {
				resource.del(rule.getIndexingKey());
				resource.del(rule.getKey());
			}
		});
	}

	/**
	 * 删除hash中的元素
	 * 
	 * @param t
	 */
	public <T> void hashRemove(Rule rule, T t) {
		// 获取对象中指定的属性值，做为数据的唯一标识
		String field = getField(rule, t);
		hashRemove(rule, field);
	}

	/**
	 * 删除hash中的元素
	 * 
	 * @param field
	 */
	public <T> void hashRemove(final Rule rule, final String field) {
		excute(new RedisInvocation() {
			@Override
			public void invoke(JedisCommands resource) {
				resource.lrem(rule.getIndexingKey(), 0, field);
				resource.hdel(rule.getKey(), field);
			}
		});
	}

	/**
	 * 批量删除hash中的元素
	 *
	 * @param rule
	 * @param list
	 */
	public <T> void hashRemove(Rule rule, List<T> list) {
		// 循环删除
		for (T t : list) {
			hashRemove(rule, t);
		}
	}

	/**
	 * 批量删除hash中的元素
	 * 
	 * @param rule
	 * @param fields
	 */
	public void hashRemoveByFields(Rule rule, List<String> fields) {
		// 循环删除
		for (String field : fields) {
			hashRemove(rule, field);
		}
	}

	/**
	 * 
	 * 取得所有hash缓存中的数据
	 * 
	 * @param typeOfT
	 * @return
	 */
	public <T> List<T> hashGetAll(Rule rule, Type typeOfT) {
		return hashGetWithFields(rule, typeOfT, null);
	}

	/**
	 * 根据limit和offset 取得hash缓存中的数据
	 * @FileName  AbstractSmartRedisOpts.java
	 * @author  彭程
	 * @param rule
	 * @param typeOfT
	 * @param limit
	 * @param offset
	 * @date  [2017/2/10 16:25]
	 * @since  V1.0.0
	 */
	public <T> List<T> hashGetWithNumber(final Rule rule, Type typeOfT, int limit,
			int offset) {

		final int start = limit;//改为从0开始
		final int end = start + offset - 1;
		
		String[] fields = excuteWithReturn(new RedisInvocationWithReturn<String[]>(){
			@Override
			public String[] invoke(JedisCommands resource) {
				String[] fields = resource
						.lrange(rule.getIndexingKey(), start, end).toArray(
								new String[] {});
				return fields;
			}
		});

		return hashGetWithFields(rule, typeOfT, fields);
	}

	/**
	 * 
	 * 根据ids获取hash缓存中的部分数据 ids数组存放的是对象唯一标识
	 * 
	 * @param typeOfT
	 * @param fields
	 * @return
	 */
	public <T> List<T> hashGetWithFields(final Rule rule, final Type typeOfT,
			final String[] fields) {
		
		List<T> list = excuteWithReturn(new RedisInvocationWithReturn<List<T>>(){
			@Override
			public List<T> invoke(JedisCommands resource) {
				// 对象唯一标识保存的list对应的key
				String indexingKey = rule.getIndexingKey();

				// 如果key不存在，则返回null
				if (!resource.exists(indexingKey)) {
					return null;
				}
				//fieldsNew
				String[] fieldsNew = fields;
				// 获取数组对应的值
				if (fieldsNew == null || fieldsNew.length == 0) {
					// 所有标识数组
					fieldsNew = resource.lrange(indexingKey, 0, -1).toArray(
							new String[] {});
				}
				List<String> values = resource.hmget(rule.getKey(), fieldsNew);
				//结果集
				List<T> list = new ArrayList<T>();
				// 转换成对象
				for (String value : values) {
					if (StringUtils.isNotEmpty(value)) {
						T t = GsonUtil.fromJson(value, typeOfT);
						list.add(t);
					}
				}
				
				return list;
			}
		});

		return list;
	}

	/**
	 * 获取T中的唯一标识的值作为hash中的field
	 * 
	 * @param rule
	 * @param t
	 * @return
	 */
	private <T> String getField(Rule rule, T t) {
		String field = "";
		if (t instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) t;
			Object value = map.get(rule.getId());
			if (value == null) {
				throw new BusinessException("00900003",
						new String[] { rule.getId() });
			}
			field = value.toString();
		} else {
			field = (String) ReflectUtil.execMethod(t,
					"get" + StringUtil.toUpperCaseFirstOne(rule.getId()), null,
					null);
		}
		return field;
	}

	protected abstract JedisCommands getResource();
	
	protected abstract void releaseResource(JedisCommands resource);

	/**
	 * 根据key 保存或者更新redis
	 * 
	 * @param key
	 * @param t
	 */
	public <T> void save(final String key, final T t) {
		if (StringUtil.isEmpty(key) || t == null) {
			return;
		}
		
		excute(new RedisInvocation() {
			@Override
			public void invoke(JedisCommands resource) {
				String json = GsonUtil.toJson(t);
				resource.set(key, json);
			}
		});
	}

	/**
	 * 根据key查询对象
	 * 
	 * @param key
	 * @param typeOfT
	 *            innerClass like : new TypeToken<List<Map<String ,
	 *            String>>>(){}.getType()
	 * @return
	 */
	public <T> T get(final String key, final Type typeOfT) {
		if (StringUtil.isEmpty(key) || typeOfT == null) {
			return null;
		}
		
		T obj = excuteWithReturn(new RedisInvocationWithReturn<T>(){
			@Override
			public T invoke(JedisCommands resource) {
				String strJson = resource.get(key);
				return GsonUtil.fromJson(strJson, typeOfT);
			}
		});
		
		
		return obj;
	}

	/**
	 * 删除key对应的redis缓存
	 * 
	 * @param keys
	 */
	public void delete(final String... keys) {
		excute(new RedisInvocation() {
			@Override
			public void invoke(JedisCommands resource) {
				for (String key : keys) {
					if (!StringUtil.isEmpty(key)) {
						resource.del(key);
					}
				}
			}
		});
	}

	/**
	 * 匹配删除
	 * @param pattern
	 */
	public void deletePattern(final String pattern) {
		excute(new RedisInvocation() {
			@Override
			public void invoke(JedisCommands resource) {
				Set<String> keys = getKeys(pattern);
				for (String key : keys) {
					if (!StringUtil.isEmpty(key)) {
						resource.del(key);
					}
				}
			}
		});
	}
	
	interface RedisInvocation{
		void invoke(JedisCommands resource);
	}
	
	interface RedisInvocationWithReturn<T>{
		T invoke(JedisCommands resource);
	}
	
	public <T> T excuteWithReturn(RedisInvocationWithReturn<T> invocation){
		JedisCommands resource = null;
		T obj = null;
		try {
			resource = getResource();
			obj = invocation.invoke(resource);
		} finally{
			if(resource!=null){
				releaseResource(resource);
			}
		}
		return obj;
	}
	
	public void excute(RedisInvocation invocation){
		JedisCommands resource = null;
		try {
			resource = getResource();
			invocation.invoke(resource);
		} finally{
			if(resource!=null){
				releaseResource(resource);
			}
		}
	}
	
}
