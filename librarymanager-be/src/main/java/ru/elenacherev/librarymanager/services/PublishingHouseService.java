package ru.elenacherev.librarymanager.services;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.elenacherev.librarymanager.api.dto.Edition;
import ru.elenacherev.librarymanager.api.dto.PublishingHouse;
import ru.elenacherev.librarymanager.domain.entity.PublishingHouseEntity;
import ru.elenacherev.librarymanager.domain.repository.EditionRepository;
import ru.elenacherev.librarymanager.domain.repository.PublishingHouseRepository;
import ru.elenacherev.librarymanager.mapper.EditionMapper;
import ru.elenacherev.librarymanager.mapper.PublishingHouseMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PublishingHouseService {
    private final PublishingHouseRepository publishingHouseRepository;
    private final EditionRepository editionRepository;

    @Transactional(readOnly = true)
    public List<PublishingHouse> findAll() {
        return publishingHouseRepository.findAll()
                .stream()
                .map(PublishingHouseMapper::map)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<PublishingHouse> findAllByPage(Pageable pageable) {
        return publishingHouseRepository.findAll(pageable)
                .map(PublishingHouseMapper::map);
    }
    
    @Transactional(readOnly = true)
    public PublishingHouse findPublishingHouseById(Long publishingHouseId) {
        return publishingHouseRepository.findById(publishingHouseId)
                .map(PublishingHouseMapper::map)
                .orElse(null);
    }
    
    @Transactional
    public PublishingHouse save(Long publishingHouseId,  PublishingHouse dto) {
    	return publishingHouseRepository.findById(publishingHouseId)
                .map(entity -> PublishingHouseMapper.map(dto, entity))
                .map(publishingHouseRepository::saveAndFlush)
                .map(PublishingHouseMapper::map)
                .orElse(null);

    }
    
    @Transactional
    public PublishingHouse create(PublishingHouse dto) {
        return   Optional.of(dto)
                .map(PublishingHouseMapper::map)
                .map(publishingHouseRepository::saveAndFlush)
                .map(PublishingHouseMapper::map)
                .orElse(null);

    }
    
    @Transactional(readOnly = true)
    public Page<Edition> findEditionsByPublishingHouseId(Long publishingHouseId, Pageable pageable) {
        return publishingHouseRepository.findById(publishingHouseId)
        	   .map(publHouseEntity -> 
        	   			editionRepository.findAllByPublishingHouse(publHouseEntity, pageable)
        	   	)
               .orElse(Page.empty())
        	   .map(EditionMapper::map);
    }

	public List<Edition> updateEditionsByPublishingHouseId(
			Long publishingHouseId, 
			List<Long> editionIds
	) {
		return publishingHouseRepository
				.findById(publishingHouseId)
				.map(publishingHouseEntity-> {
					publishingHouseEntity.getEditions().clear();
					publishingHouseEntity.getEditions().addAll(
							editionIds.stream()
  							.map(editionRepository::getOne)
  							.collect(Collectors.toList())
  							);
					return publishingHouseEntity;
				 })
				.map(publishingHouseRepository::saveAndFlush)
				.map(PublishingHouseEntity :: getEditions)
				.orElse(Collections.emptySet())
				.stream()
				.map(EditionMapper :: map)
				.collect(Collectors.toList());
	}
    
}