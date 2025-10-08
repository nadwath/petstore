package com.caceis.petstore.advice;

import com.caceis.petstore.common.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class SuccessResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Only wrap responses that are not already ApiResponse
        return !returnType.getParameterType().equals(ApiResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // Skip wrapping for error responses (4xx, 5xx)
        HttpStatus status = HttpStatus.OK;
        if (response instanceof org.springframework.http.server.ServletServerHttpResponse servletResponse) {
            int statusCode = servletResponse.getServletResponse().getStatus();
            status = HttpStatus.resolve(statusCode);
            if (status == null) {
                status = HttpStatus.OK;
            }
        }

        if (status.isError()) {
            return ApiResponse.error(status.value(), status.getReasonPhrase());
        }

        // Skip for Swagger/OpenAPI endpoints
        String path = request.getURI().getPath();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            return body;
        }

        // Wrap successful response
        if (status == HttpStatus.CREATED) {
            return ApiResponse.created(body);
        } else {
            return ApiResponse.success(body);
        }
    }
}
