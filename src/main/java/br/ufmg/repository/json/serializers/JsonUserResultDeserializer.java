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

import br.ufmg.domain.User;
import br.ufmg.repository.json.wrappers.UserResult;

public class JsonUserResultDeserializer extends JsonDeserializer<UserResult> {

	@Override
	public UserResult deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		UserResult result = new UserResult();
		JsonNode root = jp.readValueAsTree();
		JsonNode response = root.get("response");
		result.setUsers(this.getUsers(response.get("players")));

		return result;
	}

	private List<User> getUsers(JsonNode usersJson) {
		List<User> users = new ArrayList<User>();
		if (usersJson != null && usersJson.isArray()) {
			Iterator<JsonNode> elements = usersJson.getElements();
			while (elements.hasNext()) {
				JsonNode userJson = elements.next();
				User user = new User();
				user.setId(userJson.get("steamid").asLong());

				JsonNode nicknameJson = userJson.get("personaname");
				if (nicknameJson != null) {
					user.setNickname(nicknameJson.getTextValue());
				}

				JsonNode locationJson = userJson.get("loccountrycode");
				if (locationJson != null) {
					user.setLocation(locationJson.getTextValue());
				}

				users.add(user);
			}
		}

		return users;

	}
}
