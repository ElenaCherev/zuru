package ru.elenacherev.librarymanager.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.elenacherev.librarymanager.api.dto.Author;
import ru.elenacherev.librarymanager.api.dto.Edition;
import ru.elenacherev.librarymanager.domain.entity.AuthorEntity;
import ru.elenacherev.librarymanager.domain.repository.AuthorRepository;
import ru.elenacherev.librarymanager.domain.repository.EditionRepository;
import ru.elenacherev.librarymanager.mapper.AuthorMapper;
import ru.elenacherev.librarymanager.mapper.EditionMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final EditionRepository editionRepository;

    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(AuthorMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<Author> findAllByPage(Pageable pageable) {
        return authorRepository.findAll(pageable)
                .map(AuthorMapper::map);
    }

    @Transactional(readOnly = true)
    public Author findAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .map(AuthorMapper::map)
                .orElse(null);
    }
    
    @Transactional
    public Author save(Long authorId,  Author dto) {
    	return authorRepository.findById(authorId)
                .map(entity -> AuthorMapper.map(dto, entity))
                .map(authorRepository::saveAndFlush)
                .map(AuthorMapper::map)
                .get();

    }

    @Transactional
    public Author create(Author dto) {
        return   Optional.of(dto)
                .map(AuthorMapper::map)
                .map(authorRepository::saveAndFlush)
                .map(AuthorMapper::map)
                .get();

    }

    @Transactional(readOnly = true)
    public Page<Edition> findEditionsByAuthorId(Long authorId, Pageable pageable) {
        return authorRepository.findById(authorId)
        	   .map(entity -> editionRepository.findAllByAuthor(entity, pageable))
               .orElse(Page.empty())
        	   .map(EditionMapper::map);
    }
    
    @Transactional
	public List<Edition> updateEditionsByAuthorId(Long authorId, List<Long> editionIds) {
		return authorRepository
				.findById(authorId)
				.map(authorEntity-> {
					authorEntity.getEditions().clear();
					authorEntity.getEditions().addAll(
							editionIds.stream()
  							.map(editionRepository::getOne)
  							.collect(Collectors.toList())
  							);
					return authorEntity;
				 })
				.map(authorRepository::saveAndFlush)
				.map(AuthorEntity :: getEditions)
				.orElse(Collections.emptySet())
				.stream()
				.map(EditionMapper :: map)
				.collect(Collectors.toList());
		
	}

}