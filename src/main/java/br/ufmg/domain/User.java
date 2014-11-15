package br.ufmg.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	@Id
	private Long id;
	private String nickname;
	private String location;
	private Date memberSince;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserGame> userGames;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Long> friendIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<UserGame> getUserGames() {
		return userGames;
	}

	public void setUserGames(List<UserGame> userGames) {
		this.userGames = userGames;
	}

	public List<Long> getFriendIds() {
		return friendIds;
	}

	public void setFriendIds(List<Long> friendIds) {
		this.friendIds = friendIds;
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
