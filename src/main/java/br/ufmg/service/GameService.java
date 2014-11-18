package br.ufmg.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufmg.domain.Game;
import br.ufmg.domain.GameRanking;
import br.ufmg.domain.User;
import br.ufmg.domain.UserGame;
import br.ufmg.repository.GameRepository;
import br.ufmg.repository.GameRestRepository;

@Service
public class GameService {
	private static final Logger log = Logger.getLogger(GameService.class);
	@Autowired
	GameRepository gameRepository;
	@Autowired
	GameRestRepository gameRestRepository;

	public List<Game> importGames() {
		List<Game> games = gameRestRepository.importGames();
		gameRepository.create(games);

		return games;
	}

	public void buildGameRanking() {
		List<Game> games = gameRepository.list();
		List<User> users = null;// userRepository.list();
		UserGame userGame = new UserGame();
		for (Game game : games) {
			GameRanking gameRanking = new GameRanking();
			gameRanking.setId(game.getId());
			userGame.setGameId(game.getId());
			for (User user : users) {
				if (user.getUserGames().contains(userGame)) {
					gameRanking.addRankingValue(1.0);
				}
			}
			log.info(gameRanking);
		}
	}

	public List<Game> list() {
		return gameRepository.list();
	}

	public Game get(Integer id) {
		return gameRepository.get(id);
	}
}
