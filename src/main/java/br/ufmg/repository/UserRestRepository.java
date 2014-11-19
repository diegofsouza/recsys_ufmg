package br.ufmg.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import br.ufmg.domain.User;
import br.ufmg.repository.json.wrappers.UserFriendsResult;
import br.ufmg.repository.json.wrappers.UserResult;

@Repository
public class UserRestRepository {
	private static final String STEAM_API = "http://api.steampowered.com";
	private static final String USER_INTERFACE = "/ISteamUser/GetPlayerSummaries/v0002";
	private static final String FRIENDS_INTERFACE = "/ISteamUser/GetFriendList/v0001";
	private static final String API_KEY = "?key=626283C7288C1540C6A54AC7D836D25D";
	private static final String STEAM_IDS_PARAM = "&steamids=";
	private static final String STEAM_ID_PARAM = "&steamid=";

	@Autowired
	RestTemplate restTemplate;

	public List<User> get(List<Long> ids) {
		StringBuilder url = new StringBuilder(STEAM_API);
		url.append(USER_INTERFACE);
		url.append(API_KEY);
		url.append(STEAM_IDS_PARAM);
		url.append(ids.get(0));
		for (int i = 1; i < ids.size(); i++) {
			url.append(",");
			url.append(ids.get(i));
		}

		UserResult result = restTemplate.getForObject(url.toString(), UserResult.class);

		return result.getUsers();
	}

	public List<Long> getFriendsIds(long userId) {
		StringBuilder url = new StringBuilder(STEAM_API);
		url.append(FRIENDS_INTERFACE);
		url.append(API_KEY);
		url.append(STEAM_ID_PARAM);
		url.append(userId);

		UserFriendsResult result = restTemplate.getForObject(url.toString(), UserFriendsResult.class);

		return result.getFriends();
	}
}
