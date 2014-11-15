package br.ufmg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import br.ufmg.domain.User;
import br.ufmg.domain.UserGame;
import br.ufmg.repository.UserRepository;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamId;

public class UserService {
	private static final Logger log = Logger.getLogger(UserRepository.class);
	private static final long START_USER_ID = 76561197960287900L;
	// private static final long STEAM_FIRST_USER_ID = 76561197960287930L;
	/** User test created 03-10. */
	private static final long DIEGO_TEST_USER_ID = 76561198157200427L;
	private UserRepository userRepository = new UserRepository();

	public void importUsers() {
		long firstUser = START_USER_ID;
		long lastUser = DIEGO_TEST_USER_ID;
		for (long currentUserId = firstUser; currentUserId < lastUser; currentUserId++) {
			User foundUser = this.userRepository.get(currentUserId);
			if (foundUser != null) {
				continue;
			}
			try {
				SteamId steamId = SteamId.create(currentUserId);
				if (steamId != null) {
					User user = this.getUser(steamId);
					if (!CollectionUtils.isEmpty(user.getUserGames())) {
						userRepository.storeUser(user);
					}
				}
				firstUser++;
			} catch (SteamCondenserException e) {
				log.info(e.getMessage() + currentUserId);
			}
		}

		userRepository.close();
	}

	private User getUser(SteamId steamId) {
		long id = steamId.getSteamId64();
		log.info(String.format("Parsing user %d: %s", steamId.getSteamId64(), steamId.getNickname()));
		User user = new User();
		user.setId(id);
		user.setNickname(steamId.getNickname());
		user.setLocation(steamId.getLocation());
		user.setMemberSince(steamId.getMemberSince());
		user.setUserGames(this.getUserGames(steamId));
		user.setFriendIds(this.getFriendIds(steamId));

		return user;
	}

	private List<Long> getFriendIds(SteamId id) {
		List<Long> friendIds = null;

		try {
			SteamId[] friends = id.getFriends();
			friendIds = new ArrayList<Long>(friends.length);
			for (SteamId steamId : friends) {
				friendIds.add(steamId.getSteamId64());
			}
		} catch (SteamCondenserException e) {
			log.error(e);
		}

		return friendIds;
	}

	private List<UserGame> getUserGames(SteamId steamId) {
		List<UserGame> userGames = null;
		try {
			HashMap<Integer, SteamGame> steamGames = steamId.getGames();
			if (steamGames != null) {
				userGames = new ArrayList<UserGame>(steamGames.size());
				for (Entry<Integer, SteamGame> steamGame : steamGames.entrySet()) {
					int gameId = steamGame.getValue().getAppId();
					int totalPlaytime = steamId.getTotalPlaytime(steamGame.getValue().getAppId());
					UserGame userGame = new UserGame(gameId, totalPlaytime);

					userGames.add(userGame);
				}

			}
		} catch (SteamCondenserException e) {
			log.error(e);
		}

		return userGames;
	}
}
