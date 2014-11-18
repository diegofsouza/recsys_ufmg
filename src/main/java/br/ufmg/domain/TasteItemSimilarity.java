package br.ufmg.domain;

import java.math.BigDecimal;

import lombok.Data;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class TasteItemSimilarity {
	private Long sourceItemId;
	private Long targetItemId;
	private BigDecimal similarity;
	private SimilarityType type;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
