package org.pagecache.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pagecache.cache.CacheStore;
import org.pagecache.config.PageCacheGlobalConfig;
import org.pagecache.config.UrlKeyCreator;
import org.pagecache.config.UrlKeyMatcher;

public class PageCacheFilter implements Filter {

	private CacheStore cacheStore;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest
				&& response instanceof HttpServletResponse) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;

			String method = httpRequest.getMethod();
			if("GET".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method)){
				String requestUri = httpRequest.getRequestURI();
				String urlPattern = UrlKeyMatcher.getMatchUrlPattern(requestUri);
				if(urlPattern != null){
					String urlKey = UrlKeyCreator.getUrlKey(requestUri, httpRequest.getParameterMap());
					String pageContent = cacheStore.get(urlKey);
					if(pageContent != null){
						httpResponse.getWriter().write(pageContent);
						return;
					}
					PageCacheHttpServletResponse pageCacheResponse = new PageCacheHttpServletResponse(httpResponse);
					filterChain.doFilter(request, pageCacheResponse);
					cacheStore.put(urlKey, pageCacheResponse.getPageContent());
					return ;
				}
	
			}
		}
		filterChain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		String cacheStoreName = arg0.getInitParameter("cacheStore");
		String cacheStoreParams = arg0.getInitParameter("cacheStoreParams");
		try {
			Class cacheStoreClass = Class.forName(cacheStoreName);
			cacheStore = (CacheStore) cacheStoreClass.newInstance();
			cacheStore.init(getInitParams(cacheStoreParams));
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		} catch (InstantiationException e) {
			throw new ServletException(e);
		} catch (IllegalAccessException e) {
			throw new ServletException(e);
		}
		String urlPattern = arg0.getInitParameter("urlPattern");
		String cacheExpireTime = arg0.getInitParameter("cacheExpireTime");
		String includeParams = arg0.getInitParameter("includeParams");
		String excludeParams = arg0.getInitParameter("excludeParams");
		initGrobleConfig(urlPattern, cacheExpireTime, includeParams,
				excludeParams);
	}

	private Map<String, String> getInitParams(String cacheStoreParams) {
		Map<String, String> initParams = new HashMap<String, String>();
		String[] keyValues = cacheStoreParams.split(";");
		if (keyValues != null && keyValues.length > 0) {
			for (String kv : keyValues) {
				String[] keyValue = kv.split(":");
				if (keyValue != null && keyValue.length == 2) {
					initParams.put(keyValue[0], keyValue[1]);
				}
			}
		}
		return initParams;
	}

	private void initGrobleConfig(String urlPattern, String cacheExpireTime,
			String includeParams, String excludeParams) {
		String[] urlPatterns = urlPattern.split(",");
		String[] cacheExpireTimes = cacheExpireTime.split(",");
		String[] includeParamList = includeParams.split(";");
		String[] excludeParamList = excludeParams.split(";");
		Map<String, Integer> urlCacheTime = new HashMap<String, Integer>();
		Map<String, List<String>> urlIncludeParams = new HashMap<String, List<String>>();
		Map<String, List<String>> urlExcludeParams = new HashMap<String, List<String>>();
		for (int i = 0; i < urlPatterns.length; i++) {
			urlCacheTime.put(urlPatterns[i],
					Integer.valueOf(cacheExpireTimes[i]));
			urlIncludeParams.put(urlPatterns[i],
					Arrays.asList(includeParamList[i].split(",")));
			urlExcludeParams.put(urlPatterns[i],
					Arrays.asList(excludeParamList[i].split(",")));
		}
		PageCacheGlobalConfig.setUrlPattern(Arrays.asList(urlPatterns));
		PageCacheGlobalConfig.setUrlCacheTime(urlCacheTime);
		PageCacheGlobalConfig.setUrlIncludeParams(urlIncludeParams);
		PageCacheGlobalConfig.setUrlExcludeParams(urlExcludeParams);
	}

}
