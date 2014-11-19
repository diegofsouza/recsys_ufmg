package br.ufmg.repository;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import br.ufmg.domain.Game;
import br.ufmg.domain.TastePreference;
import br.ufmg.repository.adapter.GamesAdapter;
import br.ufmg.repository.json.wrappers.TastePreferenceResult;

import com.github.koraktor.steamcondenser.steam.community.WebApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Repository
public class GameRestRepository {
	private static final String KEY_PARAM = "?key=";

	@Autowired
	RestTemplate restTemplate;

	private static final String STEAM_API = "http://api.steampowered.com";
	private static final String OWNED_GAMES_INTERFACE = "/IPlayerService/GetOwnedGames/v0001/";
	private static final String API_KEY = "626283C7288C1540C6A54AC7D836D25D";
	private static final String STEAM_ID_PARAM = "&steamid=";
	private static final String JSON_FORMAT = "&format=json";

	private static final Logger log = Logger.getLogger(GameRestRepository.class);
	private static final String JSON_ROOT = "applist";
	private static final String JSON_APPS = "apps";
	private static final int INTERFACE_VERSION = 2;
	private static final String GET_APP_LIST = "GetAppList";
	private static final String GAME_INTERFACE = "ISteamApps";

	public List<Game> importGames() {
		String json = null;
		try {
			json = WebApi.getJSON(GAME_INTERFACE, GET_APP_LIST, INTERFACE_VERSION);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		Gson gson = new GsonBuilder().registerTypeAdapter(List.class, new GamesAdapter()).create();
		JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
		JsonElement jsonElement = jsonObject.get(JSON_ROOT);
		jsonObject = jsonElement.getAsJsonObject();
		Type type = new TypeToken<List<Game>>() {
		}.getType();

		return gson.fromJson(jsonObject.get(JSON_APPS), type);
	}

	public List<TastePreference> findByUser(Long userId) {
		StringBuilder url = new StringBuilder(STEAM_API);
		url.append(OWNED_GAMES_INTERFACE);
		url.append(KEY_PARAM);
		url.append(API_KEY);
		url.append(JSON_FORMAT);
		url.append(STEAM_ID_PARAM);
		url.append(userId);

		TastePreferenceResult result = restTemplate.getForObject(url.toString(), TastePreferenceResult.class);

		return result.getTastePreferences();
	}

}
