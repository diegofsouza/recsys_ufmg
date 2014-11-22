package br.ufmg.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufmg.domain.Game;
import br.ufmg.domain.GameRanking;
import br.ufmg.domain.TastePreference;
import br.ufmg.domain.User;
import br.ufmg.repository.GameRankingRepository;
import br.ufmg.repository.GameRepository;
import br.ufmg.repository.GameRestRepository;
import br.ufmg.repository.UserRepository;

@Service
public class GameService {
	private static final Logger log = Logger.getLogger(GameService.class);
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private GameRestRepository gameRestRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private GameRankingRepository gameRankingRepository;

	public List<Game> importGames() {
		List<Game> games = gameRestRepository.importGames();
		gameRepository.create(games);

		return gameRepository.list();
	}

	public List<GameRanking> ranking() {
		List<Game> games = gameRepository.list();
		List<User> users = userRepository.list();
		gameRankingRepository.clear();
		for (final Game game : games) {
			GameRanking gameRanking = new GameRanking();
			gameRanking.setId(game.getId());
			for (User user : users) {
				if (CollectionUtils.isEmpty(user.getTastePreferences())) {
					user.setTastePreferences(userRepository.getTastePreferences(user.getId()));
				}

				TastePreference foundTaste = (TastePreference) CollectionUtils.find(user.getTastePreferences(), new Predicate() {
					@Override
					public boolean evaluate(Object object) {
						TastePreference currentTaste = (TastePreference) object;
						return game.getId().equals(currentTaste.getItemId());
					}
				});

				if (foundTaste != null) {
					gameRanking.addRankingValue(1.0);
					gameRanking.addMinutesPlayed(foundTaste.getMinutesPlayed());
				}
			}

			gameRankingRepository.create(gameRanking);
			log.info(String.format("Ranking saved: [%s - %dMin:%.1fx]", game.getName(), gameRanking.getMinutesPlayed(), gameRanking.getRanking()));

		}

		return gameRankingRepository.list();
	}

	public List<Game> list() {
		return gameRepository.list();
	}

	public Game get(Integer id) {
		return gameRepository.get(id);
	}

	public List<GameRanking> rankingList() {
		return gameRankingRepository.list();
	}
}
