package br.com.het.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.het.exception.BadRequestException;
import br.com.het.exception.ExceptionResponse;
import br.com.het.exception.FileNotFoundException;
import br.com.het.exception.FileStorageException;
import br.com.het.exception.RequiredObjectIsNullException;
import br.com.het.exception.ResourceNotFoundException;

@RestController
@ControllerAdvice // <- define essa classe como tratamento global, qualquer exception lançada por
                  // outro controller cai aq
public class CostumizedEntityResponseHandler extends ResponseEntityExceptionHandler {

  // trata erros e exceptions genericos do servidor
  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
    ExceptionResponse response = new ExceptionResponse(
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(Exception ex, WebRequest request) {
    ExceptionResponse response = new ExceptionResponse(
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(RequiredObjectIsNullException.class)
  public final ResponseEntity<ExceptionResponse> handleRequiredObjectExceptions(Exception ex, WebRequest request) {
    ExceptionResponse response = new ExceptionResponse(
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadRequestException.class)
  public final ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception ex, WebRequest request) {
    ExceptionResponse response = new ExceptionResponse(
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(FileNotFoundException.class)
  public final ResponseEntity<ExceptionResponse> handleFileNotFoundExceptions(Exception ex, WebRequest request) {
    ExceptionResponse response = new ExceptionResponse(
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(FileStorageException.class)
  public final ResponseEntity<ExceptionResponse> handleFileStorageException(Exception ex, WebRequest request) {
    ExceptionResponse response = new ExceptionResponse(
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
