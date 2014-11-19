package br.ufmg.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import br.ufmg.domain.Game;
import br.ufmg.domain.TastePreference;
import br.ufmg.repository.json.wrappers.GameResult;
import br.ufmg.repository.json.wrappers.TastePreferenceResult;

@Repository
public class GameRestRepository {

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	GameRepository gameRepository;

	private static final String STEAM_API = "http://api.steampowered.com";
	private static final String OWNED_GAMES_INTERFACE = "/IPlayerService/GetOwnedGames/v0001/";
	private static final String API_KEY = "?key=626283C7288C1540C6A54AC7D836D25D";
	private static final String STEAM_ID_PARAM = "&steamid=";
	private static final String JSON_FORMAT = "&format=json";
	private static final String GAME_LIST_INTERFACE = "/ISteamApps/GetAppList/v2";

	public List<Game> importGames() {
		StringBuilder url = new StringBuilder(STEAM_API);
		url.append(GAME_LIST_INTERFACE);
		url.append(API_KEY);
		GameResult result = restTemplate.getForObject(url.toString(), GameResult.class);

		return result.getGames();
	}

	public List<TastePreference> findByUser(Long userId) {
		StringBuilder url = new StringBuilder(STEAM_API);
		url.append(OWNED_GAMES_INTERFACE);
		url.append(API_KEY);
		url.append(JSON_FORMAT);
		url.append(STEAM_ID_PARAM);
		url.append(userId);

		TastePreferenceResult result = restTemplate.getForObject(url.toString(), TastePreferenceResult.class);

		return result.getTastePreferences();
	}

}
