package easy.market.exception;

public class UsernameAlreadyExistsException extends BusinessException{
    public UsernameAlreadyExistsException(String message, String field) {
        super(message, field);
    }
}
