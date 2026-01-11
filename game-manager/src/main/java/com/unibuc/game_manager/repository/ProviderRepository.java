package com.unibuc.game_manager.repository;

import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.model.ProviderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository<T extends Provider> extends JpaRepository<T, Integer> {

    List<T> findByStatus(ProviderStatus status);
    Optional<T> findByIdAndStatus(Integer id, ProviderStatus status);

}
