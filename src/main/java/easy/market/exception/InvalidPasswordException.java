package easy.market.exception;

public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException(String message, String field) {
        super(message, field);
    }
}
