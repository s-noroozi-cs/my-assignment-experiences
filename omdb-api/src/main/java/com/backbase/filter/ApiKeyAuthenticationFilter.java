package com.backbase.filter;

import com.backbase.model.ApiKeyAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ApiKeyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public ApiKeyAuthenticationFilter(AuthenticationManager authManager) {
        super("/api/**");
        this.setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        Optional<String> apiKeyOptional = Optional.ofNullable(request.getParameter("apikey"));

        ApiKeyAuthenticationToken token =
                apiKeyOptional.map(ApiKeyAuthenticationToken::new).orElse(new ApiKeyAuthenticationToken());

        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        boolean isXmlResponse = "xml".equalsIgnoreCase(request.getParameter("r"));
        String content = isXmlResponse
                ? xmlErrorResponse(failed.getMessage())
                : jsonErrorResponse(failed.getMessage());
        String contentType = isXmlResponse
                ? "application/xml"
                : "application/json";
        response.setHeader("content-type", contentType);
        response.getOutputStream().println(content);
    }

    private String xmlErrorResponse(String msg) {
        return "<root response=\"False\"><error>" + msg + "</error></root>";
    }

    private String jsonErrorResponse(String msg) {
        return "{\"Response\":\"False\",\"Error\":\"" + msg + "\"}";
    }
}
