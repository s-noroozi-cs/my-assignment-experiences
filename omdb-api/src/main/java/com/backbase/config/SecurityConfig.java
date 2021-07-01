package com.backbase.config;

import com.backbase.filter.ApiKeyAuthenticationFilter;
import com.backbase.provider.ApiKeyAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private ApiKeyAuthenticationProvider apiKeyAuthProvider;

    @Autowired
    public void setApiKeyAuthProvider(ApiKeyAuthenticationProvider apiKeyAuthProvider) {
        this.apiKeyAuthProvider = apiKeyAuthProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(apiKeyAuthProvider));
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/h2-console/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/**")
                .authenticated()
                .and().cors()
                //csrf and frame option disable for h2 console security blocked
                .and().csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .addFilterBefore(
                        new ApiKeyAuthenticationFilter(authenticationManager()), AnonymousAuthenticationFilter.class);
    }


}
