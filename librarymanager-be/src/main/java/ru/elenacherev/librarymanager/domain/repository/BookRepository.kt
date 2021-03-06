package ru.elenacherev.librarymanager.domain.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.elenacherev.librarymanager.domain.entity.BookEntity
import ru.elenacherev.librarymanager.domain.entity.EditionEntity
import java.util.*

@Repository
@Transactional(propagation = Propagation.MANDATORY)
interface BookRepository : JpaRepository<BookEntity, UUID> {

    fun findAllByEdition(edition: EditionEntity, pageable: Pageable): Page<BookEntity>
}