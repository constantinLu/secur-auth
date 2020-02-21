package ro.secur.auth.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
class ExceptionModel {

    private String message;
    private HttpStatus httpStatus;
    private LocalDateTime timestamp;
}