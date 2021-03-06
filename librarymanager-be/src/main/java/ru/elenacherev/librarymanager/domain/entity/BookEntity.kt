package ru.elenacherev.librarymanager.domain.entity

import org.hibernate.annotations.ColumnDefault
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Version
import javax.validation.constraints.NotNull

@Entity
@Table(name = "BOOKS")
data class BookEntity(

    @Column(name = "BOOK_ID", nullable = false, updatable = false)
    @GeneratedValue
    @Id
    @get:NotNull
    var bookId: UUID? = null,

    //версия, инкрементится при редактировании записи
    @Column(name = "VERSION", nullable = false)
    @Version
    var version: Int = 0,

    @Column(name = "IS_RESERVED")
    var isReserved: Boolean = false,

    @Column(name = "IS_AVAILABLE")
    var isAvailable: Boolean = false,

    @Column(name = "SHELF_NUMBER")
    var shelfNumber: Int = 0,

    @ManyToOne
    @JoinColumn(name = "EDITION_ID", nullable = false)
    var edition: EditionEntity,

    @OneToMany(mappedBy = "book", cascade = [CascadeType.ALL], orphanRemoval = true)
    val bookUsings: MutableSet<BookUsingEntity> = mutableSetOf()
)