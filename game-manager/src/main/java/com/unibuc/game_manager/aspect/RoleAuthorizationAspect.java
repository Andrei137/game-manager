package com.unibuc.game_manager.aspect;

import com.unibuc.game_manager.annotation.RequireProvider;
import com.unibuc.game_manager.controller.ProviderController;
import com.unibuc.game_manager.model.*;
import com.unibuc.game_manager.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import com.unibuc.game_manager.exception.UnauthorizedException;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public final class RoleAuthorizationAspect {

    private final JWTService jwtService;

    private void checkAuthorization(Class<?>... classes) {
        Object user = jwtService.getUser();
        if (Arrays.stream(classes).noneMatch(_class -> _class.isInstance(user))) {
            throw new UnauthorizedException();
        }
    }

    @Before("@annotation(com.unibuc.game_manager.annotation.RequireAdmin)")
    public void checkAdminAuthorization() {
        jwtService.checkAdmin();
    }

    @Before("@annotation(com.unibuc.game_manager.annotation.RequireCustomer)")
    public void checkCustomerAuthorization() {
        checkAuthorization(Customer.class);
    }

    @Before("@annotation(requireProvider)")
    public void checkProviderAuthorization(JoinPoint joinPoint, RequireProvider requireProvider) {
        Class<?>[] allowed = requireProvider.value();
        if (allowed.length == 0) {
            if (joinPoint.getTarget() instanceof ProviderController<?, ?> providerController) {
                allowed = new Class[]{providerController.getService().getProviderClass()};
            }
        }
        checkAuthorization(allowed);
    }

}