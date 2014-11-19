package br.ufmg.domain;

import lombok.Data;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class TastePreference {
	private Long userId;
	private Integer itemId;
	private Integer minutesPlayed;
	private Data lastUpdate;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
