package br.ufmg.repository;

import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.ufmg.database.Connection;
import br.ufmg.domain.Game;
import br.ufmg.repository.adapter.GamesAdapter;

import com.github.koraktor.steamcondenser.steam.community.WebApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class GameRepository {
	private static final Logger log = Logger.getLogger(GameRepository.class);
	public static final String STEAM_URL_BASE = "http://store.steampowered.com/";
	public static final String STEAM_APP_URL_BASE = STEAM_URL_BASE + "app/";

	private static final String JSON_ROOT = "applist";
	private static final String JSON_APPS = "apps";
	private static final int INTERFACE_VERSION = 2;
	private static final String GET_APP_LIST = "GetAppList";
	private static final String GAME_INTERFACE = "ISteamApps";
	private static final Connection CONNECTION = Connection.getInstance();

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

	public boolean existsInDatabase(Game game) {
		TypedQuery<Long> count = CONNECTION.getConnection().createQuery("select count(g) from Game g where g.id = :id", Long.class);
		count.setParameter("id", game.getId());

		return count.getSingleResult() > 0;
	}

	public void storeGame(Game game) {
		CONNECTION.getConnection().getTransaction().begin();
		CONNECTION.getConnection().persist(game);
		CONNECTION.getConnection().getTransaction().commit();
		CONNECTION.getConnection().flush();
		log.info("Object stored!");
	}

	public void close() {
		CONNECTION.close();
	}

	public Game get(Long id) {
		TypedQuery<Game> game = CONNECTION.getConnection().createQuery("select count(g) from Game g where g.id = :id", Game.class);
		game.setParameter("id", id);

		return game.getSingleResult();

	}
}
