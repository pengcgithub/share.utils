package com.share.redis.impl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author zhouwei
 * 
 */
public class ShardedJedisPoolCacheClientImpl extends AbstractSmartRedisOpts {

	@Resource
	ShardedJedisPool shardedJedisPool;

	protected JedisCommands getResource() {
		return shardedJedisPool.getResource();
	}
	
	@Override
	protected void releaseResource(JedisCommands resource) {
		shardedJedisPool.returnResourceObject((ShardedJedis)resource);
	}

	@Override
	public Set<String> getKeys(String pattern) {
		Set<String> keys = new TreeSet<String>();
		ShardedJedis resource = null;
		try {
			resource = shardedJedisPool.getResource();
			Collection<Jedis> nodes = resource.getAllShards();
			for (Jedis node : nodes) {
				try {
					keys.addAll(node.keys(pattern));
				} catch (Exception e) {
				} finally {
					node.close();
				}
			}
		} finally{
			if(resource!=null){
				releaseResource(resource);
			}
		}
		return keys;
	}

	@Override
	public void flushAll() {
		ShardedJedis resource = null;
		try {
			resource = shardedJedisPool.getResource();
			Collection<Jedis> nodes = resource.getAllShards();
			for (Jedis node : nodes) {
				try {
					node.flushAll();
				} catch (Exception e) {
				} finally {
					node.close();
				}
			}
		} finally{
			if(resource!=null){
				releaseResource(resource);
			}
		}
	}

}
