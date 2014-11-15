package br.ufmg.service;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.log4j.Logger;

import br.ufmg.domain.Game;
import br.ufmg.repository.adapter.GamesAdapter;

import com.github.koraktor.steamcondenser.steam.community.WebApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class GameService {
	private static final Logger log = Logger.getLogger(GameService.class);
	private static final String JSON_ROOT = "applist";
	private static final String JSON_APPS = "apps";
	private static final int INTERFACE_VERSION = 2;
	private static final String GET_APP_LIST = "GetAppList";
	private static final String GAME_INTERFACE = "ISteamApps";

	public void importGames() {
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

		gson.fromJson(jsonObject.get(JSON_APPS), type);
	}
}
