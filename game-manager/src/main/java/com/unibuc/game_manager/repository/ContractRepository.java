package com.unibuc.game_manager.repository;

import com.unibuc.game_manager.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {

    List<Contract> getContractsByDeveloperId(Integer publisherId);

    List<Contract> getContractsByPublisherId(Integer developerId);

    List<Contract> getContractsByDeveloperIdAndGameId(Integer developerId, Integer gameId);

    List<Contract> getContractsByPublisherIdAndGameId(Integer publisherId, Integer gameId);

    Contract getContractByDeveloperIdAndPublisherIdAndGameId(Integer developerId, Integer publisherId, Integer gameId);

}
