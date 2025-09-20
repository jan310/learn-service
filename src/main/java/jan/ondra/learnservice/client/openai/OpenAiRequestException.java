package jan.ondra.learnservice.client.openai;

public class OpenAiRequestException extends RuntimeException {
    public OpenAiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
