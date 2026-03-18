package com.chemical.common.handler;

import com.chemical.common.BaseResponse;
import com.chemical.common.errors.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Tất cả các Exception không được khai báo sẽ được xử lý tại đây
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        // quá trình kiểm soat lỗi diễn ra ở đây
        ex.printStackTrace();
        logger.error(ex.getMessage());
        return buildResponseEntity(BaseResponse.error(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.OK);
    }

    /**
     * Description: handleForbidden
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(PermissionDeniedException.class)
    public final ResponseEntity<Object> handleForbidden(PermissionDeniedException ex, WebRequest request) {
        ex.printStackTrace();
        logger.error(ex.getMessage());
        return buildResponseEntity(BaseResponse.error(ex.getLocalizedMessage(), HttpStatus.FORBIDDEN.value()), HttpStatus.OK);
    }

    /**
     * Description: Handle record not found exception
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler({RecordNotFoundException.class})
    public final ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {
        ex.printStackTrace();
        logger.error(ex.getMessage());
        String message = ex.getLocalizedMessage();
        return buildResponseEntity(BaseResponse.error(message, HttpStatus.BAD_REQUEST.value()), HttpStatus.OK);
    }

    /**
     * Description: Handle authentication exception
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler({UnauthorizedException.class}) //, AuthenticationException.class
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public final ResponseEntity<Object> handleAuthenticationException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        logger.error(ex.getMessage());
        String message = ex.getLocalizedMessage();
        if (!message.equals(ErrorMessages.USER_NOT_FOUND_ERROR.getMessage())) {
            message = ErrorMessages.USER_USERNAME_PASSWORD_ERROR.getMessage();
        }
        BaseResponse<Object> baseResponse = BaseResponse.error(message, HttpStatus.UNAUTHORIZED.value());
        baseResponse.setData("USER_USERNAME_PASSWORD_ERROR"); // Đặt giá trị dữ liệu mong muốn
        return buildResponseEntity(baseResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Description: Handle conflict exception
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(ConflictException.class)
    public final ResponseEntity<Object> handleConflictException(ConflictException ex, WebRequest request) {
        ex.printStackTrace();
        logger.error(ex.getMessage());
        String message = ex.getLocalizedMessage();
        return buildResponseEntity(BaseResponse.error(message, HttpStatus.CONFLICT.value()), HttpStatus.OK);
    }

    /**
     * Description: Handle logic exception
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(LogicException.class)
    public final ResponseEntity<Object> handleLogicException(LogicException ex, WebRequest request) {
        ex.printStackTrace();
        logger.error(ex.getMessage());
        return buildResponseEntity(BaseResponse.error(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.OK);
    }

    /**
     * Description: handle method argument not valid
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    public @NotNull ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatus status,
            @NotNull WebRequest request
    ) {
        ex.printStackTrace();
        List<ErrorResponse> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            ErrorResponse errorResponse = new ErrorResponse(error.getDefaultMessage(), error.getField());
            details.add(errorResponse);
        }
        ObjectError error = ex.getBindingResult().getGlobalError();
        if (error != null) {
            ErrorResponse errorResponse = new ErrorResponse(error.getDefaultMessage(), "");
            details.add(errorResponse);
        }
        return buildResponseEntity(BaseResponse.error(details, status.value()), HttpStatus.OK);


    }

    /**
     * Description: build return response entity
     *
     * @param baseResponse
     * @param status
     * @return
     */
    private ResponseEntity<Object> buildResponseEntity(BaseResponse<Object> baseResponse, HttpStatus status) {
        return new ResponseEntity<>(baseResponse, status);
    }

}

