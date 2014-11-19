package br.ufmg.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.ufmg.domain.GameRanking;
import br.ufmg.repository.rowmapper.GameRakingRowMapper;

@Repository
public class GameRankingRepository extends BaseRepository {
	@Autowired
	private GameRakingRowMapper gameRakingRowMapper;
	@Autowired
	private UserRepository userRepository;

	public void create(GameRanking ranking) {
		StringBuilder query = new StringBuilder();
		query.append("insert into game_ranking");
		query.append(" (id, ranking, minutes_played)");
		query.append(" values (?, ?, ?)");

		this.jdbcTemplate.update(query.toString(), ranking.getId(), ranking.getRanking(), ranking.getMinutesPlayed());
	}

	public List<GameRanking> list() {
		List<GameRanking> gameRanking = this.jdbcTemplate.query("select g.* from game_ranking g order by g.id, g.ranking, g.minutes_played",
				gameRakingRowMapper);

		return gameRanking;
	}
}
