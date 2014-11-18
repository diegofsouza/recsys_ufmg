package br.ufmg.repository.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.ufmg.domain.Game;

public class GameParser {

	private static final Logger log = Logger.getLogger(GameParser.class);
	private static final String RELEASE_DATE_FORMAT = "d MMM, yyyy";
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
	private static final String APP_NAME_SELECTOR = "div.game_title_area div.apphub_AppName";

	public Game parseGame(Document html, Integer appId) {
		Game game = null;
		String appName = html.select(APP_NAME_SELECTOR).text();
		List<String> categories = this.getElementTexts(html, CATEGORIES_SELECTOR);
		if (!StringUtils.isEmpty(appName) || !this.isDownloadableContent(categories)) {
			game = new Game();
			game.setId(appId);
			game.setName(appName);

			log.info("parsing game: " + game.toString());
			game.setCategories(categories);

			List<String> tags = this.getElementTexts(html, TAGS_SELECTOR);
			game.setTags(tags);

			String releaseDateStr = html.select(RELEASE_DATE_SELECTOR).text();
			Date releaseDate = this.parseDateValue(releaseDateStr);
			game.setReleaseDate(releaseDate);

			Elements reviewElement = html.select(REVIEW_SELECTOR);
			if (reviewElement.size() > 0) {
				String reviewStr = reviewElement.get(0).attr(REVIEW_TEXT_DATA_ATTR);
				String positiveReviewPercentage = reviewStr.split("%")[0];
				String totalReview = reviewStr.split("[0-9]+%\\sof\\sthe\\s")[1];
				totalReview = totalReview.split("\\suser\\sreviews")[0];
				totalReview = totalReview.replace(",", "");

				game.setPositivePercentReview(Integer.parseInt(positiveReviewPercentage));
				game.setTotalReview(Integer.parseInt(totalReview));
			}

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
			log.error("DLC ignored: " + appId);
		}

		return game;
	}

	private boolean isDownloadableContent(List<String> categories) {
		return categories.contains(DLC_CATEGORY) || categories.isEmpty();
	}

	private Date parseDateValue(String releaseDateStr) {
		Date releaseDate = null;
		try {
			releaseDate = new SimpleDateFormat(RELEASE_DATE_FORMAT).parse(releaseDateStr);
		} catch (ParseException e) {
			log.error(e.getMessage());
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
