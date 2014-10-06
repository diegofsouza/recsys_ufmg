package br.ufmg.repository.adapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import br.ufmg.database.Connection;
import br.ufmg.domain.Game;
import br.ufmg.repository.GameRepository;

import com.db4o.ObjectContainer;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class GamesAdapter implements JsonDeserializer<List<Game>> {

	private static final Logger log = Logger.getLogger(GamesAdapter.class);
	private static final String APP_NAME_SELECTOR = "div.game_title_area div.apphub_AppName";

	@Override
	public List<Game> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonArray gamesJsonArray = json.getAsJsonArray();
		List<Game> games = new ArrayList<Game>();

		for (JsonElement jsonElement : gamesJsonArray) {
			Game game = context.deserialize(jsonElement, Game.class);
			try {
				String appUrl = GameRepository.STEAM_APP_URL_BASE + game.getId();
				Document html = Jsoup.connect(appUrl).get();
				String appName = html.select(APP_NAME_SELECTOR).text();
				if (appUrl.equals(html.location()) && game.getName().equalsIgnoreCase(appName)) {
					GameParser parser = new GameParser(game);
					parser.parseGame(html);
					if (game != null) {
						games.add(game);
						this.storeGame(game);
					}
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}

		}

		return games;
	}

	private void storeGame(Game game) {
		ObjectContainer connection = Connection.getInstance().getConnection();
		connection.store(game);
		connection.commit();
		log.info("Object stored: " + game.toString());
	}

}
