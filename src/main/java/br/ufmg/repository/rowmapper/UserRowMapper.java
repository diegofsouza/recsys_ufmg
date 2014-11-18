package br.ufmg.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import br.ufmg.domain.User;

import com.google.gson.Gson;

@Component
public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		Gson gson = new Gson();
		user.setId(rs.getLong("id"));
		user.setNickname(rs.getString("nickname"));
		user.setLocation(rs.getString("location"));
		user.setMemberSince(rs.getDate("member_since"));

		String friendIds = rs.getString("friend_ids");
		if (!StringUtils.isEmpty(friendIds)) {
			user.setFriendIds(Arrays.asList(gson.fromJson(friendIds, Long[].class)));
		}

		return user;
	}
}
