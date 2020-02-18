package ro.secur.auth.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Getter
@Setter
public class ExceptionModel {

    private String message;
    private HttpStatus httpStatus;
    private LocalDateTime timestamp;

    public ExceptionModel(String message, HttpStatus httpStatus, LocalDateTime timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}
