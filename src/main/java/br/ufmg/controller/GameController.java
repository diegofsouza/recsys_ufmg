package br.ufmg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.ufmg.domain.Game;
import br.ufmg.domain.GameRanking;
import br.ufmg.service.GameService;

@Controller
@RequestMapping("/games")
public class GameController {
	@Autowired
	private GameService gameService;

	@ResponseBody
	@RequestMapping(value = "/import", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Game> importGames() {
		return gameService.importGames();
	}

	@ResponseBody
	@RequestMapping(value = "/rankings/build", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GameRanking> ranking() {
		return gameService.ranking();
	}

	@ResponseBody
	@RequestMapping(value = "/rankings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GameRanking> rankingList() {
		return gameService.rankingList();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Game> list() {
		return gameService.list();
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Game get(@PathVariable Integer id) {
		return gameService.get(id);
	}
}
