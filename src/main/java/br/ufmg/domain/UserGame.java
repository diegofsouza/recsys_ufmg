package br.ufmg.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UserGame {
	@Id
	@GeneratedValue
	private Long id;
	private Integer minutesPlayed;
	private Integer gameId;

	public UserGame() {
	}

	public Long getId() {
		return id;
	}

	public UserGame(Integer gameId, Integer minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
		this.gameId = gameId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMinutesPlayed() {
		return minutesPlayed;
	}

	public void setMinutesPlayed(Integer minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	@Override
	public String toString() {
		return gameId != null ? gameId.toString() : null;
	}
}
