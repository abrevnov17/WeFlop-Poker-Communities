package com.weflop.Database;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.weflop.Database.DomainObjects.GameDocument;

public interface GameRepository extends MongoRepository<GameDocument, String> {
	public Optional<GameDocument> findById(String id);
}
