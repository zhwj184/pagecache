基于servlet的页面级缓存框架的基本用法：

在web.xml里面配置

urlPattern：需要缓存的页面url的正则表达式列表，多个不同url的正则用，分隔；

cacheExpireTime：对应urlPattern的每个url的缓存时间，单位秒，用，分隔；

includeParams：对应urlPattern的每个url需要考虑的参数列表，每个url的参数用；分隔，每个url的多个参数用，号分隔；需要配置了该参数，则不考虑excludeParams的参数配置；

excludeParams：对应urlPattern的每个url需要排除的参数列表，每个url的参数用；分隔，每个url的多个参数用，号分隔；

cacheStore：缓存策略，这里提供基本本地的缓存LRU实现SimpleLRUCacheStore，用类名，可以通过实现org.pagecache.cache.CacheStore接口来实现自己缓存存储策略，常用的可以用memcache，后续提供

cacheStoreParams：对应cacheStore的缓存策略类参数列表，参数之间用；号分隔，参数名和参数值用：分隔，在init参数中可以根据参数做一些初始化工作；



