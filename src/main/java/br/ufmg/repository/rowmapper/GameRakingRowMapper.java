package br.ufmg.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import br.ufmg.domain.GameRanking;

@Component
public class GameRakingRowMapper implements RowMapper<GameRanking> {

	@Override
	public GameRanking mapRow(ResultSet rs, int rowNum) throws SQLException {
		GameRanking ranking = new GameRanking();
		ranking.setId(rs.getInt("game_id"));
		ranking.setRanking(rs.getDouble("ranking"));
		ranking.setMinutesPlayed(rs.getInt("minutes_played"));

		return ranking;
	}

}
