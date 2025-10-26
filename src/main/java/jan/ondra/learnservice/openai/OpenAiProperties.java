package jan.ondra.learnservice.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openai")
public record OpenAiProperties(
    String apiKey,
    String baseUrl,
    Timeout timeout
) {
    public record Timeout(
        int connect,
        int read
    ) {}
}
