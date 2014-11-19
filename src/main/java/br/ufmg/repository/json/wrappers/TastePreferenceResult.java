package br.ufmg.repository.json.wrappers;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import br.ufmg.domain.TastePreference;
import br.ufmg.repository.json.serializers.JsonTastePreferenceResultDeserializer;

@Data
@JsonDeserialize(using = JsonTastePreferenceResultDeserializer.class)
public class TastePreferenceResult {
	private List<TastePreference> tastePreferences;
}
