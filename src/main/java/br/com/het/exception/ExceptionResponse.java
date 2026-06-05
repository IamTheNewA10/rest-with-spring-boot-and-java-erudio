package br.com.het.exception;

import java.util.Date;

// Classe q armazena informacoes do tratamento de exceptions
public record ExceptionResponse(Date timestamp, String message, String Details) {

}
