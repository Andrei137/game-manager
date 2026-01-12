package com.unibuc.game_manager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.annotation.RequireProvider;
import com.unibuc.game_manager.dto.ProviderCreateDto;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.service.ProviderService;
import com.unibuc.game_manager.utils.ResponseUtils;
import com.unibuc.game_manager.utils.ValidationUtils;
import com.unibuc.game_manager.utils.ViewUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
public abstract class ProviderController<P extends Provider, D extends ProviderCreateDto> {

    public abstract ProviderService<P, D> getService();

    @GetMapping("")
    @JsonView(ViewUtils.Public.class)
    @ResponseBody
    public ResponseEntity<List<P>> getAll() {
        return ResponseUtils.ok(getService().getProvidersByStatus(Provider.Status.APPROVED));
    }

    @GetMapping("/me")
    @JsonView(ViewUtils.Public.class)
    @RequireProvider
    @ResponseBody
    public ResponseEntity<P> getCurrent() {
        return ResponseUtils.ok(getService().getCurrentUser());
    }

    @GetMapping("/{providerId}")
    @JsonView(ViewUtils.Public.class)
    @ResponseBody
    public ResponseEntity<P> getById(@PathVariable Integer providerId) {
        return ResponseUtils.ok(getService().getProviderByIdAndStatus(providerId, Provider.Status.APPROVED));
    }

    @PutMapping("")
    @JsonView(ViewUtils.Public.class)
    @RequireProvider
    @ResponseBody
    public ResponseEntity<P> updateCurrent(@RequestBody @Validated(ValidationUtils.Update.class) D dto) {
        return ResponseUtils.ok(getService().updateLoggedUser(dto));
    }

}
