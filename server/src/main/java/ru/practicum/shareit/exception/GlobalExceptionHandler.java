package ru.practicum.shareit.exception;


import io.micrometer.core.instrument.config.validate.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateEmailException(DuplicateEmailException e) {
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(Throwable e) {
        return new ErrorResponse("Произошла непредвиденная ошибка: " + e.getMessage());
    }
}