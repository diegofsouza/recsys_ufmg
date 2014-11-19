package br.ufmg.repository.json.wrappers;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import br.ufmg.domain.Game;
import br.ufmg.repository.json.serializers.JsonGameResultDeserializer;

@Data
@JsonDeserialize(using = JsonGameResultDeserializer.class)
public class GameResult {
	private List<Game> games;
}
