package code.grind.giftedschoolonline.system.Exception;


import code.grind.giftedschoolonline.system.Result;
import code.grind.giftedschoolonline.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExpectionHandlerAdvice {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleObjectNotFoundException(ObjectNotFoundException ex){
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
    }

    //handles invalid inputs

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(MethodArgumentNotValidException ex){
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String,String> map = new HashMap<>(errors.size());
        errors.forEach((error) ->{
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return new Result(false, StatusCode.INVALID_ARGUMENT, "Provided arguments are Invalid, see data for details.", map);
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAuthenticationException(Exception ex){
        return new Result(false, StatusCode.UNAUTHORIZED,"username or password is incorrect", ex.getMessage());
    }


}
