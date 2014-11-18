package br.ufmg.service.builders;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class ItemBasedRecommenderBuilder implements RecommenderBuilder {

	@Override
	public Recommender buildRecommender(DataModel model) throws TasteException {
		ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(model);
		Recommender recommender = new GenericItemBasedRecommender(model, itemSimilarity);

		return recommender;
	}
}