package com.backbase.model;

import com.backbase.model.entity.ServiceKey;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Transient;
import org.springframework.security.core.authority.AuthorityUtils;

@Transient
public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    private ServiceKey serviceKey;
    private String apiKey;

    public ApiKeyAuthenticationToken(ServiceKey serviceKey, boolean authenticated) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.serviceKey = serviceKey;
        setAuthenticated(authenticated);
    }

    public ApiKeyAuthenticationToken(String apiKey) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.apiKey = apiKey;
        setAuthenticated(false);
    }

    public ApiKeyAuthenticationToken() {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.serviceKey = null;
        setAuthenticated(false);
    }

    public ServiceKey getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(ServiceKey serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }
}
