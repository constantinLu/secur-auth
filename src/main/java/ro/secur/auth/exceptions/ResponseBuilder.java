package ro.secur.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;

public class ResponseBuilder<T> extends GlobalException {

    public ResponseBuilder() {
    }

    public ResponseBuilder(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
        super(message, httpStatus, timestamp);
    }

    /**
     * @param ex          generic type of error
     * @param httpStatus status code for 400 and 500 series
     * @return responseEntity populated
     */
    ResponseEntity<Object> error(Exception ex, HttpStatus httpStatus) {
        GlobalException response = new GlobalException(ex.getMessage(), httpStatus, ZonedDateTime.now());
        return new ResponseEntity<>(response, httpStatus);
    }
}
