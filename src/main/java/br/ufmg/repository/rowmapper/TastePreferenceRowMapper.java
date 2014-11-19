package br.ufmg.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import br.ufmg.domain.TastePreference;

@Component
public class TastePreferenceRowMapper implements RowMapper<TastePreference> {

	@Override
	public TastePreference mapRow(ResultSet rs, int rowNum) throws SQLException {
		TastePreference tastePreference = new TastePreference();
		tastePreference.setUserId(rs.getLong("user_id"));
		tastePreference.setItemId(rs.getInt("item_id"));
		tastePreference.setMinutesPlayed(rs.getInt("minutes_played"));

		return tastePreference;
	}

}
