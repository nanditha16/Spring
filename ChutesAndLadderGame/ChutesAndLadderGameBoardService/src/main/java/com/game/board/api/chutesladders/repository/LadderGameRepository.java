package com.game.board.api.chutesladders.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.game.board.api.chutesladders.model.Board;

public interface LadderGameRepository extends MongoRepository<Board, String> {

	@Query("{'gameName': ?0}")
	Optional<Board> findByGameName(final String gameName);

}
