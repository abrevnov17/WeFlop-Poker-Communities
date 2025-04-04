package com.weflop.GameService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.weflop.Evaluation.TwoPlusTwo.TwoPlusTwoHandEvaluator;
import com.weflop.GameService.Game.GameManager;

@SpringBootApplication
public class GameServiceApplication {

	public static void main(String[] args) {
		TwoPlusTwoHandEvaluator.getInstance(); // preloading hand ranks
		SpringApplication.run(GameServiceApplication.class, args);
		
		GameManager.spawnGarbageCollectorThread();
	}

}
