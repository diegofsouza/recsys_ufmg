package br.ufmg.repository.json.serializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import br.ufmg.domain.TastePreference;
import br.ufmg.repository.json.wrappers.TastePreferenceResult;

public class JsonTastePreferenceResultDeserializer extends JsonDeserializer<TastePreferenceResult> {

	@Override
	public TastePreferenceResult deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		TastePreferenceResult result = new TastePreferenceResult();
		JsonNode root = jp.readValueAsTree();
		JsonNode response = root.get("response");
		result.setTastePreferences(this.getTastePreferences(response.get("games")));

		return result;
	}

	private List<TastePreference> getTastePreferences(JsonNode tastePreferencesJson) {
		List<TastePreference> tastePreferences = new ArrayList<TastePreference>();
		if (tastePreferencesJson != null && tastePreferencesJson.isArray()) {
			Iterator<JsonNode> elements = tastePreferencesJson.getElements();
			while (elements.hasNext()) {
				JsonNode tastePreferenceJson = elements.next();
				TastePreference tastePreference = new TastePreference();
				tastePreference.setItemId(tastePreferenceJson.get("appid").asInt());
				tastePreference.setMinutesPlayed(tastePreferenceJson.get("playtime_forever").asInt());

				tastePreferences.add(tastePreference);
			}
		}

		return tastePreferences;
	}
}
