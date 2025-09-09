package jan.ondra.learnservice.domain.user.persistence;

import org.springframework.dao.DataIntegrityViolationException;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(DataIntegrityViolationException cause) {
        super(cause);
    }
}
