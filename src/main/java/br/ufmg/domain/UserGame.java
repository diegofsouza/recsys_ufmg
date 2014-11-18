package br.ufmg.domain;

import lombok.Data;

@Data
public class UserGame {
	private Long id;
	private Long userId;
	private Integer minutesPlayed;
	private Integer gameId;

	@Override
	public String toString() {
		return gameId != null ? gameId.toString() : null;
	}

}
