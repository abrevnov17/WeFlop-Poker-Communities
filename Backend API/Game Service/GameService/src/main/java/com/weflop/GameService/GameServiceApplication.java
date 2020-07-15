package com.weflop.GameService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.weflop.Evaluation.TwoPlusTwo.TwoPlusTwoHandEvaluator;

@SpringBootApplication
public class GameServiceApplication {

	public static void main(String[] args) {
		TwoPlusTwoHandEvaluator.getInstance(); // preloading hand ranks
		SpringApplication.run(GameServiceApplication.class, args);
	}

}
