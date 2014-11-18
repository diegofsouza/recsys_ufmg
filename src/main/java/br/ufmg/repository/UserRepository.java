package br.ufmg.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import br.ufmg.domain.User;
import br.ufmg.domain.UserGame;
import br.ufmg.repository.rowmapper.UserRowMapper;

@Repository
public class UserRepository extends BaseRepository {
	private static final Logger log = Logger.getLogger(UserRepository.class);
	@Autowired
	UserRowMapper userRowMapper;

	public void create(User user) {
		StringBuilder query = new StringBuilder();
		query.append("insert into User");
		query.append(" (id, location, member_since, friend_ids, nickname)");
		query.append(" values (?, ?, ?, ?, ?)");

		String friendIdStr = null;
		if (!CollectionUtils.isEmpty(user.getFriendIds())) {
			friendIdStr = user.getFriendIds().toString();
		}
		this.jdbcTemplate.update(query.toString(), user.getId(), user.getLocation(), user.getMemberSince(), friendIdStr, user.getNickname());

		if (!CollectionUtils.isEmpty(user.getUserGames())) {
			this.saveUserGames(user.getUserGames());
		}
	}

	private void saveUserGames(final List<UserGame> userGames) {
		StringBuilder query = new StringBuilder();
		query.append("insert into UserGame");
		query.append(" (user_id, game_id, minutes_played)");
		query.append(" values (?, ?, ?)");

		this.jdbcTemplate.batchUpdate(query.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				UserGame userGame = userGames.get(i);
				ps.setLong(1, userGame.getUserId());
				ps.setInt(2, userGame.getGameId());
				ps.setInt(3, userGame.getMinutesPlayed());
			}

			@Override
			public int getBatchSize() {
				return userGames.size();
			}
		});

	}

	public List<User> list() {
		List<User> users = this.jdbcTemplate.query("select u.* from User u order by u.id", userRowMapper);

		return users;
	}

	public User get(long id) {
		User user = null;
		try {
			user = this.jdbcTemplate.queryForObject("select u.* from User u where u.id = ?", userRowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			log.debug(e.getMessage());
		}

		return user;
	}
}
