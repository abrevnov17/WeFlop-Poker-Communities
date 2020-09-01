package com.weflop.GameService.REST;

import org.springframework.web.bind.annotation.RestController;

import com.weflop.GameService.Database.GameRepository;
import com.weflop.GameService.Database.DomainObjects.GameDocument;
import com.weflop.GameService.Game.Game;
import com.weflop.GameService.Game.GameFactory;
import com.weflop.GameService.Game.GameManager;
import com.weflop.GameService.REST.Errors.ForbiddenOperationException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class RESTController {

	@Autowired
	private GameRepository repository;
	
	@Autowired
	private GameFactory factory;

	@PostMapping("/api/create-game")
	@ResponseBody
	public String createGame(
			@RequestParam(name = "user_id", required = true) String userId,
			@RequestParam(name = "name", required = true) String name,
			@RequestParam(name = "small_blind", required = true) float smallBlind,
			@RequestParam(name = "min_buy_in", required = true) int minBuyInBB,
			@RequestParam(name = "max_buy_in", required = true) int maxBuyInBB
			) {

		if (smallBlind < 0.5) {
			throw new IllegalArgumentException("Small blind must be greater than or equal to 0.5");
		}

		return factory.generateStandardPokerGame(name, smallBlind, minBuyInBB, maxBuyInBB, userId); // returning id of newly created game
	}

	@GetMapping("/api/game-metadata")
	@ResponseBody
	public GameMetadata getGameMetadata(@RequestParam(name = "game_id", required = true) String gameId) {
		Game game = GameManager.ID_TO_GAME.get(gameId);
		
		// first, we check to see if game is on this replica
		if (game != null) {
			return game.getGameMetadata();
		}
		
		System.out.println("Loading game from db to fetch metadata...");
		
		// otherwise, we need to load from database
		Optional<GameDocument> gameDocument = repository.findById(gameId);
		
		if (!gameDocument.isPresent()) {
			throw new IllegalArgumentException("No game exists with id: " + gameId);
		}
		
	    GameDocument doc = gameDocument.get();
		
		return doc.toMetadata();
	}
	
	@GetMapping("/api/ledger")
	@ResponseBody
	public Map<String, Float> getGameLedger(@RequestParam(name = "game_id", required = true) String gameId) {
		Game game = GameManager.ID_TO_GAME.get(gameId);
		
		// first, we check to see if game is on this replica
		if (game != null) {
			return game.getGameMetadata().getLedger();
		}
		
		System.out.println("Loading game from db to fetch ledger...");

		// otherwise, we need to load from database
		Optional<GameDocument> gameDocument = repository.findById(gameId);
		
		if (!gameDocument.isPresent()) {
			throw new IllegalArgumentException("No game exists with id: " + gameId);
		}
		
	    GameDocument doc = gameDocument.get();
		
		return doc.getLedger();
	}
	
	@GetMapping("/api/active-games")
	@ResponseBody
	public List<GameMetadata> getActiveGames(@RequestParam(name = "user_id", required = true) String userId) {
		// fetching all active games by timestamp in descending order
		return repository.findByIdAndActive(userId, true, Sort.by(Sort.Direction.DESC, "startTime"))
				.stream().map(doc -> doc.toMetadata())
				.collect(Collectors.toList());
	}
	
	@GetMapping("/api/archived-games")
	@ResponseBody
	public List<GameMetadata> getArchivedGames(@RequestParam(name = "user_id", required = true) String userId) {
		// fetching all archived games by timestamp in descending order
		return repository.findByIdAndActive(userId, false, Sort.by(Sort.Direction.DESC, "startTime"))
				.stream().map(doc -> doc.toMetadata())
				.collect(Collectors.toList());
	}
	
	@PostMapping("/api/archive-game")
	@ResponseBody
	public void archiveGame(
			@RequestParam(name = "user_id", required = true) String userId,
			@RequestParam(name = "game_id", required = true) String gameId
			) {
		
		// first, we check to see if game is being hosted on this replica
		Game game = GameManager.ID_TO_GAME.get(gameId);
		
		if (game != null) {
			if (!game.archive(userId)) {
				throw new ForbiddenOperationException("Cannot archive a game with active players.");
			}
			
			return;
		}
		
		System.out.println("Loading game from db to archive game...");

		// since game is not on this replica, we must load it from the db
		Optional<GameDocument> gameDocument = repository.findById(gameId);
		
		if (!gameDocument.isPresent()) {
			throw new IllegalArgumentException("No game exists with id: " + gameId);
		}
		
	    GameDocument doc = gameDocument.get();
	    
	    if (!doc.getMetadata().getCreatedBy().equals(userId)) {
	    	throw new ForbiddenOperationException("Can only archive games that you have created.");
	    }
	    
	    if (!doc.isActive()) {
	    	throw new IllegalArgumentException("Game is already archived");
	    }
	    
	    doc.setActive(false); // setting game to be inactive
	    
	    repository.save(doc); // archiving game
	}
}
