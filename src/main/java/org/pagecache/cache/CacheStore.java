package org.pagecache.cache;

import java.util.Map;

public interface CacheStore {
	
	public void init(Map<String,String> initParams);
	
	public void put(String key, String value, String urlPattern);
	
	public String get(String key);

}
