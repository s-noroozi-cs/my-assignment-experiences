package com.backbase.provider;

import com.backbase.model.ApiKeyAuthenticationToken;
import com.backbase.model.entity.ServiceKey;
import com.backbase.model.repository.ServiceKeyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {
    private ServiceKeyRepo serviceKeyRepo;

    @Autowired
    public void setServiceKeyRepo(ServiceKeyRepo serviceKeyRepo) {
        this.serviceKeyRepo = serviceKeyRepo;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String apiKey = (String) authentication.getPrincipal();

        if (ObjectUtils.isEmpty(apiKey)) {
            throw new InsufficientAuthenticationException("No API key in request");
        } else {
            Optional<ServiceKey> serviceKey =
                    serviceKeyRepo.findByKeyAndExpirationTimeAfter(apiKey, LocalDateTime.now());

            if (serviceKey.isPresent()) {
                return new ApiKeyAuthenticationToken(serviceKey.get(), true);
            }
            throw new BadCredentialsException("API Key is invalid");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
