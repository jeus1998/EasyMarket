package easy.market.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private String field;
    public BusinessException(String message, String field) {
        super(message);
        this.field = field;
    }
    public BusinessException(String message) {
       super(message);
    }
}
