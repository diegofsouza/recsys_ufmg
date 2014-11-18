package br.ufmg.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import br.ufmg.domain.TasteItemSimilarity;

@Component
public class SimilarityRowMapper implements RowMapper<TasteItemSimilarity> {

	@Override
	public TasteItemSimilarity mapRow(ResultSet rs, int rowNum) throws SQLException {
		TasteItemSimilarity itemSimilarity = new TasteItemSimilarity();
		itemSimilarity.setSourceItemId(rs.getLong("item_id_a"));
		itemSimilarity.setTargetItemId(rs.getLong("item_id_b"));
		itemSimilarity.setSimilarity(rs.getBigDecimal("similarity"));

		return itemSimilarity;
	}

}
