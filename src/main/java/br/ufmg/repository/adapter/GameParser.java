package br.ufmg.repository.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.ufmg.domain.Game;
import br.ufmg.domain.Review;

public class GameParser {

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
	private Game game;

	public GameParser(Game game) {
		this.game = game;
	}

	public void parseGame(Document html) {
		List<String> categories = this.getElementTexts(html, CATEGORIES_SELECTOR);
		if (!this.isDownloadableContent(categories)) {
			System.out.println("parsing game: " + this.game.toString());
			this.game.setCategories(categories);

			List<String> tags = this.getElementTexts(html, TAGS_SELECTOR);
			this.game.setTags(tags);

			String releaseDateStr = html.select(RELEASE_DATE_SELECTOR).text();
			Date releaseDate = this.parseDateValue(releaseDateStr);
			this.game.setRelease(releaseDate);

			String reviewStr = html.select(REVIEW_SELECTOR).get(0).attr(REVIEW_TEXT_DATA_ATTR);
			Review review = this.parseReview(reviewStr);
			this.game.setReview(review);

			String about = html.select(GAME_DESCRIPTION_SELECTOR).text();
			this.game.setAbout(about);

			Elements gameDetailsElements = html.select(GAME_DETAILS_BASE_SELECTOR);
			List<String> genres = this.getElementTexts(gameDetailsElements, GENRES_SELECTOR);
			this.game.setGenres(genres);

			List<String> developers = this.getElementTexts(gameDetailsElements, DEVELOPERS_SELECTOR);
			this.game.setDevelopers(developers);

			List<String> publishers = this.getElementTexts(gameDetailsElements, PUBLISHERS_SELECTOR);
			this.game.setPublishers(publishers);
		} else {
			System.err.println("DLC: " + this.game.toString());
			this.game = null;
		}

	}

	private boolean isDownloadableContent(List<String> categories) {
		return categories.contains(DLC_CATEGORY);
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
			releaseDate = new SimpleDateFormat(RELEASE_DATE_FORMAT).parse(releaseDateStr);
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
