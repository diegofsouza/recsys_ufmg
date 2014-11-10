package br.ufmg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import br.ufmg.domain.Game;
import br.ufmg.domain.User;
import br.ufmg.repository.GameRepository;
import br.ufmg.repository.UserRepository;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamId;

public class UserService {
	private static final Logger log = Logger.getLogger(UserRepository.class);
	private static final long START_USER_ID = 76561100000000000L;
	// private static final long STEAM_FIRST_USER_ID = 76561197960287930L;
	/** User test created 03-10. */
	private static final long DIEGO_TEST_USER_ID = 76561198157200427L;
	private GameRepository gameRepository = new GameRepository();
	private UserRepository userRepository = new UserRepository();

	public void importUsers() {
		long error = 0;
		long ok = 0;
		long firstUser = START_USER_ID;
		long lastUser = DIEGO_TEST_USER_ID;
		for (int i = 0; i < lastUser; i++) {
			try {
				SteamId id = SteamId.create(firstUser++);
				if (id != null) {
					ok++;
					User user = getUser(id);
					userRepository.storeUser(user);
				}
			} catch (SteamCondenserException e) {
				error++;
			}
		}

		System.out.println("Errors: " + error + " / Ok: " + ok);
	}

	private User getUser(SteamId steamId) {
		long id = steamId.getSteamId64();
		log.info("Parsing user " + id);
		User user = new User();
		user.setId(id);
		user.setNickname(steamId.getNickname());
		user.setHoursPlayed(steamId.getHoursPlayed());
		user.setLocation(steamId.getLocation());
		user.setMemberSince(steamId.getMemberSince());
		user.setFriends(this.getFriends(steamId));
		user.setGames(this.getGames(steamId));

		return user;
	}

	private List<Game> getGames(SteamId steamId) {
		List<Game> games = null;
		try {
			if (steamId.getGames() != null) {
				games = new ArrayList<Game>(steamId.getGames().size());
				for (Entry<Integer, SteamGame> steamGame : steamId.getGames().entrySet()) {
					int gameId = steamGame.getValue().getAppId();
					Game game = this.gameRepository.get(Long.valueOf(gameId));
					if (game != null) {
						games.add(game);
					} else {
						log.info(String.format("User game not found: [%d]: %s", gameId, steamGame.getValue().getName()));
					}
				}

			}
		} catch (SteamCondenserException e) {
			log.error(e);
		}

		return games;
	}

	private List<User> getFriends(SteamId id) {
		List<User> friends = new ArrayList<User>();
		try {
			for (SteamId steamId : id.getFriends()) {
				User user = this.userRepository.get(steamId.getSteamId64());
				if (user == null) {
					user = this.getUser(steamId);
					userRepository.storeUser(user);
				}

				friends.add(user);
			}
		} catch (SteamCondenserException e) {
			log.error(e);
		}

		return friends;
	}
}
