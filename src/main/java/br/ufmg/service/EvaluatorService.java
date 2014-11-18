package br.ufmg.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

import br.ufmg.domain.User;
import br.ufmg.domain.TastePreference;
import br.ufmg.repository.UserRepository;
import br.ufmg.service.builders.ItemBasedRecommenderBuilder;
import br.ufmg.service.builders.UserBasedRecommenderBuilder;

public class EvaluatorService {
//	private static final Logger log = Logger.getLogger(RecommendationService.class);
//	private UserRepository userRepository = new UserRepository();
//
//	public void evaluate() {
//		List<User> users = userRepository.list();
//		DataModel dataModel = new GenericBooleanPrefDataModel(this.getBooleanPrefDataModel(users));
//
//		this.evaluate(dataModel, new UserBasedRecommenderBuilder(), new ItemBasedRecommenderBuilder());
//	}
//
//	private void evaluate(DataModel dataModel, RecommenderBuilder... recommenderBuilders) {
//		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
//		try {
//			for (RecommenderBuilder recommenderBuilder : recommenderBuilders) {
//				double score = evaluator.evaluate(recommenderBuilder, null, dataModel, 0.9, 1.0);
//				log.info(String.format("%s: %.4f", recommenderBuilder.getClass().getSimpleName(), score));
//			}
//		} catch (TasteException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private FastByIDMap<FastIDSet> getBooleanPrefDataModel(List<User> users) {
//		FastByIDMap<FastIDSet> idMap = new FastByIDMap<FastIDSet>();
//		for (User user : users) {
//			FastIDSet userPref = this.getBooleanUserPref(user);
//			idMap.put(user.getId(), userPref);
//		}
//
//		return idMap;
//	}
//
//	private FastIDSet getBooleanUserPref(User user) {
//		FastIDSet purchasedGames = new FastIDSet();
//		for (UserGame userGame : user.getUserGames()) {
//			purchasedGames.add(userGame.getGameId());
//		}
//
//		return purchasedGames;
//	}
}
