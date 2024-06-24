package com.example.datinguserapispring.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final Map<Class<? extends BaseException>, Supplier<Error>> EXCEPTION_ERROR_MAP = new HashMap<>();

    static {
        EXCEPTION_ERROR_MAP.put(UserNotFoundException.class, () -> Error.USER_NOT_FOUND);
        EXCEPTION_ERROR_MAP.put(AuthenticationException.class, () -> Error.BAD_CREDENTIALS);
        EXCEPTION_ERROR_MAP.put(AdminAlreadyExistException.class, () -> Error.ACCESS_DENIED);
        EXCEPTION_ERROR_MAP.put(ConvertingException.class, ()-> Error.FAILED_JSON_CONVERTING);
        EXCEPTION_ERROR_MAP.put(IOException.class, () -> Error.INTERRUPTED_FILE_READ);
        EXCEPTION_ERROR_MAP.put(UserLikeLimitException.class, () -> Error.USER_LIKE_LIMIT);
        EXCEPTION_ERROR_MAP.put(UserInBlackListException.class, () -> Error.BLOCKED_USER);
        EXCEPTION_ERROR_MAP.put(EntityNotFoundException.class, () -> Error.ENTITY_NOT_FOUND);
        EXCEPTION_ERROR_MAP.put(RequestFailedException.class, () -> Error.APPLE_AUTH_FAILED);
        EXCEPTION_ERROR_MAP.put(IOException.class, () -> Error.FAILED_KEY_READ);
        EXCEPTION_ERROR_MAP.put(FirebaseException.class, () -> Error.FIREBASE_ERROR);
    }



    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleException(BaseException e) {
        Supplier<Error> errorSupplier = EXCEPTION_ERROR_MAP.getOrDefault(e.getClass(), () -> Error.ACCESS_DENIED);
        Error error = errorSupplier.get();

        ApiError apiError = createApiError(error, e.getMessage());
        log.error(String.valueOf(e), error.getMessage());

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    private ApiError createApiError(Error error, String message) {
        return new ApiError(error.getHttpStatus(), error.getCode(), Instant.now(), message);
    }


}
