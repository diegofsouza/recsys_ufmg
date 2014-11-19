package br.ufmg.domain;

import lombok.Data;

import org.apache.commons.lang3.builder.ToStringBuilder;

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
		return ToStringBuilder.reflectionToString(this);
	}

}
