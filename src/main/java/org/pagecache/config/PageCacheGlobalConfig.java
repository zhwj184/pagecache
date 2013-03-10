package org.pagecache.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageCacheGlobalConfig {
	
	private static final long defaultExpiredTime = 1 * 24 * 60 * 60;
	
	private static List<String>  urlPattern = new ArrayList<String>();

	private static Map<String,Integer> urlCacheTime = new HashMap<String,Integer>();
	
	private static Map<String,List<String>> urlIncludeParams = new HashMap<String,List<String>>();
	
	private static Map<String,List<String>> urlExcludeParams = new HashMap<String,List<String>>();

	public static List<String> getUrlPattern() {
		return urlPattern;
	}

	public static void setUrlPattern(List<String> urlPattern) {
		PageCacheGlobalConfig.urlPattern = urlPattern;
	}

	public static long getCacheExpiredTime(String urlKey){
		if(PageCacheGlobalConfig.getUrlCacheTime().containsKey(urlKey)){
			return PageCacheGlobalConfig.getUrlCacheTime().get(urlKey);
		}
		return defaultExpiredTime;
	}
	
	public static long getDefaultCacheExpiredTime(){
		return defaultExpiredTime;
	}
	
	public static Map<String, Integer> getUrlCacheTime() {
		return urlCacheTime;
	}

	public static void setUrlCacheTime(Map<String, Integer> urlCacheTime) {
		PageCacheGlobalConfig.urlCacheTime = urlCacheTime;
	}

	public static Map<String, List<String>> getUrlIncludeParams() {
		return urlIncludeParams;
	}

	public static void setUrlIncludeParams(Map<String, List<String>> urlIncludeParams) {
		PageCacheGlobalConfig.urlIncludeParams = urlIncludeParams;
	}

	public static Map<String, List<String>> getUrlExcludeParams() {
		return urlExcludeParams;
	}

	public static void setUrlExcludeParams(Map<String, List<String>> urlExcludeParams) {
		PageCacheGlobalConfig.urlExcludeParams = urlExcludeParams;
	}
}
