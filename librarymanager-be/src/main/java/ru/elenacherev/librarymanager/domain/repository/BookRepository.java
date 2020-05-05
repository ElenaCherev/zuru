package ru.elenacherev.librarymanager.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.elenacherev.librarymanager.domain.entity.BookEntity;
import ru.elenacherev.librarymanager.domain.entity.EditionEntity;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Page<BookEntity> findAllByEdition(EditionEntity edition, Pageable pageable);
}