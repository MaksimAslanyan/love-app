package com.example.datinguserapispring.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

import static com.example.datinguserapispring.exception.Error.ACCESS_DENIED;
import static com.example.datinguserapispring.util.JsonUtil.writeToJson;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;



@Component
public class ApiAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        Error error = ACCESS_DENIED;
        ApiError apiError =
                new ApiError(error.getHttpStatus(), error.getCode(), Instant.now(), error.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(error.getHttpStatus().value());
        response.getWriter().write(writeToJson(apiError));
    }
}
