package org.pagecache.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlKeyCreator {

	public static String getUrlKey(String url, String urlPattern, Map<String, String[]> params) {
		if (url == null || url.isEmpty()) {
			throw new IllegalArgumentException("url must not be null");
		}
		StringBuilder sb = new StringBuilder(url);
		List<String> includeParams = PageCacheGlobalConfig
				.getUrlIncludeParams().get(urlPattern);
		List<String> excludeParams = PageCacheGlobalConfig
				.getUrlExcludeParams().get(urlPattern);
		String paramsKey = "";
		if (includeParams != null) {
			paramsKey = getIncludeParamsKey(params, includeParams);
		} else if (excludeParams != null) {
			paramsKey = getExcludeParamsKey(params, excludeParams);
		}
		sb.append(paramsKey);
		return sb.toString();
	}

	private static String getIncludeParamsKey(Map<String, String[]> params,
			List<String> includeParams) {
		Map<String, String[]> valueMap = new HashMap<String, String[]>();
		for (String includeParam : includeParams) {
			if (params.containsKey(includeParam)) {
				valueMap.put(includeParam, params.get(includeParam));
			}
		}
		return generalParams(valueMap);

	}

	private static String getExcludeParamsKey(Map<String, String[]> params,
			List<String> excludeParams) {
		Map<String, String[]> valueMap = new HashMap<String, String[]>(params);
		for (String excludeParam : excludeParams) {
			if (params.containsKey(excludeParam)) {
				valueMap.remove(excludeParam);
			}
		}
		return generalParams(valueMap);
	}

	private static String generalParams(Map<String, String[]> valueMap) {
		List<Map.Entry<String, String[]>> mappingList = null;
		mappingList = new ArrayList<Map.Entry<String, String[]>>(valueMap.entrySet());
		Collections.sort(mappingList,
				new Comparator<Map.Entry<String, String[]>>() {
					public int compare(Map.Entry<String, String[]> mapping1,
							Map.Entry<String, String[]> mapping2) {
						return mapping1.getKey().compareTo(mapping2.getKey());
					}
				});
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String[]> entry : mappingList) {
			sb.append(entry.getKey() + "&");
			List<String> valueList = Arrays.asList(entry.getValue());
			Collections.sort(valueList);
			for(String value: valueList){
				sb.append(value + ";");
			}
		}
		return sb.toString();
	}

}
