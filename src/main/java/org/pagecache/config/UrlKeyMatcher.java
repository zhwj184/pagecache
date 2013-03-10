package org.pagecache.config;

import java.util.regex.Pattern;

public class UrlKeyMatcher {
	
	public static String getMatchUrlPattern(String url){
		for(String urlPattern: PageCacheGlobalConfig.getUrlPattern()){
			if(Pattern.matches(urlPattern, url)){
				return urlPattern;
			}
		}
		return null;
	}

}
