package br.ufmg.domain;

public class GameStats {
	private User user;
	private Game game;
	private Float hoursPlayed;

	public Float getHoursPlayed() {
		return hoursPlayed;
	}

	public void setHoursPlayed(Float hoursPlayed) {
		this.hoursPlayed = hoursPlayed;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public String toString() {
		return String.format("[%s] [%s] [%.2f]", this.user, this.game, this.hoursPlayed);
	}
}
