package br.ufmg.repository;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import br.ufmg.domain.Game;
import br.ufmg.repository.rowmapper.GameRowMapper;

@Repository
public class GameRepository extends BaseRepository {
	private static final Logger log = Logger.getLogger(GameRepository.class);
	@Autowired
	private GameRowMapper gameRowMapper;

	public void create(Game game) {
		StringBuilder query = new StringBuilder();
		query.append("insert into Game");
		query.append(" (id, name, tags, categories, genres, developers, publishers, releaseDate, about, totalReview, positivePercentReview)");
		query.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		this.jdbcTemplate.update(query.toString(), game.getId(), game.getName(), game.getTags(), game.getCategories(), game.getGenres(), game.getDevelopers(),
				game.getPublishers(), game.getReleaseDate(), game.getAbout(), game.getTotalReview(), game.getPositivePercentReview());
	}

	public Game get(int id) {
		Game game = null;
		try {
			game = this.jdbcTemplate.queryForObject("select g.* from Game g where g.id = ?", gameRowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			log.debug(e.getMessage());
		}

		return game;
	}

	public List<Game> list() {
		List<Game> games = this.jdbcTemplate.query("select g.* from Game g order by g.id", gameRowMapper);

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
