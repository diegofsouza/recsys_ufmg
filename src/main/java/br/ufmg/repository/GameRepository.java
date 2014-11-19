package br.ufmg.repository;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import br.ufmg.domain.Game;
import br.ufmg.repository.rowmapper.GameRowMapper;

@Repository
public class GameRepository extends BaseRepository {
	private static final Logger log = Logger.getLogger(GameRepository.class);
	@Autowired
	private GameRowMapper gameRowMapper;

	public void create(Game game) {
		StringBuilder query = new StringBuilder();
		query.append("insert into game");
		query.append(" (id, name, tags, categories, genres, developers, publishers, release_date, about, total_review, positive_percent_review)");
		query.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		Gson gson = new Gson();
		String tagsJson = null;
		if (!CollectionUtils.isEmpty(game.getTags())) {
			tagsJson = gson.toJson(game.getTags());
		}
		String categoriesJson = null;
		if (!CollectionUtils.isEmpty(game.getCategories())) {
			categoriesJson = gson.toJson(game.getCategories());
		}
		String genresJson = null;
		if (!CollectionUtils.isEmpty(game.getGenres())) {
			genresJson = gson.toJson(game.getGenres());
		}
		String developersJson = null;
		if (!CollectionUtils.isEmpty(game.getDevelopers())) {
			developersJson = gson.toJson(game.getDevelopers());
		}
		String publishersJson = null;
		if (!CollectionUtils.isEmpty(game.getPublishers())) {
			publishersJson = gson.toJson(game.getPublishers());
		}

		this.jdbcTemplate.update(query.toString(), game.getId(), game.getName(), tagsJson, categoriesJson, genresJson, developersJson, publishersJson,
				game.getReleaseDate(), game.getAbout(), game.getTotalReview(), game.getPositivePercentReview());
	}

	public Game get(int id) {
		Game game = null;
		try {
			game = this.jdbcTemplate.queryForObject("select g.* from game g where g.id = ?", gameRowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			log.debug(e.getMessage());
		}

		return game;
	}

	public List<Game> list() {
		List<Game> games = this.jdbcTemplate.query("select g.* from game g order by g.id", gameRowMapper);

		return games;
	}

	public Integer count(Integer id) {
		String query = "select count(g.id) from Game g where g.id = ?";
		Integer count = this.jdbcTemplate.queryForObject(query, Integer.class, id);

		return count;
	}

	public void create(List<Game> games) {
		for (Game game : games) {
			if (this.count(game.getId()) == 0) {
				this.create(game);
			}
		}
	}

}
