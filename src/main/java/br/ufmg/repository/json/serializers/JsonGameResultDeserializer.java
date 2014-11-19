package br.ufmg.repository.json.serializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import br.ufmg.domain.Game;
import br.ufmg.repository.adapter.GameParser;
import br.ufmg.repository.json.wrappers.GameResult;

public class JsonGameResultDeserializer extends JsonDeserializer<GameResult> {
	private static final Logger log = Logger.getLogger(JsonGameResultDeserializer.class);
	public static final String STEAM_URL_BASE = "http://store.steampowered.com/agecheck/app/";
	public static final String STEAM_APP_URL_PARAMS = "?ageDay=1&ageMonth=January&ageYear=1984";
	private static final String APP_URL_PARTIAL = "/app/";

	@Override
	public GameResult deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		GameResult result = new GameResult();
		JsonNode root = jp.readValueAsTree();
		JsonNode response = root.get("applist");
		result.setGames(this.getUsers(response.get("apps")));

		return result;
	}

	private List<Game> getUsers(JsonNode gamesJson) {
		List<Game> games = new ArrayList<Game>();

		if (gamesJson != null && gamesJson.isArray()) {
			Iterator<JsonNode> elements = gamesJson.getElements();
			while (elements.hasNext()) {
				JsonNode gameJson = elements.next();
				Game game = new Game();
				game.setId(gameJson.get("appid").asInt());
				game.setName(gameJson.get("name").getTextValue());
				try {
					String appUrl = STEAM_URL_BASE + game.getId() + STEAM_APP_URL_PARAMS;
					Response response = Jsoup.connect(appUrl)
							.userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36")
							.referrer("http://www.google.com").followRedirects(true).timeout(12000).execute();

					Document html = response.parse();
					if (!this.isDocumentValid(game, html)) {
						continue;
					}

					GameParser parser = new GameParser();
					Game parsedGame = parser.parseGame(html, game.getId());
					if (parsedGame != null) {
						games.add(parsedGame);
					}

				} catch (IOException e) {
					log.error(e.getMessage() + ": " + game.toString());
				}
			}
		}

		return games;
	}

	private boolean isDocumentValid(Game game, Document html) {
		boolean isValid = false;
		String gameUrlPartial = APP_URL_PARTIAL + game.getId();
		isValid = html.location().endsWith(gameUrlPartial) || html.location().endsWith(gameUrlPartial + "/");
		if (!isValid) {
			log.info(String.format("Ignoring Game: URL diff: [%s][%s][%s]", html.location(), gameUrlPartial, game.toString()));
		}

		return isValid;
	}
}
