package br.ufmg.domain;

import java.util.Date;
import java.util.List;

public class User {
	private Long id;
	private String nickname;
	private Float hoursPlayed;
	private String location;
	private Date memberSince;
	private List<Game> games;
	private List<User> friends;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getHoursPlayed() {
		return hoursPlayed;
	}

	public void setHoursPlayed(Float hoursPlayed) {
		this.hoursPlayed = hoursPlayed;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getMemberSince() {
		return memberSince;
	}

	public void setMemberSince(Date memberSince) {
		this.memberSince = memberSince;
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return String.format("%d: %s", this.id, this.nickname);
	}
}
