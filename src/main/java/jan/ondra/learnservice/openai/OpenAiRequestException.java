package jan.ondra.learnservice.openai;

public class OpenAiRequestException extends RuntimeException {
    public OpenAiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
