package com.weflop.GameService.Database;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.weflop.GameService.Database.DomainObjects.GameDocument;

public interface GameRepository extends MongoRepository<GameDocument, String> {
	@Override
	public Optional<GameDocument> findById(String id);
}
