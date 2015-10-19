package com.naivebayes.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;

@Component
public class NaiveBayesCache {

	@Autowired
	private CacheManager cacheManager;

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public NaiveBayesRemovalListener getListener() {
		return listener;
	}

	public void setListener(NaiveBayesRemovalListener listener) {
		this.listener = listener;
	}

	public CacheManager getManager() {
		return manager;
	}

	public void setManager(CacheManager manager) {
		this.manager = manager;
	}

	@Autowired
	CacheManager manager;

	@Autowired
	NaiveBayesRemovalListener listener;

	@SuppressWarnings("unchecked")
	public void init() {

		((GuavaCacheManager) this.getCacheManager())
				.setCacheBuilder(CacheBuilder.newBuilder().expireAfterAccess(
						12, TimeUnit.HOURS));
		System.out.println("Cache Initialized");
	}

	public Cache getCache(String key) throws ExecutionException {
		return (GuavaCache) ((GuavaCacheManager) this.getCacheManager())
				.getCache(key);
	}

}
