package org.example.canicampusconnectapi;

import org.example.canicampusconnectapi.common.exception.InvalidPasswordException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalInterceptorException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> intercepteurViolationContrainte(MethodArgumentNotValidException ex) {

        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        })
        ;
        return errors;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // Code de retour
    @ResponseBody // Assure que la réponse est envoyée au format JSON (dans le corp de la réponse)
    public Map<String, Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ConstraintViolationException cause = (ConstraintViolationException) ex.getCause();
        if (cause.getKind() == ConstraintViolationException.ConstraintKind.UNIQUE) {

            return Map.of("message", "Violation of unicity constraint");
        }
        else {
            return Map.of("message", "A constraint violation occurred - Foreign Key");
        }
    }

    // Ajouter un handler spécifique pour les erreurs de mot de passe
    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleInvalidPasswordException(InvalidPasswordException ex) {
        return Map.of(
                "error", ex.getMessage(),
                "field", "currentPassword"
        );
    }



//    A dé-commenter uniquement pour voir les erreurs en production
//
//    @ExceptionHandler(value = {Exception.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    protected Map<String, Object> handleAllErrors(RuntimeException ex, WebRequest request) {
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw);
//        ex.printStackTrace(pw);
//        String bodyOfResponse = sw.toString();
//        return Map.of("message", bodyOfResponse);
//    }
}
