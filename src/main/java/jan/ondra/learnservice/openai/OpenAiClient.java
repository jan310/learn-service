package jan.ondra.learnservice.openai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jan.ondra.learnservice.curriculum.model.EmptyLearningUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@EnableConfigurationProperties(OpenAiProperties.class)
public class OpenAiClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String createCurriculumRequestBodyTemplate;
    private final String createLearningUnitContentRequestBodyTemplate;

    public OpenAiClient(
        @Value("classpath:openai-request-templates/create-curriculum.json") Resource curriculumResource,
        @Value("classpath:openai-request-templates/create-learning-unit-content.json") Resource learningUnitResource,
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

        this.objectMapper = objectMapper;

        this.createCurriculumRequestBodyTemplate = curriculumResource.getContentAsString(UTF_8);
        this.createLearningUnitContentRequestBodyTemplate = learningUnitResource.getContentAsString(UTF_8);
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

    public String generateLearningUnitContent(String language, String topic, String heading, String subheading) {
        String responseJson;

        try {
            responseJson = restClient
                .post()
                .uri("/v1/responses")
                .contentType(APPLICATION_JSON)
                .body(
                    String.format(
                        createLearningUnitContentRequestBodyTemplate,
                        language,
                        topic,
                        heading,
                        subheading
                    )
                )
                .retrieve()
                .body(String.class);
        } catch (RestClientException e) {
            throw new OpenAiRequestException("OpenAI request to generate a LearningUnit failed", e);
        }

        try {
            return objectMapper.readTree(responseJson)
                .get("output")
                .get(1)
                .get("content")
                .get(0)
                .get("text")
                .asText();
        } catch (Exception e) {
            throw new OpenAiRequestException(
                "OpenAI request to generate a LearningUnit returned an unexpected response body: " + responseJson, e
            );
        }
    }

    @Async
    public CompletableFuture<String> generateLearningUnitContentAsync(
        String language,
        String topic,
        String heading,
        String subheading
    ) {
        String result = generateLearningUnitContent(language, topic, heading, subheading);
        return CompletableFuture.completedFuture(result);
    }

}
