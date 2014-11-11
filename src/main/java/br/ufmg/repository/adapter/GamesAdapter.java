package br.ufmg.repository.adapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import br.ufmg.domain.Game;
import br.ufmg.repository.GameRepository;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class GamesAdapter implements JsonDeserializer<List<Game>> {

	private static final String APP_URL_PARTIAL = "/app/";
	private static final Logger log = Logger.getLogger(GamesAdapter.class);
	private GameRepository gameRepository = new GameRepository();

	@Override
	public List<Game> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonArray gamesJsonArray = json.getAsJsonArray();
		List<Game> games = new ArrayList<Game>();
		log.info("List of games/DLC found: " + gamesJsonArray.size());
		for (JsonElement jsonElement : gamesJsonArray) {
			Game game = context.deserialize(jsonElement, Game.class);
			if (game.getId() < 332470) continue;
			if (gameRepository.existsInDatabase(game)) {
				log.info("Game exists in database: " + game.toString());
				continue;
			}
			try {
				String appUrl = GameRepository.STEAM_APP_URL_BASE + game.getId();
				Document html = Jsoup.connect(appUrl).get();
				if (this.isDocumentValid(game, html)) {
					GameParser parser = new GameParser();
					Game parsedGame = parser.parseGame(html, game.getId());
					if (parsedGame != null) {
						games.add(parsedGame);
						gameRepository.storeGame(parsedGame);
					}
				}
			} catch (IOException e) {
				log.error(e.getMessage() + ": " + game.toString());
			}

		}
		gameRepository.close();
		log.info("Saved games: " + games.size());

		return games;
	}

	private boolean isDocumentValid(Game game, Document html) {
		boolean isValid = false;
		String gameUrlPartial = APP_URL_PARTIAL + game.getId();
		isValid = html.location().endsWith(gameUrlPartial);
		if (!isValid) {
			log.info(String.format("Ignoring Game: URL diff: [%s][%s][%s]", html.location(), gameUrlPartial, game.toString()));
		}

		return isValid;
	}

}
