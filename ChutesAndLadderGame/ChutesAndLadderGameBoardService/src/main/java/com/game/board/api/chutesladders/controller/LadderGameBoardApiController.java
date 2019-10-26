package com.game.board.api.chutesladders.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.board.api.chutesladders.model.Board;
import com.game.board.api.chutesladders.service.LadderGameService;

@RestController
public class LadderGameBoardApiController {
	final static Logger logger = LoggerFactory.getLogger(LadderGameBoardApiController.class);

	@Autowired
	private LadderGameService ladderGameService;

	@GetMapping("/games")
	public ResponseEntity<Board> getChuttesAndLadderBoard() throws Exception {
		logger.info("Invoking create() endpoint... ");

		Board board = null;

		try {
			board = ladderGameService.createGameBoard();
			logger.info("Game created. Id=" + board.getId());
			logger.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(board));
			ladderGameService.updateGameBoard(board);

		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
		return ResponseEntity.ok(board);

	}

	@GetMapping("/gamesplay/{gameName}/{playerName}/{diceRolled}")
	public ResponseEntity<Board> gamePlay(@PathVariable String gameName, @PathVariable String playerName,
			@PathVariable Integer diceRolled) {
		logger.info("Invoking gamePlay() endpoint... ");

		Board board = null;
		try {
			board = ladderGameService.gamePlay(gameName, playerName, diceRolled);

			logger.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(board));
			ladderGameService.updateGameBoard(board);

		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(board);

	}

}
