package br.ufmg.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SimilarityType {
	USER_BASED(1), ITEM_BASED(2), CONTENT_BASED(3);
	@Getter
	private int id;
}
