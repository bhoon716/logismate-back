package hangman.logismate.repository;

import hangman.logismate.entity.Contract;
import hangman.logismate.entity.User;
import hangman.logismate.enums.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findAllByForwarderAndContractStatus(User forwarder, ContractStatus contractStatus);

    List<Contract> findAllByShipper(User shipper);
}
