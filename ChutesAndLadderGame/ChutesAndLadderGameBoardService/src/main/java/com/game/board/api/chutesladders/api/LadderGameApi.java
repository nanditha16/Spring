package com.game.board.api.chutesladders.api;

import com.game.board.api.chutesladders.model.Board;

public interface LadderGameApi {
	Board createGameBoard();

	Board gamePlay(String gameName, String playerName, Integer diceRolled);
}
