package com.backbase.aspect;

import com.backbase.annotation.AuthorizationChecker;
import com.backbase.exception.ServiceAccessDeniedException;
import com.backbase.model.ApiKeyAuthenticationToken;
import com.backbase.model.entity.ServiceAccess;
import com.backbase.model.entity.ServiceKey;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizationAspect {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationAspect.class);

    @Around("@annotation(authorizationChecker)")
    public Object authorizationCheckerAround(ProceedingJoinPoint joinPoint, AuthorizationChecker authorizationChecker) throws Throwable {
        String requireAccess = authorizationChecker.serviceAccessName().getValue();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof ApiKeyAuthenticationToken) {
            ServiceKey serviceKey = ((ApiKeyAuthenticationToken) authentication).getServiceKey();

            boolean hasNotAccess = serviceKey.getServiceAccesses().stream()
                    .map(ServiceAccess::getAccessName)
                    .noneMatch(i -> i.equalsIgnoreCase(requireAccess));
            if (hasNotAccess) {
                String serviceName = joinPoint.getSignature().getDeclaringTypeName() + "-"
                        + joinPoint.getSignature().getName();
                String msg = "ServiceAccessDeniedException, Service: " + serviceName +
                        "  required access: " + requireAccess +
                        " , owner: " + serviceKey.getOwner();
                logger.error(msg);
                //handle it using advice
                throw new ServiceAccessDeniedException(msg);
            }
        }
        return joinPoint.proceed();
    }
}
