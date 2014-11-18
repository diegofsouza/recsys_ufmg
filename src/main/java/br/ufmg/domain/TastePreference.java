package br.ufmg.domain;

import lombok.Data;

@Data
public class TastePreference {
	private Long userId;
	private Integer gameId;
	private Integer minutesPlayed;
	private Data lastUpdate;

	@Override
	public String toString() {
		return gameId != null ? gameId.toString() : null;
	}

}
