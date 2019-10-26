package com.game.board.api.chutesladders.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.game.board.api.chutesladders.api.LadderGameApi;
import com.game.board.api.chutesladders.config.ConfigUtility;
import com.game.board.api.chutesladders.model.Board;
import com.game.board.api.chutesladders.repository.LadderGameRepository;

@Service
@PropertySource("classpath:gameSetup/boardSetup.properties")
public class LadderGameService implements LadderGameApi {

	final static Logger logger = LoggerFactory.getLogger(LadderGameService.class);

	@Autowired
	private ConfigUtility configUtil;

	@Autowired
	private LadderGameRepository ladderGameRepository;

	@Value("#{'${chuteandladder.boardSetup.snakesFromTo}'.split(';')}")
	private List<String> snakesFromTo;

	@Value("#{'${chuteandladder.boardSetup.laddersFromTo}'.split(';')}")
	private List<String> ladderFromTo;

	private Map<String, Integer> playerPositionMapping = new HashMap<>();
	Integer currentposition = 0;

	@Override
	public Board createGameBoard() {
		// for the user first square is at position 1 but
		// internally is at 0
		int numSquares = configUtil.getIntegerProperty("chuteandladder.boardSetup.numSquares");

		List<List<Integer>> snakes = constructSnakesOrLadderCoOrdinates(snakesFromTo);
		List<List<Integer>> ladders = constructSnakesOrLadderCoOrdinates(ladderFromTo);

		logger.info("Make board");

		Board board = new Board(numSquares, ladders, snakes, "New Game", "ChuttesAndLadder");
		board.setGame_port(Integer.parseInt(configUtil.getStringProperty("local.server.port")));

		ladderGameRepository.save(board);
		return board;
	}

	@Override
	public Board gamePlay(String gameName, String playerName, Integer diceRolled) {

		Optional<Board> optionalBoard = ladderGameRepository.findByGameName(gameName);
		if (optionalBoard.isPresent()) {
			Board board = optionalBoard.get();
			currentposition = playerPositionMapping.getOrDefault(playerName, 0);
			Integer newposition = currentposition + diceRolled;

			// Todo: check for ladder or snake bite to get the right position
			newposition = currentposition + diceRolled;
			newposition = isLadderorSnake(board.getLadders(), newposition);
			newposition = isLadderorSnake(board.getSnakes(), newposition);

			playerPositionMapping.put(playerName, newposition);

			board.setPlayerPositionMapping(playerPositionMapping);

			if (newposition >= board.getNumSquares()) {
				board.setStatus(playerName + " Won");
			} else {
				board.setStatus(playerName + " Turn over.");
			}

			ladderGameRepository.save(board);
		}
		return optionalBoard.get();
	}

	public Board updateGameBoard(Board board) {
		logger.info("update existing player ");
		board = ladderGameRepository.save(board);
		return board;
	}

	private Integer isLadderorSnake(List<List<Integer>> ladderOrSnake, Integer newposition) {
		if (ladderOrSnake.stream().anyMatch(ladder -> ladder.get(0) == newposition)) {

			List<List<Integer>> ladAt = ladderOrSnake.stream().filter(ladder -> ladder.get(0) == newposition)
					.collect(Collectors.toList());

			Integer ladderOrSnakeFrom = ladAt.get(0).get(0);
			Integer ladderOrSnakeTo = ladAt.get(0).get(1);

			logger.info(" Jump from " + ladderOrSnakeFrom + " to " + ladderOrSnakeTo);

			return ladderOrSnakeTo;
		}

		return newposition;
	}

	private List<List<Integer>> constructSnakesOrLadderCoOrdinates(List<String> snakesOrLadderFromTo) {
		List<List<Integer>> snakesOrLadder = new ArrayList<List<Integer>>();

		for (String s : snakesOrLadderFromTo) {
			int[] array = Arrays.asList(s.split(",")).stream().mapToInt(Integer::parseInt).toArray();
			List<Integer> eachSnakeOrLadder = Arrays.stream(array).boxed().collect(Collectors.toList());
			snakesOrLadder.add(eachSnakeOrLadder);
		}

		return snakesOrLadder;
	}

}
