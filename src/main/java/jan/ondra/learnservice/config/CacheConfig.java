package jan.ondra.learnservice.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.concurrent.TimeUnit.HOURS;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        var userCache = new CaffeineCache(
            "users",
            Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, HOURS)
                .build()
        );

        cacheManager.setCaches(List.of(userCache));

        return cacheManager;
    }

}
