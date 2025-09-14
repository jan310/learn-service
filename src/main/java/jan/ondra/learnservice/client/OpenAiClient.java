package jan.ondra.learnservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jan.ondra.learnservice.domain.curriculum.model.EmptyLearningUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class OpenAiClient {

    private final RestClient restClient;
    private final String createCurriculumRequestBodyTemplate;
    private final ObjectMapper objectMapper;

    public OpenAiClient(
        @Value("${openai.api-key}") String apiKey,
        @Value("classpath:openai-request-templates/create-curriculum.json") Resource curriculumResource,
        RestClient.Builder restClientBuilder,
        ObjectMapper objectMapper
    ) throws IOException {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2_000);
        requestFactory.setReadTimeout(30_000);

        this.restClient = restClientBuilder
            .requestFactory(requestFactory)
            .baseUrl("https://api.openai.com/v1")
            .defaultHeader(AUTHORIZATION, "Bearer " + apiKey)
            .build();

        this.createCurriculumRequestBodyTemplate = curriculumResource.getContentAsString(UTF_8);

        this.objectMapper = objectMapper;
    }

    public List<EmptyLearningUnit> generateEmptyLearningUnits(String language, String topic) {
        var responseJson = restClient
            .post()
            .uri("/responses")
            .contentType(APPLICATION_JSON)
            .body(String.format(createCurriculumRequestBodyTemplate, language, topic))
            .retrieve()
            .body(String.class);

        try {
            var jsonContent = objectMapper.readTree(responseJson)
                .get("output")
                .get(1)
                .get("content")
                .get(0)
                .get("text")
                .asText();

            return objectMapper.convertValue(
                objectMapper.readTree(jsonContent).get("learningUnits"),
                new TypeReference<>() {}
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON response from OpenAI", e);
        }
    }

}
