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

import br.ufmg.domain.TastePreference;
import br.ufmg.domain.User;
import br.ufmg.repository.rowmapper.TastePreferenceRowMapper;
import br.ufmg.repository.rowmapper.UserRowMapper;

@Repository
public class UserRepository extends BaseRepository {
	private static final Logger log = Logger.getLogger(UserRepository.class);
	@Autowired
	private UserRowMapper userRowMapper;
	@Autowired
	private TastePreferenceRowMapper tastePreferenceRowMapper;

	public void create(User user) {
		StringBuilder query = new StringBuilder();
		query.append("insert into user");
		query.append(" (id, location, member_since, friend_ids, nickname)");
		query.append(" values (?, ?, ?, ?, ?)");

		String friendIdStr = null;
		if (!CollectionUtils.isEmpty(user.getFriendIds())) {
			friendIdStr = user.getFriendIds().toString();
		}
		this.jdbcTemplate.update(query.toString(), user.getId(), user.getLocation(), user.getMemberSince(), friendIdStr, user.getNickname());

		if (!CollectionUtils.isEmpty(user.getTastePreferences())) {
			this.saveTastePreferences(user.getId(), user.getTastePreferences());
		}
	}

	private void saveTastePreferences(final long userId, final List<TastePreference> tastePreferences) {
		StringBuilder query = new StringBuilder();
		query.append("insert into taste_preferences");
		query.append(" (user_id, item_id, minutes_played)");
		query.append(" values (?, ?, ?)");

		this.jdbcTemplate.batchUpdate(query.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TastePreference tastePreference = tastePreferences.get(i);
				ps.setLong(1, userId);
				ps.setInt(2, tastePreference.getItemId());
				ps.setInt(3, tastePreference.getMinutesPlayed());
			}

			@Override
			public int getBatchSize() {
				return tastePreferences.size();
			}
		});

	}

	public List<TastePreference> getTastePreferences(long userId) {
		List<TastePreference> tastePreferencs = this.jdbcTemplate.query("select t.* from taste_preferences t where t.user_id = ? order by t.item_id",
				new Object[] { userId }, tastePreferenceRowMapper);

		return tastePreferencs;
	}

	public List<User> list() {
		List<User> users = this.jdbcTemplate.query("select u.* from user u order by u.id", userRowMapper);

		return users;
	}

	public User get(long id) {
		User user = null;
		try {
			user = this.jdbcTemplate.queryForObject("select u.* from user u where u.id = ?", userRowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			log.debug(e.getMessage());
		}

		return user;
	}
}
