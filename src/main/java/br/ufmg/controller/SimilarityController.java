package br.ufmg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.ufmg.domain.SimilarityType;
import br.ufmg.domain.TasteItemSimilarity;
import br.ufmg.service.SimilarityService;

@Controller
@RequestMapping("/similarities")
public class SimilarityController {
	@Autowired
	private SimilarityService similarityService;

	@ResponseBody
	@RequestMapping(value = "/build/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TasteItemSimilarity> index(@PathVariable SimilarityType type) {
		return this.similarityService.index(type);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TasteItemSimilarity> list() {
		return this.similarityService.list();
	}

}
