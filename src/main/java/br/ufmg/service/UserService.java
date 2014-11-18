package br.ufmg.service;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufmg.domain.TastePreference;
import br.ufmg.domain.User;
import br.ufmg.repository.UserRepository;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamId;

@Service
public class UserService {
	private static final Logger log = Logger.getLogger(UserService.class);
	// private static final long START_USER_ID = 76561197960287900L;
	// private static final long STEAM_FIRST_USER_ID = 76561197960287930L;
	/** User test created 03-10. */
	private static final long DIEGO_TEST_USER_ID = 76561198157200427L;

	@Autowired
	private UserRepository userRepository;

	public List<User> importUsers() {
		long firstUser = 76561197960315758L;// last stop
		long lastUser = DIEGO_TEST_USER_ID;
		for (long currentUserId = firstUser; currentUserId < lastUser; currentUserId++) {
			User foundUser = this.userRepository.get(currentUserId);
			if (foundUser != null) {
				continue;
			}
			try {
				SteamId steamId = SteamId.create(currentUserId);
				HashMap<Integer, SteamGame> steamGames = steamId.getGames();
				if (steamId != null && steamGames != null && !steamGames.isEmpty()) {
					User user = this.getUser(steamId);
					userRepository.create(user);
				}
				firstUser++;
			} catch (SteamCondenserException e) {
				log.info(e.getMessage() + currentUserId);
			}
		}

		return this.list();
	}

	public List<User> list() {
		return userRepository.list();
	}

	private User getUser(SteamId steamId) {
		long id = steamId.getSteamId64();
		log.info(String.format("Parsing user %d: %s", steamId.getSteamId64(), steamId.getNickname()));
		User user = new User();
		user.setId(id);
		user.setNickname(steamId.getNickname());
		user.setLocation(steamId.getLocation());
		user.setMemberSince(steamId.getMemberSince());
		user.setTastePreferences(this.getTastePreferences(steamId));
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

	private List<TastePreference> getTastePreferences(SteamId steamId) {
		List<TastePreference> tastePreferences = null;
		try {
			HashMap<Integer, SteamGame> steamGames = steamId.getGames();
			if (steamGames != null) {
				tastePreferences = new ArrayList<TastePreference>(steamGames.size());
				for (Entry<Integer, SteamGame> steamGame : steamGames.entrySet()) {
					int gameId = steamGame.getValue().getAppId();
					int totalPlaytime = steamId.getTotalPlaytime(steamGame.getValue().getAppId());
					TastePreference tastePreference = new TastePreference();
					tastePreference.setGameId(gameId);
					tastePreference.setMinutesPlayed(totalPlaytime);
					tastePreference.setUserId(steamId.getSteamId64());

					tastePreferences.add(tastePreference);
				}

			}
		} catch (SteamCondenserException e) {
			log.error(e);
		}

		return tastePreferences;
	}
}
