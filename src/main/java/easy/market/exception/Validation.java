package easy.market.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Validation {
    private final String fieldName;
    private final String errorMessage;
}

