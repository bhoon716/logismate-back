package hangman.logismate.repository;

import hangman.logismate.entity.Container;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContainerRepository extends JpaRepository<Container, Long> {
}
