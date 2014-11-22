package br.ufmg.service;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufmg.domain.TastePreference;
import br.ufmg.domain.User;
import br.ufmg.repository.GameRestRepository;
import br.ufmg.repository.UserRepository;
import br.ufmg.repository.UserRestRepository;

@Service
public class UserService {
	private static final int STEAM_API_USERS_LIMIT = 90;
	private static final Logger log = Logger.getLogger(UserService.class);
	// private static final long START_USER_ID = 76561197960287900L;
	// private static final long STEAM_FIRST_USER_ID = 76561197960287930L;
	/** User test created 03-10. */
	private static final long DIEGO_TEST_USER_ID = 76561198157200427L;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRestRepository userRestRepository;
	@Autowired
	private GameRestRepository gameRestRepository;

	public List<User> importUsers() {
		long firstUser = 76561197960867635L;// last stop
		long lastUser = DIEGO_TEST_USER_ID;
		List<Long> idsTofind = new ArrayList<>(STEAM_API_USERS_LIMIT);
		for (long currentUserId = firstUser; currentUserId < lastUser; currentUserId++) {
			User foundUser = this.userRepository.get(currentUserId);
			if (foundUser != null) {
				continue;
			}
			idsTofind.add(currentUserId);
			if (idsTofind.size() == STEAM_API_USERS_LIMIT) {
				this.buildUserInfo(idsTofind);
				idsTofind = new ArrayList<>(STEAM_API_USERS_LIMIT);
			}
		}

		return this.list();
	}

	private void buildUserInfo(List<Long> idsTofind) {
		List<User> users = userRestRepository.get(idsTofind);
		if (!CollectionUtils.isEmpty(users)) {
			for (User user : users) {
				log.info("parsing user:" + user.toString());
				List<TastePreference> tastePreferences = gameRestRepository.findByUser(user.getId());
				if (CollectionUtils.isEmpty(tastePreferences)) {
					log.info("ignoring user without games:" + user.toString());
					continue;
				}
				user.setTastePreferences(tastePreferences);
				user.setFriendIds(userRestRepository.getFriendsIds(user.getId()));

				userRepository.create(user);
				log.info("User saved!");
			}
		}
	}

	public List<User> list() {
		return userRepository.list();
	}

}
