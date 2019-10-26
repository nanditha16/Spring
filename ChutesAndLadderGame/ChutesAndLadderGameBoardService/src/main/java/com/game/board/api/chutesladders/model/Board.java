package com.game.board.api.chutesladders.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gameBoard")
public class Board implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 523175015950997554L;

	@Id
	private String id;

	private int numSquares;
	private List<List<Integer>> ladders;
	private List<List<Integer>> snakes;
	private int game_port;
	private String status;

	private String gameName;

	private Map<String, Integer> playerPositionMapping;

	public Board() {
		super();
	}

	public Board(int numSquares, List<List<Integer>> ladders, List<List<Integer>> snakes, String status,
			String gameName) {
		super();
		this.numSquares = numSquares;
		this.ladders = ladders;
		this.snakes = snakes;
		this.status = status;
		this.gameName = gameName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumSquares() {
		return numSquares;
	}

	public void setNumSquares(int numSquares) {
		this.numSquares = numSquares;
	}

	public List<List<Integer>> getLadders() {
		return ladders;
	}

	public void setLadders(List<List<Integer>> ladders) {
		this.ladders = ladders;
	}

	public List<List<Integer>> getSnakes() {
		return snakes;
	}

	public void setSnakes(List<List<Integer>> snakes) {
		this.snakes = snakes;
	}

	public int getGame_port() {
		return game_port;
	}

	public void setGame_port(int game_port) {
		this.game_port = game_port;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Map<String, Integer> getPlayerPositionMapping() {
		return playerPositionMapping;
	}

	public void setPlayerPositionMapping(Map<String, Integer> playerPositionMapping) {
		this.playerPositionMapping = playerPositionMapping;
	}

}
