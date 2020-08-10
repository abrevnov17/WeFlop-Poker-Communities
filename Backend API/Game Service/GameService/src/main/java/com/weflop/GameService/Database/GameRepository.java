package com.weflop.GameService.Database;

import java.util.List;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Sort;

import com.weflop.GameService.Database.DomainObjects.GameDocument;

public interface GameRepository extends MongoRepository<GameDocument, String> {
	@Override
	public Optional<GameDocument> findById(String id);
	
    public List<GameDocument> findByIdAndActiveAndSort(String id, boolean active, Sort sort);
}
