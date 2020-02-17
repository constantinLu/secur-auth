package ro.secur.auth.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


@Getter
@Setter
public class GlobalException {

    private String message;
    private HttpStatus httpStatus;
    private ZonedDateTime timestamp;

    public GlobalException() {
    }

    public GlobalException(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}
