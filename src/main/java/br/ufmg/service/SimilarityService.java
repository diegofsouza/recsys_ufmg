package br.ufmg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufmg.domain.SimilarityType;
import br.ufmg.domain.TasteItemSimilarity;
import br.ufmg.repository.SimilarityRepository;

@Service
public class SimilarityService {
	@Autowired
	SimilarityRepository similarityRepository;

	public List<TasteItemSimilarity> index(SimilarityType type) {
		return similarityRepository.index(type);
	}

	public List<TasteItemSimilarity> list() {
		return similarityRepository.list();
	}

}
