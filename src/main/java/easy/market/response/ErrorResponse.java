package easy.market.response;

import easy.market.exception.Validation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {
    private final String message;
    private final List<Validation> validations = new ArrayList<>();

    public ErrorResponse(String message) {
        this.message = message;
    }

    public void addValidation(String field, String message) {
        validations.add(new Validation(field, message));
    }
}
