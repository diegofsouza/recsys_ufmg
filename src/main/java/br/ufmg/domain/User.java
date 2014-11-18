package br.ufmg.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class User {
	private Long id;
	private String nickname;
	private String location;
	private Date memberSince;
	private List<TastePreference> tastePreferences;
	private List<Long> friendIds;

	@Override
	public String toString() {
		return String.format("%d: %s", this.id, this.nickname);
	}

}
