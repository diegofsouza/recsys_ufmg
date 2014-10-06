package br.ufmg.repository.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.ufmg.database.Connection;
import br.ufmg.domain.Game;
import br.ufmg.domain.Review;
import br.ufmg.repository.GameRepository;

import com.db4o.ObjectContainer;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class GamesAdapter implements JsonDeserializer<List<Game>> {
	private static final String APP_NAME_SELECTOR = "div.game_title_area div.apphub_AppName";
	private static final String RELEASE_DATE_SELECTOR = "div.release_date span.date";
	private static final String DLC_CATEGORY = "Downloadable Content";
	private static final String GAME_DESCRIPTION_SELECTOR = "div.game_description_snippet";
	private static final String REVIEW_TEXT_DATA_ATTR = "data-store-tooltip";
	private static final String REVIEW_SELECTOR = "div[itemprop=aggregateRating]";
	private static final String GAME_DETAILS_BASE_SELECTOR = "div.game_details";
	private static final String GENRES_SELECTOR = "div.details_block a[href*=genre]";
	private static final String DEVELOPERS_SELECTOR = "div.details_block a[href*=developer]";
	private static final String PUBLISHERS_SELECTOR = "div.details_block a[href*=publisher]";
	private static final String CATEGORIES_SELECTOR = "div#category_block a.name";
	private static final String TAGS_SELECTOR = "div.popular_tags a.app_tag";

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
					this.parseGame(game, html);
					if (game != null) {
						games.add(game);
						this.storeGame(game);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return games;
	}

	private void storeGame(Game game) {
		ObjectContainer connection = Connection.getInstance().getConnection();
		connection.store(game);
		connection.commit();
		connection.close();
	}

	private void parseGame(Game game, Document html) {
		List<String> categories = this.getElementTexts(html, CATEGORIES_SELECTOR);
		if (!categories.contains(DLC_CATEGORY)) {
			System.out.println("parsing game: " + game.toString());
			game.setCategories(categories);

			List<String> tags = this.getElementTexts(html, TAGS_SELECTOR);
			game.setTags(tags);

			String releaseDateStr = html.select(RELEASE_DATE_SELECTOR).text();
			Date releaseDate = this.parseDateValue(releaseDateStr);
			game.setRelease(releaseDate);

			String reviewStr = html.select(REVIEW_SELECTOR).get(0).attr(REVIEW_TEXT_DATA_ATTR);
			Review review = this.parseReview(reviewStr);
			game.setReview(review);

			String about = html.select(GAME_DESCRIPTION_SELECTOR).text();
			game.setAbout(about);

			Elements gameDetailsElements = html.select(GAME_DETAILS_BASE_SELECTOR);
			List<String> genres = this.getElementTexts(gameDetailsElements, GENRES_SELECTOR);
			game.setGenres(genres);

			List<String> developers = this.getElementTexts(gameDetailsElements, DEVELOPERS_SELECTOR);
			game.setDevelopers(developers);

			List<String> publishers = this.getElementTexts(gameDetailsElements, PUBLISHERS_SELECTOR);
			game.setPublishers(publishers);
		} else {
			System.err.println("DLC: " + game.toString());
			game = null;
		}

	}

	private Review parseReview(String reviewStr) {
		String positiveReviewPercentage = reviewStr.split("%")[0];
		String totalReview = reviewStr.split("[0-9]+%\\sof\\sthe\\s")[1];
		totalReview = totalReview.split("\\suser\\sreviews")[0];
		totalReview = totalReview.replace(",", "");

		Review review = new Review();
		review.setPositivePercent(Integer.parseInt(positiveReviewPercentage));
		review.setTotal(Integer.parseInt(totalReview));

		return review;
	}

	private Date parseDateValue(String releaseDateStr) {
		Date releaseDate = null;
		try {
			releaseDate = new SimpleDateFormat("d MMM, yyyy").parse(releaseDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return releaseDate;
	}

	private List<String> getElementTexts(Elements elements, String selector) {
		return getElementTextsBase(null, elements, selector);
	}

	private List<String> getElementTexts(Document html, String selector) {
		return this.getElementTextsBase(html, null, selector);
	}

	private List<String> getElementTextsBase(Document html, Elements elements, String selector) {
		List<String> values = new ArrayList<String>();
		Iterator<Element> iterator = null;
		if (html != null) {
			iterator = html.select(selector).iterator();
		} else {
			iterator = elements.select(selector).iterator();

		}
		while (iterator.hasNext()) {
			Element element = iterator.next();
			values.add(element.text());
		}

		return values;
	}

}
