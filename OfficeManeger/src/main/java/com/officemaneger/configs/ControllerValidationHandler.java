package com.officemaneger.configs;

import com.officemaneger.configs.errors.CustomFieldError;
import com.officemaneger.enums.MessageType;
import com.officemaneger.messages.messageDTO.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;

@ControllerAdvice
public class ControllerValidationHandler {

    @Qualifier("messageSource")
    @Autowired
    private MessageSource msgSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MessageDTO processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        FieldError error = result.getFieldError();

        return processFieldError(error);
    }

    @ExceptionHandler(CustomFieldError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MessageDTO processDuplicateTextFieldError(CustomFieldError ex) {
        FieldError fieldError = ex.getFieldError();

        return processFieldError(fieldError);
    }

    private MessageDTO processFieldError(FieldError error) {
        MessageDTO message = null;
        if (error != null) {
            Locale currentLocale = LocaleContextHolder.getLocale();
            String fieldName = error.getField();
            String msg = msgSource.getMessage(error.getDefaultMessage(), null, currentLocale);
            message = new MessageDTO(MessageType.ERROR, msg, fieldName);
        }

        return message;
    }
}
