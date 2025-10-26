package jan.ondra.learnservice.client.openai;

import jan.ondra.learnservice.openai.OpenAiRequestException;
import jan.ondra.learnservice.openai.OpenAiClient;
import jan.ondra.learnservice.openai.OpenAiProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@EnableWireMock
@EnableConfigurationProperties(OpenAiProperties.class)
@RestClientTest(
    value = OpenAiClient.class,
    properties = {
        "openai.api-key=dummy-key",
        "openai.base-url=${wiremock.server.baseUrl}",
        "openai.timeout.connect=500",
        "openai.timeout.read=500"
    }
)
class OpenAiClientTest {

    @Autowired
    private OpenAiClient openAiClient;

    @Nested
    class GenerateEmptyLearningUnits {

        @Test
        @DisplayName("returns correct result")
        void test1() throws IOException {
            var json = getJsonFromFile("openai-mock-responses/create_curriculum_response_body.json");

            stubFor(post("/v1/responses").willReturn(ok(json)));

            var result = openAiClient.generateEmptyLearningUnits("en", "History");

            assertThat(result)
                .extracting("number")
                .containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
            assertThat(result)
                .extracting("heading")
                .contains("Medieval World");
        }

        @Test
        @DisplayName("throws correct OpenAiRequestException on timeout")
        void test2() {
            stubFor(post("/v1/responses").willReturn(aResponse().withFixedDelay(500)));

            assertThatThrownBy(() -> openAiClient.generateEmptyLearningUnits("en", "History"))
                .isInstanceOf(OpenAiRequestException.class)
                .hasMessage("OpenAI request to generate EmptyLearningUnits failed")
                .hasRootCauseInstanceOf(SocketTimeoutException.class);
        }

        @Test
        @DisplayName("throws correct OpenAiRequestException on client error")
        void test3() {
            stubFor(post("/v1/responses").willReturn(aResponse().withStatus(400)));

            assertThatThrownBy(() -> openAiClient.generateEmptyLearningUnits("en", "History"))
                .isInstanceOf(OpenAiRequestException.class)
                .hasMessage("OpenAI request to generate EmptyLearningUnits failed")
                .hasRootCauseInstanceOf(HttpClientErrorException.class);
        }

        @Test
        @DisplayName("throws correct OpenAiRequestException on server error")
        void test4() {
            stubFor(post("/v1/responses").willReturn(aResponse().withStatus(500)));

            assertThatThrownBy(() -> openAiClient.generateEmptyLearningUnits("en", "History"))
                .isInstanceOf(OpenAiRequestException.class)
                .hasMessage("OpenAI request to generate EmptyLearningUnits failed")
                .hasRootCauseInstanceOf(HttpServerErrorException.class);
        }

        @Test
        @DisplayName("throws correct OpenAiRequestException on unexpected response body")
        void test5() {
            stubFor(post("/v1/responses").willReturn(ok("-.-")));

            assertThatThrownBy(() -> openAiClient.generateEmptyLearningUnits("en", "History"))
                .isInstanceOf(OpenAiRequestException.class)
                .hasMessage(
                    "OpenAI request to generate EmptyLearningUnits returned an unexpected response body: -.-"
                );
        }

    }

    private String getJsonFromFile(String filename) throws IOException {
        return new ClassPathResource(filename).getContentAsString(UTF_8);
    }

}
