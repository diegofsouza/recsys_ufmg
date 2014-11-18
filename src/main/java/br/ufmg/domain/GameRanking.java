package br.ufmg.domain;

import lombok.Data;

@Data
public class GameRanking {
	private Integer id;
	private Double ranking = 0.0;
	private Integer minutesPlayed = 0;

	public void addRankingValue(Double value) {
		this.ranking += value;
	}

	public void addMinutesPlayed(int minutesPlayed) {
		this.minutesPlayed += minutesPlayed;
	}

	@Override
	public String toString() {
		return String.format("[%d] - %.2f ", this.id, this.ranking);
	}

}
