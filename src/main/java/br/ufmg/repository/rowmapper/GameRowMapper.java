package br.ufmg.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import br.ufmg.domain.Game;

import com.google.gson.Gson;

@Component
public class GameRowMapper implements RowMapper<Game> {

	@Override
	public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
		Game game = new Game();
		Gson gson = new Gson();
		game.setId(rs.getInt("id"));
		game.setName(rs.getString("name"));

		String tags = rs.getString("tags");
		if (!StringUtils.isEmpty(tags)) {
			game.setTags(Arrays.asList(gson.fromJson(tags, String[].class)));
		}

		String categories = rs.getString("categories");
		if (!StringUtils.isEmpty(categories)) {
			game.setCategories(Arrays.asList(gson.fromJson(categories, String[].class)));
		}

		String genres = rs.getString("genres");
		if (!StringUtils.isEmpty(genres)) {
			game.setGenres(Arrays.asList(gson.fromJson(genres, String[].class)));
		}

		String developers = rs.getString("developers");
		if (!StringUtils.isEmpty(developers)) {
			game.setDevelopers(Arrays.asList(gson.fromJson(developers, String[].class)));
		}

		String publishers = rs.getString("publishers");
		if (!StringUtils.isEmpty(publishers)) {
			game.setPublishers(Arrays.asList(gson.fromJson(publishers, String[].class)));
		}

		game.setReleaseDate(rs.getDate("release_date"));
		game.setAbout(rs.getString("about"));
		game.setTotalReview(rs.getInt("total_review"));
		game.setPositivePercentReview(rs.getInt("positive_percent_review"));

		return game;
	}
}
