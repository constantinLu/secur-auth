package ro.secur.auth.exceptions;

        import lombok.extern.slf4j.Slf4j;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.ControllerAdvice;
        import org.springframework.web.bind.annotation.ExceptionHandler;
        import ro.secur.auth.exceptions.custom.UserNotFoundException;

@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    private final ResponseFactory response;

    public ExceptionControllerAdvice(ResponseFactory response) {
        this.response = response;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(final UserNotFoundException e) {
        log.error(e.getMessage());
        return response.create(e, HttpStatus.BAD_REQUEST);
    }

}
