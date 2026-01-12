package com.unibuc.game_manager.aspect;

import com.unibuc.game_manager.annotation.RequireProvider;
import com.unibuc.game_manager.controller.ProviderController;
import com.unibuc.game_manager.exception.ForbiddenException;
import com.unibuc.game_manager.exception.UnauthorizedException;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public final class RoleAuthorizationAspect {

    private final JWTService jwtService;

    private void checkAuthorization(Class<?>... classes) {
        Object user = jwtService.getCurrentUser();
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
        Object user = jwtService.getCurrentUser();
        assert user instanceof Provider;
        Provider provider = (Provider) user;
        if (!provider.getStatus().equals(Provider.Status.APPROVED)) {
            throw new ForbiddenException("You are banned");
        }
    }

}