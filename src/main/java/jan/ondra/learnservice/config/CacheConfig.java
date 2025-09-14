package jan.ondra.learnservice.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        var userCache = new CaffeineCache(
            "userCache",
            Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, HOURS)
                .build()
        );

        var curriculumCache = new CaffeineCache(
            "curriculumCache",
            Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(30, MINUTES)
                .build()
        );

        cacheManager.setCaches(List.of(userCache, curriculumCache));

        return cacheManager;
    }

    @Bean
    public Cache userCache(CacheManager cacheManager) {
        return cacheManager.getCache("userCache");
    }

    @Bean
    public Cache curriculumCache(CacheManager cacheManager) {
        return cacheManager.getCache("curriculumCache");
    }

}
