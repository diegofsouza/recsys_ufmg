package br.ufmg.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLBooleanPrefJDBCDataModel;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import br.ufmg.domain.Game;
import br.ufmg.domain.SimilarityType;
import br.ufmg.domain.TasteItemSimilarity;
import br.ufmg.repository.rowmapper.SimilarityRowMapper;

@Repository
public class SimilarityRepository extends BaseRepository {
	private static final Logger log = Logger.getLogger(SimilarityRepository.class);
	@Autowired
	private SimilarityRowMapper similarityRowMapper;
	@Autowired
	private GameRepository gameRepository;

	public List<TasteItemSimilarity> index(SimilarityType type) {
		DataSource dataSource = this.jdbcTemplate.getDataSource();
		DataModel dataModel = new MySQLBooleanPrefJDBCDataModel(dataSource);
		ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(dataModel);

		LongPrimitiveIterator sourceItemIterator = null;
		List<Game> games = null;
		try {
			sourceItemIterator = dataModel.getItemIDs();
			games = gameRepository.list();
			this.clear(type);
			while (sourceItemIterator.hasNext()) {
				Long sourceItemId = sourceItemIterator.next();
				for (Game game : games) {
					Long targetItemId = game.getId().longValue();
					if (sourceItemId.equals(targetItemId)) {
						continue;
					}
					Double itemSimilarityCalc = itemSimilarity.itemSimilarity(sourceItemId, targetItemId);
					if (itemSimilarityCalc == null || itemSimilarityCalc.isNaN()) {
						itemSimilarityCalc = 0d;
					}
					BigDecimal itemSimilarityValue = new BigDecimal(itemSimilarityCalc);
					TasteItemSimilarity tasteItemSimilarity = new TasteItemSimilarity();
					tasteItemSimilarity.setSourceItemId(sourceItemId);
					tasteItemSimilarity.setTargetItemId(targetItemId);
					tasteItemSimilarity.setSimilarity(itemSimilarityValue);
					tasteItemSimilarity.setType(type);

					this.create(tasteItemSimilarity);
					log.info("Similarity saved:" + tasteItemSimilarity.toString());
				}
			}
		} catch (TasteException e) {
			log.error(e);
		}

		return this.list();
	}

	public List<TasteItemSimilarity> list() {
		List<TasteItemSimilarity> itemSimilarities = this.jdbcTemplate.query(
				"select s.* from taste_item_similarity s order by s.item_id_a asc, s.item_id_b asc", similarityRowMapper);

		return itemSimilarities;
	}

	public void save(TasteItemSimilarity similarity) {
		TasteItemSimilarity foundSimilarity = this.get(similarity.getSourceItemId(), similarity.getTargetItemId(), similarity.getType().getId());
		if (foundSimilarity != null) {
			this.update(similarity);
		} else {
			this.create(similarity);
		}
	}

	public TasteItemSimilarity get(long sourceItemId, long targetItemId, int typeId) {
		TasteItemSimilarity similarity = null;
		StringBuilder query = new StringBuilder();
		query.append("select s.* from taste_item_similarity s");
		query.append(" where");
		query.append(" s.type_id = ?");
		query.append(" and s.item_id_a = ? and s.item_id_b = ?");
		query.append(" or s.item_id_a = ? and s.item_id_b = ?");
		try {
			similarity = this.jdbcTemplate
					.queryForObject(query.toString(), similarityRowMapper, typeId, sourceItemId, targetItemId, targetItemId, sourceItemId);
		} catch (EmptyResultDataAccessException e) {
			log.debug(e.getMessage());
		}

		return similarity;
	}

	public void create(TasteItemSimilarity similarity) {
		StringBuilder query = new StringBuilder();
		query.append("insert into taste_item_similarity");
		query.append(" (item_id_a, item_id_b, similarity, type_id)");
		query.append(" values (?, ?, ?, ?)");

		this.jdbcTemplate.update(query.toString(), similarity.getSourceItemId(), similarity.getTargetItemId(), similarity.getSimilarity(), similarity.getType()
				.getId());
	}

	public void update(TasteItemSimilarity similarity) {
		StringBuilder query = new StringBuilder();
		query.append("update taste_item_similarity s");
		query.append(" set similarity = ?");
		query.append(" where");
		query.append(" s.type_id = ?");
		query.append(" and s.item_id_a = ? and s.item_id_b = ?");
		query.append(" or s.item_id_a = ? and s.item_id_b = ?");

		this.jdbcTemplate.update(query.toString(), similarity.getSimilarity(), similarity.getType().getId(), similarity.getSourceItemId(),
				similarity.getTargetItemId(), similarity.getTargetItemId(), similarity.getSourceItemId());
	}

	public void clear(SimilarityType type) {
		this.jdbcTemplate.update("delete from taste_item_similarity where type_id = ?", type.getId());
	}

}
