package jan.ondra.learnservice.client.openai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jan.ondra.learnservice.domain.curriculum.model.EmptyLearningUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@EnableConfigurationProperties(OpenAiProperties.class)
public class OpenAiClient {

    private final RestClient restClient;
    private final String createCurriculumRequestBodyTemplate;
    private final ObjectMapper objectMapper;

    public OpenAiClient(
        @Value("classpath:openai-request-templates/create-curriculum.json") Resource curriculumResource,
        OpenAiProperties openAiProperties,
        RestClient.Builder restClientBuilder,
        ObjectMapper objectMapper
    ) throws IOException {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(openAiProperties.timeout().connect());
        requestFactory.setReadTimeout(openAiProperties.timeout().read());

        this.restClient = restClientBuilder
            .requestFactory(requestFactory)
            .baseUrl(openAiProperties.baseUrl())
            .defaultHeader(AUTHORIZATION, "Bearer " + openAiProperties.apiKey())
            .build();

        this.createCurriculumRequestBodyTemplate = curriculumResource.getContentAsString(UTF_8);

        this.objectMapper = objectMapper;
    }

    public List<EmptyLearningUnit> generateEmptyLearningUnits(String language, String topic) {
        String responseJson;

        try {
            responseJson = restClient
                .post()
                .uri("/v1/responses")
                .contentType(APPLICATION_JSON)
                .body(String.format(createCurriculumRequestBodyTemplate, language, topic))
                .retrieve()
                .body(String.class);
        } catch (RestClientException e) {
            throw new OpenAiRequestException("OpenAI request to generate EmptyLearningUnits failed", e);
        }

        try {
            var jsonContent = objectMapper.readTree(responseJson)
                .get("output")
                .get(1)
                .get("content")
                .get(0)
                .get("text")
                .asText();

            var emptyLearningUnits = objectMapper.convertValue(
                objectMapper.readTree(jsonContent).get("learningUnits"),
                new TypeReference<List<EmptyLearningUnit>>() {}
            );

            return requireNonNull(emptyLearningUnits);
        } catch (Exception e) {
            throw new OpenAiRequestException(
                "OpenAI request to generate EmptyLearningUnits returned an unexpected response body: " + responseJson, e
            );
        }
    }

}
