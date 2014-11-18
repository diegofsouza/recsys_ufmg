package br.ufmg.service.builders;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class UserBasedRecommenderBuilder implements RecommenderBuilder {
	private static final int USER_NEIGHBORHOOD_THRESHOLD = 100;

	@Override
	public Recommender buildRecommender(DataModel model) throws TasteException {
		UserSimilarity userSimilarity = new TanimotoCoefficientSimilarity(model);
		UserNeighborhood neighborhood = new ThresholdUserNeighborhood(USER_NEIGHBORHOOD_THRESHOLD, userSimilarity, model);
		Recommender recommender = new GenericBooleanPrefUserBasedRecommender(model, neighborhood, userSimilarity);

		return recommender;
	}
}